package com.user.growth.reward.reward.context.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * 奖励兑换记录数据对象
 *
 * 对应数据库表 reward_claim_record
 *
 * @author system
 * @since 1.0.0
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("reward_claim_record")
public class RewardClaimRecordDO extends com.user.growth.reward.common.BaseEntity {

    /**
     * 主键ID
     */
    @TableId(type = IdType.AUTO)
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
}
