package com.user.growth.reward.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * 应用配置类
 *
 * 从 application.yml 中读取应用配置
 *
 * @author system
 * @since 1.0.0
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "app")
public class AppConfig {

    /**
     * 限流配置
     */
    private RateLimit rateLimit = new RateLimit();

    /**
     * 任务配置
     */
    private Task task = new Task();

    /**
     * 重试配置
     */
    private Retry retry = new Retry();

    @Data
    public static class RateLimit {
        /**
         * 事件采集限流 QPS
         */
        private Integer eventCollect = 1000;

        /**
         * 积分发放限流 QPS
         */
        private Integer pointAward = 500;

        /**
         * 奖励兑换限流 QPS
         */
        private Integer rewardClaim = 200;
    }

    @Data
    public static class Task {
        /**
         * 积分有效期(天)
         */
        private Integer pointExpireDays = 365;

        /**
         * 每日积分上限
         */
        private Integer dailyPointLimit = 1000;

        /**
         * 每日任务执行次数限制
         */
        private Integer dailyTaskLimit = 100;
    }

    @Data
    public static class Retry {
        /**
         * 最大重试次数
         */
        private Integer maxAttempts = 3;

        /**
         * 初始重试间隔(毫秒)
         */
        private Integer initialInterval = 1000;

        /**
         * 重试间隔倍数
         */
        private Double multiplier = 2.0;
    }
}
