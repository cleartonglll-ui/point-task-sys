package com.vivo.pointtask.iqiyi.limiter;

/**
 * 【爱奇艺亮点】限流配置
 * 
 * @author 参考：爱奇艺技术产品团队
 * @version 4.0.0
 */
public class RateLimitConfig {
    
    /**
     * 限流类型
     */
    private LimitType limitType;
    
    /**
     * QPS阈值
     */
    private int qpsThreshold;
    
    /**
     * 并发数阈值
     */
    private int concurrencyThreshold;
    
    /**
     * 是否启用
     */
    private boolean enabled;
    
    /**
     * 限流范围：global-全局 user-按用户 pointLine-按积分线
     */
    private String scope;
    
    /**
     * 限流类型枚举
     */
    public enum LimitType {
        /**
         * QPS限流
         */
        QPS,
        
        /**
         * 并发限流
         */
        CONCURRENCY,
        
        /**
         * 热点限流
         */
        HOT_SPOT,
        
        /**
         * 熔断降级
         */
        CIRCUIT_BREAKER
    }
    
    // ==================== 工厂方法 ====================
    
    /**
     * 创建QPS限流配置
     */
    public static RateLimitConfig qpsLimit(int qps) {
        RateLimitConfig config = new RateLimitConfig();
        config.limitType = LimitType.QPS;
        config.qpsThreshold = qps;
        config.enabled = true;
        config.scope = "global";
        return config;
    }
    
    /**
     * 创建并发限流配置
     */
    public static RateLimitConfig concurrencyLimit(int concurrency) {
        RateLimitConfig config = new RateLimitConfig();
        config.limitType = LimitType.CONCURRENCY;
        config.concurrencyThreshold = concurrency;
        config.enabled = true;
        config.scope = "global";
        return config;
    }
    
    /**
     * 创建热点限流配置
     */
    public static RateLimitConfig hotSpotLimit(int qps) {
        RateLimitConfig config = new RateLimitConfig();
        config.limitType = LimitType.HOT_SPOT;
        config.qpsThreshold = qps;
        config.enabled = true;
        config.scope = "pointLine";
        return config;
    }
    
    // ==================== Getter/Setter ====================
    
    public LimitType getLimitType() {
        return limitType;
    }
    
    public void setLimitType(LimitType limitType) {
        this.limitType = limitType;
    }
    
    public int getQpsThreshold() {
        return qpsThreshold;
    }
    
    public void setQpsThreshold(int qpsThreshold) {
        this.qpsThreshold = qpsThreshold;
    }
    
    public int getConcurrencyThreshold() {
        return concurrencyThreshold;
    }
    
    public void setConcurrencyThreshold(int concurrencyThreshold) {
        this.concurrencyThreshold = concurrencyThreshold;
    }
    
    public boolean isEnabled() {
        return enabled;
    }
    
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
    
    public String getScope() {
        return scope;
    }
    
    public void setScope(String scope) {
        this.scope = scope;
    }
}
