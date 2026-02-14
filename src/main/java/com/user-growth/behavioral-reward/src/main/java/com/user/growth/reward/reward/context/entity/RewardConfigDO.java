package com.user.growth.reward.reward.context.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * 奖励配置数据对象
 *
 * 对应数据库表 reward_config
 *
 * @author system
 * @since 1.0.0
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("reward_config")
public class RewardConfigDO extends com.user.growth.reward.common.BaseEntity {

    /**
     * 主键ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 奖励名称
     */
    private String rewardName;

    /**
     * 奖励类型 1:学习资料 2:课程优惠券 3:虚拟勋章 4:实物奖品
     */
    private Integer rewardType;

    /**
     * 兑换所需积分
     */
    private Integer pointCost;

    /**
     * 库存数量
     */
    private Integer stock;

    /**
     * 每人限兑数量
     */
    private Integer userLimit;

    /**
     * 状态 0:下架 1:上架
     */
    private Integer status;

    /**
     * 图片URL
     */
    private String imageUrl;

    /**
     * 奖励描述
     */
    private String description;
}
