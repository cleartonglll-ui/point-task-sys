package com.vivo.pointtask.iqiyi.entity;

import java.time.LocalDateTime;

/**
 * 【爱奇艺亮点】用户积分账户实体 - 统一存储模型
 * 
 * 参考来源：爱奇艺积分系统架构演进
 * 文章链接：https://mp.weixin.qq.com/s/vvSLW4EnsYEyHYIbGl8q2Q
 * 
 * 核心设计：
 * 将原本分离存储的MySQL总值 + MongoDB明细，统一为MongoDB统一存储模型。
 * 一个用户在一个积分线下有一个独立的积分账户。
 * 
 * 存储演进：
 * - 原有架构：MySQL存储总值 + MongoDB存储明细（分离式架构）
 * - 问题：一致性问题、维护成本高、性能瓶颈
 * - 新架构：MongoDB 7.0统一存储总值+明细
 * - 优势：简化架构、提升性能、保证一致性
 * 
 * 字段设计：
 * - 总值字段：支持快速查询用户当前积分余额
 * - 扩展字段：存储请求参数，用于后续验证
 * - 版本号：乐观锁，防止并发更新问题
 * 
 * @author 参考：爱奇艺技术产品团队
 * @version 4.0.0
 */
public class UserPointAccount {
    
    /**
     * 账户ID
     */
    private Long id;
    
    /**
     * 用户ID
     */
    private Long userId;
    
    /**
     * 积分线ID
     */
    private Long pointLineId;
    
    /**
     * 积分线编码（冗余存储，方便查询）
     */
    private String pointLineCode;
    
    /**
     * 【核心】积分总值
     * 演进说明：统一存储后，总值和明细在同一数据库，保证一致性
     */
    private Long totalPoints;
    
    /**
     * 冻结积分（如提现申请中的积分）
     */
    private Long frozenPoints;
    
    /**
     * 可用积分 = totalPoints - frozenPoints
     */
    private Long availablePoints;
    
    /**
     * 累计获得积分（历史总计）
     */
    private Long cumulativeEarned;
    
    /**
     * 累计消耗积分（历史总计）
     */
    private Long cumulativeConsumed;
    
    /**
     * 【爱奇艺亮点】扩展信息（JSON格式）
     * 演进说明：存储线上用户积分请求参数，用于后续逻辑验证
     * 示例：{"source": "task_complete", "taskId": 123, "clientVersion": "11.2.0"}
     */
    private String extInfo;
    
    /**
     * 【爱奇艺亮点】版本号（乐观锁）
     * 演进说明：防止并发更新导致的数据不一致
     */
    private Integer version;
    
    /**
     * 账户状态：0-冻结 1-正常
     */
    private Integer status;
    
    /**
     * 最后更新时间
     */
    private LocalDateTime lastUpdateTime;
    
    /**
     * 创建时间
     */
    private LocalDateTime createTime;
    
    // ==================== 充血模型：积分账户业务行为 ====================
    
    /**
     * 【爱奇艺亮点】检查账户是否有效
     * 
     * @return 是否有效
     */
    public boolean isValid() {
        return status != null && status == 1;
    }
    
    /**
     * 【爱奇艺亮点】检查积分是否充足
     * 
     * @param points 需要消耗的积分
     * @return 是否充足
     */
    public boolean hasEnoughPoints(long points) {
        return getAvailablePoints() >= points;
    }
    
    /**
     * 【爱奇艺亮点】增加积分
     * 
     * @param points 增加的积分
     * @return 增加后的总值
     */
    public long addPoints(long points) {
        if (points <= 0) {
            throw new IllegalArgumentException("增加的积分必须大于0");
        }
        this.totalPoints = (this.totalPoints == null ? 0 : this.totalPoints) + points;
        this.cumulativeEarned = (this.cumulativeEarned == null ? 0 : this.cumulativeEarned) + points;
        recalculateAvailablePoints();
        this.lastUpdateTime = LocalDateTime.now();
        this.version = (this.version == null ? 1 : this.version) + 1;
        return this.totalPoints;
    }
    
    /**
     * 【爱奇艺亮点】扣除积分
     * 
     * @param points 扣除的积分
     * @return 扣除后的总值
     */
    public long deductPoints(long points) {
        if (points <= 0) {
            throw new IllegalArgumentException("扣除的积分必须大于0");
        }
        if (!hasEnoughPoints(points)) {
            throw new IllegalStateException("积分不足，当前可用积分：" + getAvailablePoints());
        }
        this.totalPoints = this.totalPoints - points;
        this.cumulativeConsumed = (this.cumulativeConsumed == null ? 0 : this.cumulativeConsumed) + points;
        recalculateAvailablePoints();
        this.lastUpdateTime = LocalDateTime.now();
        this.version = (this.version == null ? 1 : this.version) + 1;
        return this.totalPoints;
    }
    
