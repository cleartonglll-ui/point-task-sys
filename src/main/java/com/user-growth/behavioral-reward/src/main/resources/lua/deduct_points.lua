-- 积分扣除 Lua 脚本
-- 用于奖励兑换，原子性扣减并防止余额不足
-- KEYS[1]: 用户积分账户 key (user:points:{userId})
-- KEYS[2]: 扣减流水 key (user:points:deduct:{deductId})
-- KEYS[3]: 分布式锁 key (lock:deduct:points:{userId}:{deductId})
-- ARGV[1]: 待扣减积分数量
-- ARGV[2]: 锁过期时间(秒)
-- ARGV[3]: 当前时间戳

local accountKey = KEYS[1]
local recordKey = KEYS[2]
local lockKey = KEYS[3]
local points = tonumber(ARGV[1])
local lockExpire = tonumber(ARGV[2])
local now = tonumber(ARGV[3])

-- 检查是否已扣减
local hasDeducted = redis.call('GET', recordKey)
if hasDeducted then
    return {-2, 'already_deducted', 0}  -- 重复扣减
end

-- 获取分布式锁
local lock = redis.call('SET', lockKey, now, 'NX', 'EX', lockExpire)
if not lock then
    return {-1, 'lock_failed', 0}  -- 获取锁失败
end

-- 获取当前积分
local currentPoints = tonumber(redis.call('GET', accountKey) or '0')

-- 检查余额是否足够
if currentPoints < points then
    -- 释放锁
    redis.call('DEL', lockKey)
    return {-3, 'insufficient_balance', currentPoints}
end

-- 扣减积分
redis.call('INCRBY', accountKey, -points)

-- 记录扣减标记
redis.call('SET', recordKey, now)
redis.call('EXPIRE', recordKey, 86400 * 7)  -- 记录保留7天

-- 释放锁
redis.call('DEL', lockKey)

return {1, 'success', currentPoints - points}
