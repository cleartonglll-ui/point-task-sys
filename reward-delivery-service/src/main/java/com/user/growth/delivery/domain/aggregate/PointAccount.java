package com.user.growth.delivery.domain.aggregate;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 积分账户聚合根
 * 领域层：管理用户积分资产，确保积分操作的准确性与安全性
 * 
 * 该聚合根负责管理用户的积分资产，包括积分的增加、扣减、冻结等核心操作，
 * 确保积分账户数据的完整性和一致性，防止积分超发、负数等异常情况。
 */
@Data
public class PointAccount {
    private Long id;              // 账户ID
    private Long userId;          // 用户ID
    private Long totalPoints;     // 总积分
    private Long availablePoints; // 可用积分
    private Long frozenPoints;    // 冻结积分
    private Integer status;       // 账户状态：0-正常, 1-冻结
    private LocalDateTime createTime; // 创建时间
    private LocalDateTime updateTime; // 更新时间

    /**
     * 领域方法：增加积分
     * 为用户账户增加指定数量的积分，同时更新总积分和可用积分
     * 
     * @param amount 要增加的积分数量
     * @throws IllegalArgumentException 如果积分数量小于等于0
     */
    public void addPoints(Long amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("积分数量必须大于0");
        }
        this.totalPoints += amount;
        this.availablePoints += amount;
        this.updateTime = LocalDateTime.now();
    }

    /**
     * 领域方法：扣减积分
     * 从用户账户的可用积分中扣除指定数量的积分
     * 
     * @param amount 要扣减的积分数量
     * @throws IllegalArgumentException 如果积分数量小于等于0
     * @throws IllegalStateException 如果可用积分不足
     */
    public void deductPoints(Long amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("积分数量必须大于0");
        }
        if (this.availablePoints < amount) {
            throw new IllegalStateException("积分不足");
        }
        this.availablePoints -= amount;
        this.updateTime = LocalDateTime.now();
    }

    /**
     * 领域方法：冻结积分
     * 将用户账户中的部分可用积分转为冻结状态
     * 
     * @param amount 要冻结的积分数量
     * @throws IllegalArgumentException 如果积分数量小于等于0
     * @throws IllegalStateException 如果可用积分不足
     */
    public void freezePoints(Long amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("积分数量必须大于0");
        }
        if (this.availablePoints < amount) {
            throw new IllegalStateException("可用积分不足");
        }
        this.availablePoints -= amount;
        this.frozenPoints += amount;
        this.updateTime = LocalDateTime.now();
    }
}