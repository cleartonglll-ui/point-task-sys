package com.vivo.pointtask.stage1.entity;

import java.time.LocalDateTime;

/**
 * 【演化阶段一】用户任务记录实体 - 贫血模型
 * 
 * 演化说明：
 * 记录用户与任务的关联关系，以及任务完成状态。
 * 同样采用贫血模型设计，仅作为数据载体。
 * 
 * @author vivo积分任务系统
 * @version 1.0.0
 */
public class UserTask {
    
    /**
     * 记录ID
     */
    private Long id;
    
    /**
     * 用户ID
     */
    private Long userId;
    
    /**
     * 任务ID
     */
    private Long taskId;
    
    /**
     * 任务完成状态：0-未完成 1-已完成 2-已领取奖励
     */
    private Integer status;
    
    /**
     * 任务进度（如阅读5篇文章中的3篇）
     */
    private Integer progress;
    
    /**
     * 任务目标值
     */
    private Integer targetValue;
    
    /**
     * 完成时间
     */
    private LocalDateTime completeTime;
    
    /**
     * 奖励领取时间
     */
    private LocalDateTime rewardTime;
    
    /**
     * 创建时间
     */
    private LocalDateTime createTime;
    
    /**
     * 更新时间
     */
    private LocalDateTime updateTime;
    
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
    
    public Long getTaskId() {
        return taskId;
    }
    
    public void setTaskId(Long taskId) {
        this.taskId = taskId;
    }
    
    public Integer getStatus() {
        return status;
    }
    
    public void setStatus(Integer status) {
        this.status = status;
    }
    
    public Integer getProgress() {
        return progress;
    }
    
    public void setProgress(Integer progress) {
        this.progress = progress;
    }
    
    public Integer getTargetValue() {
        return targetValue;
    }
    
    public void setTargetValue(Integer targetValue) {
        this.targetValue = targetValue;
    }
    
    public LocalDateTime getCompleteTime() {
        return completeTime;
    }
    
    public void setCompleteTime(LocalDateTime completeTime) {
        this.completeTime = completeTime;
    }
    
    public LocalDateTime getRewardTime() {
        return rewardTime;
    }
    
    public void setRewardTime(LocalDateTime rewardTime) {
        this.rewardTime = rewardTime;
    }
    
    public LocalDateTime getCreateTime() {
        return createTime;
    }
    
    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }
    
    public LocalDateTime getUpdateTime() {
        return updateTime;
    }
    
    public void setUpdateTime(LocalDateTime updateTime) {
        this.updateTime = updateTime;
    }
}
