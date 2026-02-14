package com.vivo.pointtask.stage2.entity;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * 【演化阶段二】行为实体 - 核心新增
 * 
 * 演化逻辑（对应文章阶段二核心改进）：
 * 引入行为模型，将"用户行为"抽象为独立的实体。
 * 这是从贫血模型向充血模型演化的关键一步。
 * 
 * 核心改进：
 * 1. 行为定义与任务解耦，一个行为可以关联多个任务
 * 2. 行为包含埋点事件名和过滤条件
 * 3. 系统通过SDK采集埋点，自动匹配行为定义
 * 
 * 业务价值：
 * - 业务方无需自行实现行为采集
 * - 一次SDK接入，后续任务零成本开发
 * - 上线周期从1-3个月缩短到1-3人天
 * 
 * @author vivo积分任务系统
 * @version 2.0.0
 */
public class Behavior {
    
    /**
     * 行为ID
     */
    private Long id;
    
    /**
     * 行为名称
     */
    private String behaviorName;
    
    /**
     * 行为描述
     */
    private String description;
    
    /**
     * 【核心】埋点事件名称
     * 演化说明：对应App端埋点的事件名，如"article_read", "video_play"
     * SDK通过监听这些事件来采集用户行为
     */
    private String eventName;
    
    /**
     * 【核心】事件过滤条件（JSON格式）
     * 演化说明：用于精确匹配行为，如只统计阅读"科技"分类的文章
     * 示例：{"category": "tech", "read_time": ">30"}
     */
    private Map<String, Object> filterConditions;
    
    /**
     * 行为状态：0-禁用 1-启用
     */
    private Integer status;
    
    /**
     * 创建时间
     */
    private LocalDateTime createTime;
    
    /**
     * 更新时间
     */
    private LocalDateTime updateTime;
    
    // ==================== 充血模型：行为自我验证 ====================
    
    /**
     * 【演化改进】验证埋点事件是否匹配当前行为定义
     * 演化说明：行为实体自己知道如何验证事件是否匹配
     * 
     * @param event 埋点事件
     * @return 是否匹配
     */
    public boolean matchesEvent(TrackingEvent event) {
        // 首先检查事件名是否匹配
        if (!eventName.equals(event.getEventName())) {
            return false;
        }
        
        // 如果没有过滤条件，则事件名匹配即可
        if (filterConditions == null || filterConditions.isEmpty()) {
            return true;
        }
        
        // 检查过滤条件
        Map<String, Object> eventParams = event.getParameters();
        for (Map.Entry<String, Object> condition : filterConditions.entrySet()) {
            String key = condition.getKey();
            Object expectedValue = condition.getValue();
            Object actualValue = eventParams.get(key);
            
            // 如果事件参数中不包含该字段，则不匹配
            if (actualValue == null) {
                return false;
            }
            
            // 简单相等判断（实际可能需要更复杂的比较逻辑）
            if (!expectedValue.toString().equals(actualValue.toString())) {
                return false;
            }
        }
        
        return true;
    }
    
    /**
     * 【演化改进】判断行为是否有效
     * 
     * @return 是否有效
     */
    public boolean isValid() {
        return status != null && status == 1;
    }
    
    // ==================== Getter/Setter ====================
    
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getBehaviorName() {
        return behaviorName;
    }
    
    public void setBehaviorName(String behaviorName) {
        this.behaviorName = behaviorName;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public String getEventName() {
        return eventName;
    }
    
    public void setEventName(String eventName) {
        this.eventName = eventName;
    }
    
    public Map<String, Object> getFilterConditions() {
        return filterConditions;
    }
    
    public void setFilterConditions(Map<String, Object> filterConditions) {
        this.filterConditions = filterConditions;
    }
    
    public Integer getStatus() {
        return status;
    }
    
    public void setStatus(Integer status) {
        this.status = status;
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
