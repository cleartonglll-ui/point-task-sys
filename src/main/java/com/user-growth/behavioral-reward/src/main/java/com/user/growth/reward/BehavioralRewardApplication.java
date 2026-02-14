package com.user.growth.reward;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * 行为积分奖励系统启动类
 *
 * 系统架构说明：
 * 1. 四层逻辑架构：
 *    - 任务配置层：管理任务规则、奖励配置
 *    - 精细化运营层：基于用户标签的任务推荐
 *    - 采集计算层：事件采集、清洗、计算
 *    - 奖励触达层：积分发放、奖励兑换
 *
 * 2. 物理服务拆分：
 *    - EventCollectionService: 事件采集服务
 *    - EventCalculationService: 事件计算服务
 *    - TaskConfigService: 任务配置服务
 *    - RewardDeliveryService: 奖励触达服务
 *
 * 3. DDD 限界上下文：
 *    - BehaviorEventContext: 行为事件上下文
 *    - PointAccountContext: 积分账户上下文
 *    - TaskRuleContext: 任务规则上下文
 *    - RewardDeliveryContext: 奖励发放上下文
 *
 * @author system
 * @since 1.0.0
 */
@SpringBootApplication
@EnableAsync
@EnableScheduling
public class BehavioralRewardApplication {

    public static void main(String[] args) {
        SpringApplication.run(BehavioralRewardApplication.class, args);
    }
}
