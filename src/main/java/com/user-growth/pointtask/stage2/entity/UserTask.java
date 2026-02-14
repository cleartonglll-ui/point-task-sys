package com.vivo.pointtask.stage2.entity;

import java.time.LocalDateTime;

/**
 * 【演化阶段二】用户任务记录实体
 * 
 * @author vivo积分任务系统
 * @version 2.0.0
 */
public class UserTask {
    
    private Long id;
    private Long userId;
    private Long taskId;
    private Integer status; // 0-未完成 1-已完成 2-已领取奖励
    private Integer progress;
    private Integer targetValue;
    private LocalDateTime completeTime;
    private LocalDateTime rewardTime;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
    
    // ==================== 充血模型：自我验证 ====================
    
    /**
     * 【演化改进】检查任务是否已完成
     * 
     * @return 是否已完成
     */
    public boolean isCompleted() {
        return status != null && status >= 1;
    }
    
    /**
     * 【演化改进】检查奖励是否已领取
     * 
     * @return 是否已领取
     */
    public boolean isRewardClaimed() {
        return status != null && status == 2;
    }
    
    /**
     * 【演化改进】获取完成进度百分比
     * 
     * @return 进度百分比
     */
    public int getProgressPercentage() {
        if (targetValue == null || targetValue == 0) {
            return 0;
        }
        return Math.min(100, (progress * 100) / targetValue);
    }
    
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
