package com.vivo.pointtask.iqiyi.consistency;

/**
 * 【爱奇艺亮点】不一致记录
 * 
 * 记录数据不一致的详细信息，用于后续修复
 * 
 * @author 参考：爱奇艺技术产品团队
 * @version 4.0.0
 */
public class InconsistencyRecord {
    
    /**
     * 记录ID
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
     * 校验维度
     * 1-新旧明细对比 2-新旧总值对比 3-总值与明细汇总对比
     */
    private Integer checkDimension;
    
    /**
     * 旧值
     */
    private String oldValue;
    
    /**
     * 新值
     */
    private String newValue;
    
    /**
     * 差异描述
     */
    private String diffDescription;
    
    /**
     * 修复状态：0-未修复 1-已修复 2-修复失败
     */
    private Integer repairStatus;
    
    /**
     * 修复时间
     */
    private String repairTime;
    
    /**
     * 创建时间
     */
    private String createTime;
    
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
    
    public Integer getCheckDimension() {
        return checkDimension;
    }
    
    public void setCheckDimension(Integer checkDimension) {
        this.checkDimension = checkDimension;
    }
    
    public String getOldValue() {
        return oldValue;
    }
    
    public void setOldValue(String oldValue) {
        this.oldValue = oldValue;
    }
    
    public String getNewValue() {
        return newValue;
    }
    
    public void setNewValue(String newValue) {
        this.newValue = newValue;
    }
    
    public String getDiffDescription() {
        return diffDescription;
    }
    
    public void setDiffDescription(String diffDescription) {
        this.diffDescription = diffDescription;
    }
    
    public Integer getRepairStatus() {
        return repairStatus;
    }
    
    public void setRepairStatus(Integer repairStatus) {
        this.repairStatus = repairStatus;
    }
    
    public String getRepairTime() {
        return repairTime;
    }
    
    public void setRepairTime(String repairTime) {
        this.repairTime = repairTime;
    }
    
    public String getCreateTime() {
        return createTime;
    }
    
    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }
}
