package com.vivo.pointtask.iqiyi.consistency;

import java.util.List;

/**
 * 【爱奇艺亮点】一致性校验结果
 * 
 * @author 参考：爱奇艺技术产品团队
 * @version 4.0.0
 */
public class ConsistencyCheckResult {
    
    /**
     * 校验是否通过
     */
    private boolean passed;
    
    /**
     * 校验总数
     */
    private long totalCount;
    
    /**
     * 一致数量
     */
    private long consistentCount;
    
    /**
     * 不一致数量
     */
    private long inconsistentCount;
    
    /**
     * 不一致记录列表
     */
    private List<InconsistencyRecord> inconsistencies;
    
    /**
     * 校验时间
     */
    private String checkTime;
    
    /**
     * 错误信息（如果校验失败）
     */
    private String errorMessage;
    
    // ==================== 工厂方法 ====================
    
    public static ConsistencyCheckResult success(long totalCount, long consistentCount) {
        ConsistencyCheckResult result = new ConsistencyCheckResult();
        result.passed = true;
        result.totalCount = totalCount;
        result.consistentCount = consistentCount;
        result.inconsistentCount = totalCount - consistentCount;
        return result;
    }
    
    public static ConsistencyCheckResult fail(String errorMessage) {
        ConsistencyCheckResult result = new ConsistencyCheckResult();
        result.passed = false;
        result.errorMessage = errorMessage;
        return result;
    }
    
    public static ConsistencyCheckResult withInconsistencies(long totalCount, List<InconsistencyRecord> inconsistencies) {
        ConsistencyCheckResult result = new ConsistencyCheckResult();
        result.passed = inconsistencies.isEmpty();
        result.totalCount = totalCount;
        result.consistentCount = totalCount - inconsistencies.size();
        result.inconsistentCount = inconsistencies.size();
        result.inconsistencies = inconsistencies;
        return result;
    }
    
    // ==================== Getter/Setter ====================
    
    public boolean isPassed() {
        return passed;
    }
    
    public void setPassed(boolean passed) {
        this.passed = passed;
    }
    
    public long getTotalCount() {
        return totalCount;
    }
    
    public void setTotalCount(long totalCount) {
        this.totalCount = totalCount;
    }
    
    public long getConsistentCount() {
        return consistentCount;
    }
    
    public void setConsistentCount(long consistentCount) {
        this.consistentCount = consistentCount;
    }
    
    public long getInconsistentCount() {
        return inconsistentCount;
    }
    
    public void setInconsistentCount(long inconsistentCount) {
        this.inconsistentCount = inconsistentCount;
    }
    
    public List<InconsistencyRecord> getInconsistencies() {
        return inconsistencies;
    }
    
    public void setInconsistencies(List<InconsistencyRecord> inconsistencies) {
        this.inconsistencies = inconsistencies;
    }
    
    public String getCheckTime() {
        return checkTime;
    }
    
    public void setCheckTime(String checkTime) {
        this.checkTime = checkTime;
    }
    
    public String getErrorMessage() {
        return errorMessage;
    }
    
    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
    
    /**
     * 获取一致率
     * 
     * @return 一致率（0-1之间）
     */
    public double getConsistencyRate() {
        if (totalCount == 0) {
            return 1.0;
        }
        return (double) consistentCount / totalCount;
    }
}
