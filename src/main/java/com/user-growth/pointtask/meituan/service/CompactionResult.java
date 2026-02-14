package com.vivo.pointtask.meituan.service;

/**
 * 【美团亮点】定期合并结果
 * 
 * @author 参考：Fox爱分享
 * @version 5.0.0
 */
public class CompactionResult {
    
    /**
     * 合并是否成功
     */
    private boolean success;
    
    /**
     * 扫描用户总数
     */
    private int scannedUsers;
    
    /**
     * 合并用户数量
     */
    private int mergedUsers;
    
    /**
     * 合并前Bucket总数
     */
    private int bucketsBefore;
    
    /**
     * 合并后Bucket总数
     */
    private int bucketsAfter;
    
    /**
     * 减少的Bucket数量
     */
    private int bucketsReduced;
    
    /**
     * 执行时间（毫秒）
     */
    private long executionTime;
    
    /**
     * 错误信息
     */
    private String errorMessage;
    
    // ==================== 工厂方法 ====================
    
    public static CompactionResult success(int scannedUsers, int mergedUsers,
                                           int before, int after, long executionTime) {
        CompactionResult result = new CompactionResult();
        result.success = true;
        result.scannedUsers = scannedUsers;
        result.mergedUsers = mergedUsers;
        result.bucketsBefore = before;
        result.bucketsAfter = after;
        result.bucketsReduced = before - after;
        result.executionTime = executionTime;
        return result;
    }
    
    public static CompactionResult fail(String errorMessage) {
        CompactionResult result = new CompactionResult();
        result.success = false;
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
    
    public int getScannedUsers() {
        return scannedUsers;
    }
    
    public void setScannedUsers(int scannedUsers) {
        this.scannedUsers = scannedUsers;
    }
    
    public int getMergedUsers() {
        return mergedUsers;
    }
    
    public void setMergedUsers(int mergedUsers) {
        this.mergedUsers = mergedUsers;
    }
    
    public int getBucketsBefore() {
        return bucketsBefore;
    }
    
    public void setBucketsBefore(int bucketsBefore) {
        this.bucketsBefore = bucketsBefore;
    }
    
    public int getBucketsAfter() {
        return bucketsAfter;
    }
    
    public void setBucketsAfter(int bucketsAfter) {
        this.bucketsAfter = bucketsAfter;
    }
    
    public int getBucketsReduced() {
        return bucketsReduced;
    }
    
    public void setBucketsReduced(int bucketsReduced) {
        this.bucketsReduced = bucketsReduced;
    }
    
    public long getExecutionTime() {
        return executionTime;
    }
    
    public void setExecutionTime(long executionTime) {
        this.executionTime = executionTime;
    }
    
    public String getErrorMessage() {
        return errorMessage;
    }
    
    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
    
    /**
     * 获取压缩率
     * 
     * @return 压缩率（0-1之间）
     */
    public double getCompressionRatio() {
        if (bucketsBefore == 0) {
            return 0.0;
        }
        return (double) bucketsReduced / bucketsBefore;
    }
}
