package com.vivo.pointtask.meituan.statemachine;

import java.time.LocalDateTime;

/**
 * 【状态机】积分状态机核心接口
 * 
 * 参考：适合几十万用户量的积分系统设计
 * 
 * 设计说明：
 * 使用状态机规范积分的生命周期流转，确保状态转换的合法性和可追溯性。
 * 
 * 核心功能：
 * 1. 状态流转控制：定义合法的状态转换路径
 * 2. 状态变更日志：记录每次状态变更的上下文
 * 3. 持久化支持：支持服务重启后恢复状态
 * 
 * 与PointDetailBucket的关系：
 * - PointDetailBucket存储积分的金额、过期时间等业务属性
 * - PointStatusMachine管理积分的生命周期状态
 * - 两者配合实现完整的积分管理
 * 
 * @version 5.0.0
 */
public interface PointStatusMachine {
    
    /**
     * 【状态流转】创建积分 → 可用
     * 
     * 场景：积分发放成功后，从CREATED转为AVAILABLE
     * 
     * @param bucketId 积分分桶ID
     * @param operator 操作人
     * @param reason 原因
     * @return 是否成功
     */
    boolean activate(Long bucketId, String operator, String reason);
    
    /**
     * 【状态流转】可用 → 冻结
     * 
     * 场景：订单未确认收货，暂时冻结积分
     * 
     * @param bucketId 积分分桶ID
     * @param operator 操作人
     * @param reason 原因（如"订单未确认收货"）
     * @return 是否成功
     */
    boolean freeze(Long bucketId, String operator, String reason);
    
    /**
     * 【状态流转】冻结 → 可用
     * 
     * 场景：订单确认收货，解冻积分
     * 
     * @param bucketId 积分分桶ID
     * @param operator 操作人
     * @param reason 原因
     * @return 是否成功
     */
    boolean unfreeze(Long bucketId, String operator, String reason);
    
    /**
     * 【状态流转】可用/冻结 → 已消费
     * 
     * 场景：用户使用积分消费
     * 
     * @param bucketId 积分分桶ID
     * @param consumeAmount 消费金额
     * @param operator 操作人
     * @param reason 原因（如"兑换商品"）
     * @return 是否成功
     */
    boolean consume(Long bucketId, int consumeAmount, String operator, String reason);
    
    /**
     * 【状态流转】可用/冻结 → 过期
     * 
     * 场景：积分到达过期时间
     * 
     * @param bucketId 积分分桶ID
     * @param operator 操作人
     * @param reason 原因
     * @return 是否成功
     */
    boolean expire(Long bucketId, String operator, String reason);
    
    /**
     * 【查询】获取当前状态
     * 
     * @param bucketId 积分分桶ID
     * @return 当前状态
     */
    PointStatus getCurrentStatus(Long bucketId);
    
    /**
     * 【查询】检查是否可以转换到目标状态
     * 
     * @param bucketId 积分分桶ID
     * @param targetStatus 目标状态
     * @return 是否可以转换
     */
    boolean canTransition(Long bucketId, PointStatus targetStatus);
    
    /**
     * 【查询】获取状态变更历史
     * 
     * @param bucketId 积分分桶ID
     * @return 状态变更日志列表
     */
    java.util.List<StatusChangeLog> getStatusHistory(Long bucketId);
}

/**
 * 状态变更日志
 */
class StatusChangeLog {
    private Long id;
    private Long bucketId;
    private PointStatus fromStatus;
    private PointStatus toStatus;
    private String operator;
    private String reason;
    private LocalDateTime changeTime;
    private String extInfo; // 扩展信息，如订单号、业务单号等
    
    // 构造器和Getter/Setter
    public StatusChangeLog() {}
    
    public StatusChangeLog(Long bucketId, PointStatus fromStatus, PointStatus toStatus,
                          String operator, String reason) {
        this.bucketId = bucketId;
        this.fromStatus = fromStatus;
        this.toStatus = toStatus;
        this.operator = operator;
        this.reason = reason;
        this.changeTime = LocalDateTime.now();
    }
    
    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public Long getBucketId() { return bucketId; }
    public void setBucketId(Long bucketId) { this.bucketId = bucketId; }
    
    public PointStatus getFromStatus() { return fromStatus; }
    public void setFromStatus(PointStatus fromStatus) { this.fromStatus = fromStatus; }
    
    public PointStatus getToStatus() { return toStatus; }
    public void setToStatus(PointStatus toStatus) { this.toStatus = toStatus; }
    
    public String getOperator() { return operator; }
    public void setOperator(String operator) { this.operator = operator; }
    
    public String getReason() { return reason; }
    public void setReason(String reason) { this.reason = reason; }
    
    public LocalDateTime getChangeTime() { return changeTime; }
    public void setChangeTime(LocalDateTime changeTime) { this.changeTime = changeTime; }
    
    public String getExtInfo() { return extInfo; }
    public void setExtInfo(String extInfo) { this.extInfo = extInfo; }
}
