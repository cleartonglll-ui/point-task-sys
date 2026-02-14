package com.vivo.pointtask.iqiyi.consistency;

import java.util.List;

/**
 * 【爱奇艺亮点】数据修复结果
 * 
 * @author 参考：爱奇艺技术产品团队
 * @version 4.0.0
 */
public class RepairResult {
    
    /**
     * 修复是否成功
     */
    private boolean success;
    
    /**
     * 总记录数
     */
    private int totalCount;
    
    /**
     * 修复成功数
     */
    private int successCount;
    
    /**
     * 修复失败数
     */
    private int failCount;
    
    /**
     * 失败的记录列表
     */
    private List<InconsistencyRecord> failedRecords;
    
    /**
     * 错误信息
     */
    private String errorMessage;
    
    // ==================== 工厂方法 ====================
    
    public static RepairResult success(int totalCount, int successCount) {
        RepairResult result = new RepairResult();
        result.success = true;
        result.totalCount = totalCount;
        result.successCount = successCount;
        result.failCount = totalCount - successCount;
        return result;
    }
    
    public static RepairResult fail(String errorMessage) {
        RepairResult result = new RepairResult();
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
    
    public int getTotalCount() {
        return totalCount;
    }
    
    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }
    
    public int getSuccessCount() {
        return successCount;
    }
    
    public void setSuccessCount(int successCount) {
        this.successCount = successCount;
    }
    
    public int getFailCount() {
        return failCount;
    }
    
    public void setFailCount(int failCount) {
        this.failCount = failCount;
    }
    
    public List<InconsistencyRecord> getFailedRecords() {
        return failedRecords;
    }
    
    public void setFailedRecords(List<InconsistencyRecord> failedRecords) {
        this.failedRecords = failedRecords;
    }
    
    public String getErrorMessage() {
        return errorMessage;
    }
    
    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}
