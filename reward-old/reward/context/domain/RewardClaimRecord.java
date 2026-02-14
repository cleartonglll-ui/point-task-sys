package com.user.growth.reward.reward.context.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 奖励兑换记录领域模型
 *
 * 记录用户兑换奖励的信息
 *
 * @author system
 * @since 1.0.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RewardClaimRecord implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 记录ID（对应数据库主键）
     */
    private Long id;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 用户昵称
     */
    private String userName;

    /**
     * 奖励ID
     */
    private Long rewardId;

    /**
     * 奖励名称
     */
    private String rewardName;

    /**
     * 消耗积分
     */
    private Integer pointCost;

    /**
     * 兑换状态 1:待发货 2:已发货 3:已完成
     */
    private Integer claimStatus;

    /**
     * 发货时间
     */
    private LocalDateTime deliveryTime;

    /**
     * 收货地址
     */
    private String address;

    /**
     * 备注
     */
    private String remark;

    /**
     * 创建时间
     */
    private LocalDateTime createdAt;

    /**
     * 更新时间
     */
    private LocalDateTime updatedAt;

    /**
     * 判断是否已发货
     */
    public boolean isDelivered() {
        return this.claimStatus != null && this.claimStatus >= 2;
    }

    /**
     * 判断是否已完成
     */
    public boolean isCompleted() {
        return this.claimStatus != null && this.claimStatus == 3;
    }

    /**
     * 更新为已发货
     */
    public void markAsDelivered() {
        this.claimStatus = 2;
        this.deliveryTime = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    /**
     * 更新为已完成
     */
    public void markAsCompleted() {
        this.claimStatus = 3;
        this.updatedAt = LocalDateTime.now();
    }
}
