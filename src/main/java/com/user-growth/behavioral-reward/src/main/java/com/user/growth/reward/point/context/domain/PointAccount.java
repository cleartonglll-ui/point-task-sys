package com.user.growth.reward.point.context.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 积分账户领域模型
 *
 * 描述用户的积分账户信息
 *
 * @author system
 * @since 1.0.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PointAccount implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 账户ID（对应数据库主键）
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
     * 总积分
     */
    private Integer totalPoints;

    /**
     * 可用积分
     */
    private Integer availablePoints;

    /**
     * 冻结积分
     */
    private Integer frozenPoints;

    /**
     * 累计获得积分
     */
    private Integer totalEarned;

    /**
     * 累计消费积分
     */
    private Integer totalSpent;

    /**
     * 会员等级
     */
    private Integer level;

    /**
     * 最后登录时间
     */
    private LocalDateTime lastLoginTime;

    /**
     * 创建时间
     */
    private LocalDateTime createdAt;

    /**
     * 更新时间
     */
    private LocalDateTime updatedAt;

    /**
     * 判断积分余额是否足够
     *
     * @param amount 所需积分数量
     * @return 是否足够
     */
    public boolean isBalanceEnough(Integer amount) {
        return this.availablePoints != null && this.availablePoints >= amount;
    }

    /**
     * 增加积分
     *
     * @param amount 积分数量
     */
    public void addPoints(Integer amount) {
        if (amount == null || amount <= 0) {
            return;
        }
        if (this.totalPoints == null) {
            this.totalPoints = 0;
        }
        if (this.availablePoints == null) {
            this.availablePoints = 0;
        }
        if (this.totalEarned == null) {
            this.totalEarned = 0;
        }

        this.totalPoints += amount;
        this.availablePoints += amount;
        this.totalEarned += amount;
        this.updatedAt = LocalDateTime.now();
    }

    /**
     * 扣减积分
     *
     * @param amount 积分数量
     */
    public void deductPoints(Integer amount) {
        if (amount == null || amount <= 0) {
            return;
        }
        if (this.totalPoints == null) {
            this.totalPoints = 0;
        }
        if (this.availablePoints == null) {
            this.availablePoints = 0;
        }
        if (this.totalSpent == null) {
            this.totalSpent = 0;
        }

        this.totalPoints -= amount;
        this.availablePoints -= amount;
        this.totalSpent += amount;
        this.updatedAt = LocalDateTime.now();
    }
}
