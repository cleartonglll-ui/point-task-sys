package com.vivo.pointtask.meituan.entity;

import java.time.LocalDateTime;

/**
 * 【美团亮点】积分明细/分桶表（Point_Detail_Bucket）- 三表分离架构之核算核心
 * 
 * 参考来源：美团面试文章《用户积分系统怎么设计》
 * 文章链接：https://mp.weixin.qq.com/s/vvSLW4EnsYEyHYIbGl8q2Q
 * 
 * 核心设计：
 * 三表分离架构中的"明细表"，记录每一笔入账积分的余额和过期时间，是核算核心。
 * 
 * 设计原则：
 * 1. 分桶存储：每一笔入账作为一个独立的Bucket
 * 2. 过期时间：记录每笔积分的过期时间，支持FIFO扣减
 * 3. 余额追踪：记录当前剩余金额，支持部分消费
 * 
 * 关键索引：
 * - idx_user_expire (user_id, expire_time)：支持按过期时间排序查询，用于FIFO扣减
 * 
 * FIFO扣减逻辑：
 * 1. 按expire_time ASC排序查询用户的Bucket
 * 2. 优先扣除快过期的积分
 * 3. 一个Bucket扣完再扣下一个
 * 
 * 与流水表的区别：
 * - 流水表：记录每一次变动操作，用于审计
 * - 明细表：记录每一笔入账的余额和过期时间，用于核算和消费
 * 
 * 业务价值：
 * - 有效期管理：每笔积分独立记录过期时间
 * - FIFO扣减：优先扣除快过期的积分（对用户最有利）
 * - 部分消费：支持一笔积分分多次消费
 * 
 * @author 参考：Fox爱分享
 * @version 5.0.0
 */
public class PointDetailBucket {
    
    /**
     * 明细ID（主键）
     */
    private Long id;
    
    /**
     * 用户ID
     */
    private Long userId;
    
    /**
     * 【核心】初始入账金额
     * 演化说明：记录这笔积分最初入账的金额
     */
    private Integer initialAmount;
    
    /**
     * 【核心】当前剩余金额
     * 演化说明：记录这笔积分还剩余多少可用
     */
    private Integer currentBalance;
    
    /**
     * 【核心】过期时间
     * 演化说明：决定这笔积分的生死，用于FIFO扣减
     * 关键索引：idx_user_expire (user_id, expire_time)
     */
    private LocalDateTime expireTime;
    
    /**
     * 【核心】状态
     * 0-有效 1-已用完 2-已过期
     */
    private Integer status;
    
    /**
     * 积分线ID（支持多积分线）
     */
    private Long pointLineId;
    
    /**
     * 关联流水ID
     */
    private Long flowId;
    
    /**
     * 来源类型
     * 1-签到 2-购物 3-任务 4-补偿
     */
    private Integer sourceType;
    
    /**
     * 创建时间
     */
    private LocalDateTime createTime;
    
    /**
     * 更新时间
     */
    private LocalDateTime updateTime;
    
    // ==================== 常量定义 ====================
    
    /**
     * 状态：有效
     */
    public static final int STATUS_VALID = 0;
    
    /**
     * 状态：已用完
     */
    public static final int STATUS_DEPLETED = 1;
    
    /**
     * 状态：已过期
     */
    public static final int STATUS_EXPIRED = 2;
    
    // ==================== 充血模型：分桶业务行为 ====================
    
    /**
     * 【美团亮点】检查是否有效
     * 
     * @return 是否有效
     */
    public boolean isValid() {
        return status != null && status == STATUS_VALID && currentBalance != null && currentBalance > 0;
    }
    
    /**
     * 【美团亮点】检查是否过期
     * 
     * @return 是否过期
     */
    public boolean isExpired() {
        if (expireTime == null) {
            return false;
        }
        return LocalDateTime.now().isAfter(expireTime);
    }
    
    /**
     * 【美团亮点】检查是否已用完
     * 
     * @return 是否已用完
     */
    public boolean isDepleted() {
        return currentBalance == null || currentBalance <= 0;
    }
    
    /**
     * 【美团亮点】扣除积分（FIFO扣减核心）
     * 
     * 演化说明：
     * 这是FIFO扣减的核心方法，从当前Bucket中扣除指定积分。
     * 如果当前Bucket不够扣，返回实际扣除的金额，调用方需要继续扣下一个Bucket。
     * 
     * @param points 需要扣除的积分
     * @return 实际扣除的积分
     */
    public int deduct(int points) {
        if (points <= 0) {
            return 0;
        }
        if (!isValid()) {
            return 0;
        }
        
        int deductAmount = Math.min(currentBalance, points);
        currentBalance = currentBalance - deductAmount;
        
        // 更新状态
        if (currentBalance <= 0) {
            status = STATUS_DEPLETED;
        }
        
        updateTime = LocalDateTime.now();
        return deductAmount;
    }
    
    /**
     * 【美团亮点】获取剩余可用积分
     * 
     * @return 剩余积分
     */
    public int getAvailableBalance() {
        if (!isValid()) {
            return 0;
        }
        return currentBalance;
    }
    
