package com.vivo.pointtask.meituan.entity;

import java.time.LocalDateTime;

/**
 * 【美团亮点】积分总表（User_Point_Wallet）- 三表分离架构之总额表
 * 
 * 参考来源：美团面试文章《用户积分系统怎么设计》
 * 文章链接：https://mp.weixin.qq.com/s/vvSLW4EnsYEyHYIbGl8q2Q
 * 
 * 核心设计：
 * 三表分离架构中的"总额表"，相当于"钱包"，只存总额，用于快速读取（展示给用户看）。
 * 
 * 设计原则：
 * 1. 只存当前可用总积分（读视图）
 * 2. 使用乐观锁版本号防止并发冲突
 * 3. 不存积分明细，明细存储在PointDetailBucket中
 * 
 * 与爱奇艺方案的区别：
 * - 爱奇艺：UserPointAccount包含totalPoints、cumulativeEarned等统计字段
 * - 美团：UserPointWallet只存totalBalance当前余额，更纯粹的读视图
 * 
 * 业务价值：
 * - 读优化：用户查询积分时直接读取此表，无需计算
 * - 缓存友好：可作为Redis缓存的数据源
 * - 乐观锁：version字段防止并发更新问题
 * 
 * @author 参考：Fox爱分享
 * @version 5.0.0
 */
public class UserPointWallet {
    
    /**
     * 用户ID（主键）
     */
    private Long userId;
    
    /**
     * 【核心】当前可用总积分（读视图）
     * 演化说明：这是给用户展示的积分余额，由后台计算维护
     */
    private Long totalBalance;
    
    /**
     * 【美团亮点】乐观锁版本号
     * 演化说明：防止并发更新导致的数据不一致
     * 更新时使用：UPDATE user_point_wallet SET total_balance = ?, version = version + 1 
     *              WHERE user_id = ? AND version = ?
     */
    private Integer version;
    
    /**
     * 创建时间
     */
    private LocalDateTime createTime;
    
    /**
     * 更新时间
     */
    private LocalDateTime updateTime;
    
    // ==================== 充血模型：钱包业务行为 ====================
    
    /**
     * 【美团亮点】增加积分（带乐观锁）
     * 
     * @param points 增加的积分
     * @return 增加后的余额
     */
    public long addPoints(long points) {
        if (points <= 0) {
            throw new IllegalArgumentException("增加的积分必须大于0");
        }
        this.totalBalance = (this.totalBalance == null ? 0 : this.totalBalance) + points;
        this.version = (this.version == null ? 1 : this.version) + 1;
        this.updateTime = LocalDateTime.now();
        return this.totalBalance;
    }
    
    /**
     * 【美团亮点】扣除积分（带乐观锁）
     * 
     * @param points 扣除的积分
     * @return 扣除后的余额
     */
    public long deductPoints(long points) {
        if (points <= 0) {
            throw new IllegalArgumentException("扣除的积分必须大于0");
        }
        if (this.totalBalance == null || this.totalBalance < points) {
            throw new IllegalStateException("积分不足，当前余额：" + this.totalBalance);
        }
        this.totalBalance = this.totalBalance - points;
        this.version = (this.version == null ? 1 : this.version) + 1;
        this.updateTime = LocalDateTime.now();
        return this.totalBalance;
    }
    
    /**
     * 【美团亮点】检查积分是否充足
     * 
     * @param points 需要扣除的积分
     * @return 是否充足
     */
    public boolean hasEnoughPoints(long points) {
        return this.totalBalance != null && this.totalBalance >= points;
    }
    
    /**
     * 【美团亮点】获取乐观锁更新SQL条件
     * 
     * 演化说明：生成带版本号的更新条件，用于防止并发冲突
     * 
     * @return 当前版本号
     */
    public Integer getCurrentVersionForUpdate() {
        return this.version;
    }
    
    // ==================== Getter/Setter ====================
    
    public Long getUserId() {
        return userId;
    }
    
    public void setUserId(Long userId) {
        this.userId = userId;
    }
    
    public Long getTotalBalance() {
        return totalBalance;
    }
    
    public void setTotalBalance(Long totalBalance) {
        this.totalBalance = totalBalance;
    }
    
    public Integer getVersion() {
        return version;
    }
    
    public void setVersion(Integer version) {
        this.version = version;
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
