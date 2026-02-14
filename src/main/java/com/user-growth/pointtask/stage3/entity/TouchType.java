package com.vivo.pointtask.stage3.entity;

/**
 * 【演化阶段三】触达方式枚举
 * 
 * 演化说明：
 * 阶段二只支持TOAST和SNACKBAR两种简单触达方式。
 * 阶段三扩展到支持DIALOG自定义弹窗和NOTIFICATION消息透传。
 * 
 * @author vivo积分任务系统
 * @version 3.0.0
 */
public enum TouchType {
    
    /**
     * 不触达
     */
    NONE("不触达"),
    
    /**
     * Toast提示（阶段二支持）
     */
    TOAST("Toast提示"),
    
    /**
     * Snackbar提示（阶段二支持）
     */
    SNACKBAR("Snackbar提示"),
    
    /**
     * 【演化新增】自定义弹窗
     * 演化说明：支持富文本、图片、按钮等复杂交互
     */
    DIALOG("自定义弹窗"),
    
    /**
     * 【演化新增】消息透传/推送通知
     * 演化说明：支持消息推送，即使用户不在App内也能收到通知
     */
    NOTIFICATION("消息通知");
    
    private final String description;
    
    TouchType(String description) {
        this.description = description;
    }
    
    public String getDescription() {
        return description;
    }
}
