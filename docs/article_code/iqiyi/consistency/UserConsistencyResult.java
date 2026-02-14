package com.vivo.pointtask.iqiyi.consistency;

/**
 * 【爱奇艺亮点】用户数据一致性校验结果
 * 
 * @author 参考：爱奇艺技术产品团队
 * @version 4.0.0
 */
public class UserConsistencyResult {
    
    /**
     * 用户ID
     */
    private Long userId;
    
    /**
     * 积分线ID
     */
    private Long pointLineId;
    
    /**
     * 是否一致
     */
    private boolean consistent;
    
    /**
     * 旧总值
     */
    private Long oldTotalPoints;
    
    /**
     * 新总值
     */
    private Long newTotalPoints;
    
    /**
     * 明细汇总值
     */
    private Long detailSumPoints;
    
    /**
     * 差异值
     */
    private Long diffPoints;
    
    /**
     * 错误信息
     */
    private String errorMessage;
    
    // ==================== 工厂方法 ====================
    
    public static UserConsistencyResult consistent(Long userId, Long pointLineId, Long points) {
        UserConsistencyResult result = new UserConsistencyResult();
        result.userId = userId;
        result.pointLineId = pointLineId;
        result.consistent = true;
        result.oldTotalPoints = points;
        result.newTotalPoints = points;
        result.detailSumPoints = points;
        result.diffPoints = 0L;
        return result;
    }
    
    public static UserConsistencyResult inconsistent(Long userId, Long pointLineId, 
                                                     Long oldTotal, Long newTotal, Long detailSum) {
        UserConsistencyResult result = new UserConsistencyResult();
        result.userId = userId;
        result.pointLineId = pointLineId;
        result.consistent = false;
        result.oldTotalPoints = oldTotal;
        result.newTotalPoints = newTotal;
        result.detailSumPoints = detailSum;
        result.diffPoints = Math.abs(newTotal - detailSum);
        return result;
    }
    
    // ==================== Getter/Setter ====================
    
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
    
    public boolean isConsistent() {
        return consistent;
    }
    
    public void setConsistent(boolean consistent) {
        this.consistent = consistent;
    }
    
    public Long getOldTotalPoints() {
        return oldTotalPoints;
    }
    
    public void setOldTotalPoints(Long oldTotalPoints) {
        this.oldTotalPoints = oldTotalPoints;
    }
    
    public Long getNewTotalPoints() {
        return newTotalPoints;
    }
    
    public void setNewTotalPoints(Long newTotalPoints) {
        this.newTotalPoints = newTotalPoints;
    }
    
    public Long getDetailSumPoints() {
        return detailSumPoints;
    }
    
    public void setDetailSumPoints(Long detailSumPoints) {
        this.detailSumPoints = detailSumPoints;
    }
    
    public Long getDiffPoints() {
        return diffPoints;
    }
    
    public void setDiffPoints(Long diffPoints) {
        this.diffPoints = diffPoints;
    }
    
    public String getErrorMessage() {
        return errorMessage;
    }
    
    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}
