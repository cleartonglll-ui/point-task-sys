-- 任务完成次数检查 Lua 脚本
-- KEYS[1]: 任务计数器 key (user:task:count:{userId}:{taskId}:{date})
-- ARGV[1]: 每日最大完成次数
-- ARGV[2]: 是否自增计数器 (1: 自增, 0: 不自增)

local counterKey = KEYS[1]
local maxCount = tonumber(ARGV[1])
local shouldIncrement = tonumber(ARGV[2])

-- 获取当前计数
local currentCount = tonumber(redis.call('GET', counterKey) or '0')

-- 检查是否达到上限
if currentCount >= maxCount then
    return {0, 'limit_exceeded', currentCount, maxCount}
end

-- 如果需要自增
if shouldIncrement == 1 then
    local newCount = currentCount + 1
    redis.call('INCR', counterKey)
    redis.call('EXPIRE', counterKey, 86400)  -- 当天有效
    return {1, 'success', newCount, maxCount}
end

-- 仅检查不自增
return {1, 'success', currentCount, maxCount}
