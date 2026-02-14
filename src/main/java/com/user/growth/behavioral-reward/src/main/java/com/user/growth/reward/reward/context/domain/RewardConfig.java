package com.user.growth.reward.reward.context.domain;

import com.user.growth.reward.reward.context.enums.RewardType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 奖励配置领域模型
 *
 * 描述可兑换的奖励信息
 *
 * @author system
 * @since 1.0.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RewardConfig implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 奖励ID（对应数据库主键）
     */
    private Long id;

    /**
     * 奖励名称
     */
    private String rewardName;

    /**
     * 奖励类型
     */
    private RewardType rewardType;

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

    /**
     * 创建时间
     */
    private LocalDateTime createdAt;

    /**
     * 更新时间
     */
    private LocalDateTime updatedAt;

    /**
     * 判断奖励是否上架
     */
    public boolean isOnShelf() {
        return this.status != null && this.status == 1;
    }

    /**
     * 判断是否有库存
     */
    public boolean hasStock() {
        return this.stock != null && this.stock > 0;
    }

    /**
     * 减少库存
     */
    public void decreaseStock(Integer count) {
        if (this.stock == null) {
            this.stock = 0;
        }
        this.stock = Math.max(0, this.stock - count);
    }
}
