package com.vivo.pointtask.meituan.service;

import java.util.List;

/**
 * 【美团亮点】积分扣减结果
 * 
 * @author 参考：Fox爱分享
 * @version 5.0.0
 */
public class DeductResult {
    
    /**
     * 扣减是否成功
     */
    private boolean success;
    
    /**
     * 用户ID
     */
    private Long userId;
    
    /**
     * 请求扣减积分
     */
    private int requestedPoints;
    
    /**
     * 实际扣减积分
     */
    private int actualDeducted;
    
    /**
     * 扣减后余额
     */
    private long balanceAfter;
    
    /**
     * 流水ID
     */
    private Long flowId;
    
    /**
     * 扣减的Bucket列表
     */
    private List<DeductedBucket> deductedBuckets;
    
    /**
     * 错误信息
     */
    private String errorMessage;
    
    // ==================== 嵌套类：扣减的Bucket信息 ====================
    
    public static class DeductedBucket {
        private Long bucketId;
        private int deductedAmount;
        private int remainingBalance;
        
        public DeductedBucket(Long bucketId, int deductedAmount, int remainingBalance) {
            this.bucketId = bucketId;
            this.deductedAmount = deductedAmount;
            this.remainingBalance = remainingBalance;
        }
        
        // Getters...
        public Long getBucketId() { return bucketId; }
        public int getDeductedAmount() { return deductedAmount; }
        public int getRemainingBalance() { return remainingBalance; }
    }
    
    // ==================== 工厂方法 ====================
    
    public static DeductResult success(Long userId, int requested, int actual, 
                                       long balance, Long flowId, 
                                       List<DeductedBucket> buckets) {
        DeductResult result = new DeductResult();
        result.success = true;
        result.userId = userId;
        result.requestedPoints = requested;
        result.actualDeducted = actual;
        result.balanceAfter = balance;
        result.flowId = flowId;
        result.deductedBuckets = buckets;
        return result;
    }
    
    public static DeductResult fail(Long userId, int requested, String errorMessage) {
        DeductResult result = new DeductResult();
        result.success = false;
        result.userId = userId;
        result.requestedPoints = requested;
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
    
    public int getRequestedPoints() {
        return requestedPoints;
    }
    
    public void setRequestedPoints(int requestedPoints) {
        this.requestedPoints = requestedPoints;
    }
    
    public int getActualDeducted() {
        return actualDeducted;
    }
    
    public void setActualDeducted(int actualDeducted) {
        this.actualDeducted = actualDeducted;
    }
    
    public long getBalanceAfter() {
        return balanceAfter;
    }
    
    public void setBalanceAfter(long balanceAfter) {
        this.balanceAfter = balanceAfter;
    }
    
    public Long getFlowId() {
        return flowId;
    }
    
    public void setFlowId(Long flowId) {
        this.flowId = flowId;
    }
    
    public List<DeductedBucket> getDeductedBuckets() {
        return deductedBuckets;
    }
    
    public void setDeductedBuckets(List<DeductedBucket> deductedBuckets) {
        this.deductedBuckets = deductedBuckets;
    }
    
    public String getErrorMessage() {
        return errorMessage;
    }
    
    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}
