-- 积分发放 Lua 脚本
-- 防止超发、重复发放，支持每日限额控制
-- KEYS[1]: 用户积分账户 key (user:points:{userId})
-- KEYS[2]: 每日积分统计 key (user:points:daily:{userId}:{date})
-- KEYS[3]: 任务发放记录 key (user:task:record:{userId}:{taskId})
-- KEYS[4]: 分布式锁 key (lock:award:points:{userId}:{taskId})
-- ARGV[1]: 待发放积分数量
-- ARGV[2]: 每日积分上限
-- ARGV[3]: 锁过期时间(秒)
-- ARGV[4]: 当前时间戳

local userId = KEYS[1]
local dailyKey = KEYS[2]
local recordKey = KEYS[3]
local lockKey = KEYS[4]
local points = tonumber(ARGV[1])
local dailyLimit = tonumber(ARGV[2])
local lockExpire = tonumber(ARGV[3])
local now = tonumber(ARGV[4])

-- 检查是否已发放过该任务积分
local hasAwarded = redis.call('GET', recordKey)
if hasAwarded then
    return {-2, 'already_awarded', 0, 0}  -- 重复发放
end

-- 获取分布式锁
local lock = redis.call('SET', lockKey, now, 'NX', 'EX', lockExpire)
if not lock then
    return {-1, 'lock_failed', 0, 0}  -- 获取锁失败
end

-- 获取当前积分
local currentPoints = tonumber(redis.call('GET', userId) or '0')

-- 获取今日已获得积分
local todayPoints = tonumber(redis.call('GET', dailyKey) or '0')

-- 检查每日上限
local canAward = dailyLimit - todayPoints
if canAward <= 0 then
    -- 释放锁
    redis.call('DEL', lockKey)
    return {-3, 'daily_limit_exceeded', currentPoints, todayPoints}
end

-- 计算实际可发放积分
local actualAward = math.min(points, canAward)

-- 增加用户积分
redis.call('INCRBY', userId, actualAward)

-- 增加今日积分统计
redis.call('INCRBY', dailyKey, actualAward)
-- 设置过期时间到第二天
redis.call('EXPIRE', dailyKey, 86400)

-- 记录发放标记
redis.call('SET', recordKey, now)
redis.call('EXPIRE', recordKey, 86400 * 7)  -- 记录保留7天

-- 释放锁
redis.call('DEL', lockKey)

return {1, 'success', currentPoints + actualAward, todayPoints + actualAward}
