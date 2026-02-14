package com.vivo.pointtask.meituan.entity;

import java.time.LocalDateTime;

/**
 * 【美团亮点】积分流水表（Point_Flow_Log）- 三表分离架构之流水表
 * 
 * 参考来源：美团面试文章《用户积分系统怎么设计》
 * 文章链接：https://mp.weixin.qq.com/s/vvSLW4EnsYEyHYIbGl8q2Q
 * 
 * 核心设计：
 * 三表分离架构中的"流水表"，相当于"银行流水"，记录每一笔增减操作。
 * 
 * 设计原则：
 * 1. 不可修改：流水记录一旦生成，不允许修改（审计要求）
 * 2. 完整追溯：记录每一笔变动的金额、类型、关联业务单号
 * 3. 时序存储：按时间顺序记录，支持按时间范围查询
 * 
 * 与明细表的区别：
 * - 流水表：记录每一次变动操作（+100、-20），用于审计和对账
 * - 明细表：记录每一笔入账积分的余额和过期时间，用于核算
 * 
 * 业务价值：
 * - 审计追溯：完整的积分变动历史
 * - 对账依据：与总额表、明细表进行三角对账
 * - 数据分析：支持积分来源分析、消费行为分析
 * 
 * @author 参考：Fox爱分享
 * @version 5.0.0
 */
public class PointFlowLog {
    
    /**
     * 流水ID（主键）
     */
    private Long flowId;
    
    /**
     * 用户ID
     */
    private Long userId;
    
    /**
     * 【核心】变动金额
     * 正值表示增加（如+100），负值表示减少（如-20）
     */
    private Integer amount;
    
    /**
     * 【核心】变动类型
     * 1-签到 2-购物 3-兑换 4-过期扣除 5-退款返还
     */
    private Integer type;
    
    /**
     * 关联业务单号
     * 如订单号、任务ID等，用于追溯业务来源
     */
    private String refId;
    
    /**
     * 积分线ID（支持多积分线）
     */
    private Long pointLineId;
    
    /**
     * 变动前余额
     */
    private Long balanceBefore;
    
    /**
     * 变动后余额
     */
    private Long balanceAfter;
    
    /**
     * 扩展信息（JSON格式）
     * 存储额外信息，如任务详情、订单信息等
     */
    private String extInfo;
    
    /**
     * 创建时间
     */
    private LocalDateTime createTime;
    
    // ==================== 常量定义 ====================
    
    /**
     * 变动类型：签到
     */
    public static final int TYPE_SIGN_IN = 1;
    
    /**
     * 变动类型：购物
     */
    public static final int TYPE_SHOPPING = 2;
    
    /**
     * 变动类型：兑换
     */
    public static final int TYPE_EXCHANGE = 3;
    
    /**
     * 变动类型：过期扣除
     */
    public static final int TYPE_EXPIRE = 4;
    
    /**
     * 变动类型：退款返还
     */
    public static final int TYPE_REFUND = 5;
    
    /**
     * 变动类型：任务完成
     */
    public static final int TYPE_TASK_COMPLETE = 6;
    
    // ==================== 充血模型：流水业务行为 ====================
    
    /**
     * 【美团亮点】判断是否为增加积分
     * 
     * @return 是否为增加
     */
    public boolean isIncrease() {
        return amount != null && amount > 0;
    }
    
    /**
     * 【美团亮点】判断是否为减少积分
     * 
     * @return 是否为减少
     */
    public boolean isDecrease() {
        return amount != null && amount < 0;
    }
    
    /**
     * 【美团亮点】获取变动类型描述
     * 
     * @return 类型描述
     */
    public String getTypeDescription() {
        if (type == null) {
            return "未知";
        }
        return switch (type) {
            case TYPE_SIGN_IN -> "签到";
            case TYPE_SHOPPING -> "购物";
            case TYPE_EXCHANGE -> "兑换";
            case TYPE_EXPIRE -> "过期扣除";
            case TYPE_REFUND -> "退款返还";
            case TYPE_TASK_COMPLETE -> "任务完成";
            default -> "其他";
        };
    }
    
    /**
     * 【美团亮点】验证流水合法性
     * 
     * @return 是否合法
     */
    public boolean isValid() {
        if (amount == null || amount == 0) {
            return false;
        }
        if (userId == null) {
            return false;
        }
        // 验证余额变化是否正确
        if (balanceBefore != null && balanceAfter != null) {
            long expected = balanceBefore + amount;
            if (expected != balanceAfter) {
                return false;
            }
        }
        return true;
    }
    
    // ==================== 建造者模式 ====================
    
    public static Builder builder() {
        return new Builder();
    }
    
    public static class Builder {
        private PointFlowLog log = new PointFlowLog();
        
        public Builder flowId(Long flowId) {
            log.flowId = flowId;
            return this;
        }
        
        public Builder userId(Long userId) {
            log.userId = userId;
            return this;
        }
        
        public Builder amount(Integer amount) {
            log.amount = amount;
            return this;
        }
        
        public Builder type(Integer type) {
            log.type = type;
            return this;
        }
        
        public Builder refId(String refId) {
            log.refId = refId;
            return this;
        }
        
        public Builder pointLineId(Long pointLineId) {
            log.pointLineId = pointLineId;
            return this;
        }
        
        public Builder balanceBefore(Long balanceBefore) {
            log.balanceBefore = balanceBefore;
            return this;
        }
        
        public Builder balanceAfter(Long balanceAfter) {
            log.balanceAfter = balanceAfter;
            return this;
        }
        
        public Builder extInfo(String extInfo) {
            log.extInfo = extInfo;
            return this;
        }
        
        public Builder createTime(LocalDateTime createTime) {
            log.createTime = createTime;
            return this;
        }
        
        public PointFlowLog build() {
            if (log.createTime == null) {
                log.createTime = LocalDateTime.now();
            }
            return log;
        }
    }
    
    // ==================== Getter/Setter ====================
    
    public Long getFlowId() {
        return flowId;
    }
    
    public void setFlowId(Long flowId) {
        this.flowId = flowId;
    }
    
    public Long getUserId() {
        return userId;
    }
    
    public void setUserId(Long userId) {
        this.userId = userId;
    }
    
    public Integer getAmount() {
        return amount;
    }
    
    public void setAmount(Integer amount) {
        this.amount = amount;
    }
    
    public Integer getType() {
        return type;
    }
    
    public void setType(Integer type) {
        this.type = type;
    }
    
    public String getRefId() {
        return refId;
    }
    
    public void setRefId(String refId) {
        this.refId = refId;
    }
    
    public Long getPointLineId() {
        return pointLineId;
    }
    
    public void setPointLineId(Long pointLineId) {
        this.pointLineId = pointLineId;
    }
    
    public Long getBalanceBefore() {
        return balanceBefore;
    }
    
    public void setBalanceBefore(Long balanceBefore) {
        this.balanceBefore = balanceBefore;
    }
    
    public Long getBalanceAfter() {
        return balanceAfter;
    }
    
    public void setBalanceAfter(Long balanceAfter) {
        this.balanceAfter = balanceAfter;
    }
    
    public String getExtInfo() {
        return extInfo;
    }
    
    public void setExtInfo(String extInfo) {
        this.extInfo = extInfo;
    }
    
    public LocalDateTime getCreateTime() {
        return createTime;
    }
    
    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }
}
