package com.user.growth.calculation.domain.aggregate;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 奖励计算聚合根
 * 领域层：处理积分计算逻辑，管理奖励计算的完整生命周期
 * 
 * 该聚合根负责处理用户行为触发的奖励计算逻辑，包括积分计算、
 * 奖励状态管理、防重复发放等核心业务逻辑，确保奖励发放的准确性和一致性。
 */
@Data
public class RewardCalculation {
    private Long id;              // 奖励计算记录ID
    private String eventId;       // 关联的事件ID
    private Long userId;          // 用户ID
    private String eventType;     // 事件类型
    private Long taskId;          // 任务ID
    private Integer rewardAmount; // 奖励数量
    private String rewardType;    // 奖励类型
    private Integer status;       // 处理状态：0-待计算, 1-计算完成, 2-已发放
    private LocalDateTime createTime; // 创建时间
    private LocalDateTime updateTime; // 更新时间

    /**
     * 领域方法：执行奖励计算
     * 根据事件和任务规则执行奖励计算逻辑，可能集成Aviator表达式引擎进行复杂计算
     */
    public void calculateReward() {
        // 这里可以集成Aviator表达式引擎进行复杂计算
        this.status = 1;
        this.updateTime = LocalDateTime.now();
    }

    /**
     * 领域方法：标记为已发放
     * 更新奖励状态为已发放，表示奖励已成功发放给用户
     */
    public void markAsDelivered() {
        this.status = 2;
        this.updateTime = LocalDateTime.now();
    }
}