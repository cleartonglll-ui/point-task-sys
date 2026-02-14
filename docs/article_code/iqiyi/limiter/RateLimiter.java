package com.vivo.pointtask.iqiyi.limiter;

/**
 * 【爱奇艺亮点】限流器 - 云配限流控制
 * 
 * 参考来源：爱奇艺积分系统架构演进
 * 文章链接：https://mp.weixin.qq.com/s/vvSLW4EnsYEyHYIbGl8q2Q
 * 
 * 核心设计：
 * 通过云配限流开关控制集群压力，防止高并发场景下系统过载。
 * 
 * 限流策略：
 * 1. QPS限流：限制每秒请求数
 * 2. 并发限流：限制同时处理的请求数
 * 3. 热点限流：针对热点用户或积分线进行限流
 * 4. 熔断降级：异常率达到阈值时自动熔断
 * 
 * 业务价值：
 * - 保护系统：防止高并发压垮系统
 * - 公平性：确保所有用户都能获得服务
 * - 可控性：通过配置动态调整限流策略
 * 
 * @author 参考：爱奇艺技术产品团队
 * @version 4.0.0
 */
public interface RateLimiter {
    
    /**
     * 【核心方法】尝试获取许可
     * 
     * @param key 限流key（如用户ID、积分线ID）
     * @return 是否获取成功
     */
    boolean tryAcquire(String key);
    
    /**
     * 【核心方法】尝试获取许可（带超时）
     * 
     * @param key 限流key
     * @param timeoutMs 超时时间（毫秒）
     * @return 是否获取成功
     */
    boolean tryAcquire(String key, long timeoutMs);
    
    /**
     * 【核心方法】检查是否被限流
     * 
     * @param key 限流key
     * @return 是否被限流
     */
    boolean isLimited(String key);
    
    /**
     * 【核心方法】获取当前QPS
     * 
     * @param key 限流key
     * @return 当前QPS
     */
    double getCurrentQps(String key);
    
    /**
     * 【核心方法】更新限流配置
     * 
     * 演化说明：
     * 通过配置中心动态更新限流配置，无需重启服务
     * 
     * @param config 限流配置
     */
    void updateConfig(RateLimitConfig config);
}
