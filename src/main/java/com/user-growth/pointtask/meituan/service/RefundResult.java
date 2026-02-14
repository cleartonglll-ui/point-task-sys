package com.vivo.pointtask.meituan.service;

/**
 * 【美团亮点】积分返还结果
 * 
 * @author 参考：Fox爱分享
 * @version 5.0.0
 */
public class RefundResult {
    
    /**
     * 返还是否成功
     */
    private boolean success;
    
    /**
     * 用户ID
     */
    private Long userId;
    
    /**
     * 原扣减流水ID
     */
    private Long originalFlowId;
    
    /**
     * 请求返还积分
     */
    private int requestedRefund;
    
    /**
     * 实际返还积分
     */
    private int actualRefunded;
    
    /**
     * 返还后余额
     */
    private long balanceAfter;
    
    /**
     * 返还流水ID
     */
    private Long refundFlowId;
    
    /**
     * 返还类型
     * 1-原路退回 2-延期退回（已过期积分延期7天）
     */
    private int refundType;
    
    /**
     * 错误信息
     */
    private String errorMessage;
    
    // ==================== 常量定义 ====================
    
    /**
     * 返还类型：原路退回
     */
    public static final int REFUND_TYPE_ORIGINAL = 1;
    
    /**
     * 返还类型：延期退回
     */
    public static final int REFUND_TYPE_EXTENDED = 2;
    
    // ==================== 工厂方法 ====================
    
    public static RefundResult success(Long userId, Long originalFlowId, int requested, 
                                       int actual, long balance, Long refundFlowId, int type) {
        RefundResult result = new RefundResult();
        result.success = true;
        result.userId = userId;
        result.originalFlowId = originalFlowId;
        result.requestedRefund = requested;
        result.actualRefunded = actual;
        result.balanceAfter = balance;
        result.refundFlowId = refundFlowId;
        result.refundType = type;
        return result;
    }
    
    public static RefundResult fail(Long userId, Long originalFlowId, int requested, String errorMessage) {
        RefundResult result = new RefundResult();
        result.success = false;
        result.userId = userId;
        result.originalFlowId = originalFlowId;
        result.requestedRefund = requested;
        result.errorMessage = errorMessage;
        return result;
    }
    
    // ==================== Getter/Setter ====================
    
    public boolean isSuccess() {
        return success;
    }
    
    public void setSuccess(boolean success) {
        this.success = success;
    }
    
    public Long getUserId() {
        return userId;
    }
    
    public void setUserId(Long userId) {
        this.userId = userId;
    }
    
    public Long getOriginalFlowId() {
        return originalFlowId;
    }
    
    public void setOriginalFlowId(Long originalFlowId) {
        this.originalFlowId = originalFlowId;
    }
    
    public int getRequestedRefund() {
        return requestedRefund;
    }
    
    public void setRequestedRefund(int requestedRefund) {
        this.requestedRefund = requestedRefund;
    }
    
    public int getActualRefunded() {
        return actualRefunded;
    }
    
    public void setActualRefunded(int actualRefunded) {
        this.actualRefunded = actualRefunded;
    }
    
    public long getBalanceAfter() {
        return balanceAfter;
    }
    
    public void setBalanceAfter(long balanceAfter) {
        this.balanceAfter = balanceAfter;
    }
    
    public Long getRefundFlowId() {
        return refundFlowId;
    }
    
    public void setRefundFlowId(Long refundFlowId) {
        this.refundFlowId = refundFlowId;
    }
    
    public int getRefundType() {
        return refundType;
    }
    
    public void setRefundType(int refundType) {
        this.refundType = refundType;
    }
    
    public String getErrorMessage() {
        return errorMessage;
    }
    
    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}
