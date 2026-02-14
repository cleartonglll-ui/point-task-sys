package com.user.growth.reward.config;

import com.alibaba.csp.sentinel.annotation.aspectj.SentinelResourceAspect;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Sentinel 配置类
 *
 * 配置 Sentinel 注解支持，实现限流、熔断、降级功能
 *
 * @author system
 * @since 1.0.0
 */
@Configuration
public class SentinelConfig {

    /**
     * 注册 SentinelResourceAspect，支持 @SentinelResource 注解
     */
    @Bean
    public SentinelResourceAspect sentinelResourceAspect() {
        return new SentinelResourceAspect();
    }
}
