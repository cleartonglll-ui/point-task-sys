package com.user.growth.calculation.utils;

/**
 * Utility that holds Lua scripts used for atomic operations in Redis.
 *
 * Typical script responsibilities:
 *  - Check idempotency key
 *  - Check and update user point balance without exceeding limits
 *  - Set idempotency key with TTL
 *
 * In production we would load this script into Redis and execute it with
 * RedisTemplate.evalSha for performance.
 */
public class LuaScriptManager {

    /**
     * Returns a Lua script that performs the grant-points atomic sequence.
     *
     * NOTE: The script text is illustrative; please test and harden before
     * using in production. Keep script short and avoid heavy logic.
     */
    public static String getPointsGrantScript() {
        return "-- ARGV[1]=idempotentKey ARGV[2]=points ARGV[3]=maxBalance\n"
                + "local idKey = ARGV[1]\n"
                + "local points = tonumber(ARGV[2])\n"
                + "local max = tonumber(ARGV[3])\n"
                + "if redis.call('exists', idKey) == 1 then return {err='ALREADY_PROCESSED'} end\n"
                + "local balance = tonumber(redis.call('get', KEYS[1]) or '0')\n"
                + "if (balance + points) > max then return {err='EXCEEDS_LIMIT'} end\n"
                + "redis.call('incrby', KEYS[1], points)\n"
                + "redis.call('set', idKey, 1, 'EX', 3600)\n"
                + "return 'OK'\n";
    }
}