    /**
     * 【美团亮点】标记为已过期
     */
    public void markAsExpired() {
        this.status = STATUS_EXPIRED;
        this.updateTime = LocalDateTime.now();
    }
    
    /**
     * 【美团亮点】获取状态描述
     * 
     * @return 状态描述
     */
    public String getStatusDescription() {
        if (status == null) {
            return "未知";
        }
        return switch (status) {
            case STATUS_VALID -> "有效";
            case STATUS_DEPLETED -> "已用完";
            case STATUS_EXPIRED -> "已过期";
            default -> "未知";
        };
    }
    
    /**
     * 【美团亮点】检查是否可合并
     * 
     * 演化说明：
     * 定期合并机制使用此方法判断两个Bucket是否可以合并。
     * 可合并条件：同一用户、相同/相近过期时间、都有效
     * 
     * @param other 另一个Bucket
     * @return 是否可合并
     */
    public boolean canMergeWith(PointDetailBucket other) {
        if (other == null) {
            return false;
        }
        // 同一用户
        if (!this.userId.equals(other.userId)) {
            return false;
        }
        // 都有效
        if (!this.isValid() || !other.isValid()) {
            return false;
        }
        // 过期时间相同或相近（7天内）
        if (this.expireTime == null || other.expireTime == null) {
            return false;
        }
        long daysDiff = java.time.Duration.between(
            this.expireTime, other.expireTime
        ).toDays();
        return Math.abs(daysDiff) <= 7;
    }
    
    /**
     * 【美团亮点】合并Bucket
     * 
     * 演化说明：
     * 将另一个Bucket合并到当前Bucket中，用于定期合并机制。
     * 
     * @param other 另一个Bucket
     * @return 合并后的金额
     */
    public int merge(PointDetailBucket other) {
        if (!canMergeWith(other)) {
            throw new IllegalArgumentException("不可合并的Bucket");
        }
        this.initialAmount = this.initialAmount + other.initialAmount;
        this.currentBalance = this.currentBalance + other.currentBalance;
        // 取较晚的过期时间
        if (other.expireTime.isAfter(this.expireTime)) {
            this.expireTime = other.expireTime;
        }
        this.updateTime = LocalDateTime.now();
        return this.currentBalance;
    }
    
    // ==================== 建造者模式 ====================
    
    public static Builder builder() {
        return new Builder();
    }
    
    public static class Builder {
        private PointDetailBucket bucket = new PointDetailBucket();
        
        public Builder id(Long id) {
            bucket.id = id;
            return this;
        }
        
        public Builder userId(Long userId) {
            bucket.userId = userId;
            return this;
        }
        
        public Builder initialAmount(Integer initialAmount) {
            bucket.initialAmount = initialAmount;
            bucket.currentBalance = initialAmount;
            return this;
        }
        
        public Builder expireTime(LocalDateTime expireTime) {
            bucket.expireTime = expireTime;
            return this;
        }
        
        public Builder pointLineId(Long pointLineId) {
            bucket.pointLineId = pointLineId;
            return this;
        }
        
        public Builder flowId(Long flowId) {
            bucket.flowId = flowId;
            return this;
        }
        
        public Builder sourceType(Integer sourceType) {
            bucket.sourceType = sourceType;
            return this;
        }
        
        public PointDetailBucket build() {
            bucket.status = STATUS_VALID;
            bucket.createTime = LocalDateTime.now();
            bucket.updateTime = LocalDateTime.now();
            return bucket;
        }
    }
    
    // ==================== Getter/Setter ====================
    
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public Long getUserId() {
        return userId;
    }
    
    public void setUserId(Long userId) {
        this.userId = userId;
    }
    
    public Integer getInitialAmount() {
        return initialAmount;
    }
    
    public void setInitialAmount(Integer initialAmount) {
        this.initialAmount = initialAmount;
    }
    
    public Integer getCurrentBalance() {
        return currentBalance;
    }
    
    public void setCurrentBalance(Integer currentBalance) {
        this.currentBalance = currentBalance;
    }
    
    public LocalDateTime getExpireTime() {
        return expireTime;
    }
    
    public void setExpireTime(LocalDateTime expireTime) {
        this.expireTime = expireTime;
    }
    
    public Integer getStatus() {
        return status;
    }
    
    public void setStatus(Integer status) {
        this.status = status;
    }
    
    public Long getPointLineId() {
        return pointLineId;
    }
    
    public void setPointLineId(Long pointLineId) {
        this.pointLineId = pointLineId;
    }
    
    public Long getFlowId() {
        return flowId;
    }
    
    public void setFlowId(Long flowId) {
        this.flowId = flowId;
    }
    
    public Integer getSourceType() {
        return sourceType;
    }
    
    public void setSourceType(Integer sourceType) {
        this.sourceType = sourceType;
    }
    
    public LocalDateTime getCreateTime() {
        return createTime;
    }
    
    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }
    
    public LocalDateTime getUpdateTime() {
        return updateTime;
    }
    
    public void setUpdateTime(LocalDateTime updateTime) {
        this.updateTime = updateTime;
    }
}