    /**
     * 【爱奇艺亮点】冻结积分
     * 
     * @param points 冻结的积分
     * @return 冻结后的冻结积分总额
     */
    public long freezePoints(long points) {
        if (points <= 0) {
            throw new IllegalArgumentException("冻结的积分必须大于0");
        }
        if (!hasEnoughPoints(points)) {
            throw new IllegalStateException("积分不足，无法冻结");
        }
        this.frozenPoints = (this.frozenPoints == null ? 0 : this.frozenPoints) + points;
        recalculateAvailablePoints();
        this.lastUpdateTime = LocalDateTime.now();
        this.version = (this.version == null ? 1 : this.version) + 1;
        return this.frozenPoints;
    }
    
    /**
     * 【爱奇艺亮点】解冻积分
     * 
     * @param points 解冻的积分
     * @return 解冻后的冻结积分总额
     */
    public long unfreezePoints(long points) {
        if (points <= 0) {
            throw new IllegalArgumentException("解冻的积分必须大于0");
        }
        if (this.frozenPoints == null || this.frozenPoints < points) {
            throw new IllegalStateException("冻结积分不足，当前冻结积分：" + this.frozenPoints);
        }
        this.frozenPoints = this.frozenPoints - points;
        recalculateAvailablePoints();
        this.lastUpdateTime = LocalDateTime.now();
        this.version = (this.version == null ? 1 : this.version) + 1;
        return this.frozenPoints;
    }
    
    /**
     * 【爱奇艺亮点】确认扣除冻结的积分（如提现完成）
     * 
     * @param points 确认的积分
     * @return 扣除后的总值
     */
    public long confirmDeductFrozenPoints(long points) {
        if (points <= 0) {
            throw new IllegalArgumentException("确认的积分必须大于0");
        }
        if (this.frozenPoints == null || this.frozenPoints < points) {
            throw new IllegalStateException("冻结积分不足");
        }
        this.frozenPoints = this.frozenPoints - points;
        this.totalPoints = this.totalPoints - points;
        this.cumulativeConsumed = (this.cumulativeConsumed == null ? 0 : this.cumulativeConsumed) + points;
        recalculateAvailablePoints();
        this.lastUpdateTime = LocalDateTime.now();
        this.version = (this.version == null ? 1 : this.version) + 1;
        return this.totalPoints;
    }
    
    /**
     * 【内部方法】重新计算可用积分
     */
    private void recalculateAvailablePoints() {
        long total = this.totalPoints == null ? 0 : this.totalPoints;
        long frozen = this.frozenPoints == null ? 0 : this.frozenPoints;
        this.availablePoints = total - frozen;
    }
    
    /**
     * 【爱奇艺亮点】获取可用积分
     * 
     * @return 可用积分
     */
    public long getAvailablePoints() {
        recalculateAvailablePoints();
        return this.availablePoints == null ? 0 : this.availablePoints;
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
    
    public Long getPointLineId() {
        return pointLineId;
    }
    
    public void setPointLineId(Long pointLineId) {
        this.pointLineId = pointLineId;
    }
    
    public String getPointLineCode() {
        return pointLineCode;
    }
    
    public void setPointLineCode(String pointLineCode) {
        this.pointLineCode = pointLineCode;
    }
    
    public Long getTotalPoints() {
        return totalPoints;
    }
    
    public void setTotalPoints(Long totalPoints) {
        this.totalPoints = totalPoints;
    }
    
    public Long getFrozenPoints() {
        return frozenPoints;
    }
    
    public void setFrozenPoints(Long frozenPoints) {
        this.frozenPoints = frozenPoints;
    }
    
    public Long getCumulativeEarned() {
        return cumulativeEarned;
    }
    
    public void setCumulativeEarned(Long cumulativeEarned) {
        this.cumulativeEarned = cumulativeEarned;
    }
    
    public Long getCumulativeConsumed() {
        return cumulativeConsumed;
    }
    
    public void setCumulativeConsumed(Long cumulativeConsumed) {
        this.cumulativeConsumed = cumulativeConsumed;
    }
    
    public String getExtInfo() {
        return extInfo;
    }
    
    public void setExtInfo(String extInfo) {
        this.extInfo = extInfo;
    }
    
    public Integer getVersion() {
        return version;
    }
    
    public void setVersion(Integer version) {
        this.version = version;
    }
    
    public Integer getStatus() {
        return status;
    }
    
    public void setStatus(Integer status) {
        this.status = status;
    }
    
    public LocalDateTime getLastUpdateTime() {
        return lastUpdateTime;
    }
    
    public void setLastUpdateTime(LocalDateTime lastUpdateTime) {
        this.lastUpdateTime = lastUpdateTime;
    }
    
    public LocalDateTime getCreateTime() {
        return createTime;
    }
    
    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }
}
