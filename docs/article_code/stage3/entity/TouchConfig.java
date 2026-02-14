package com.vivo.pointtask.stage3.entity;

import java.util.Map;

/**
 * 【演化阶段三】触达配置实体
 * 
 * 演化逻辑（对应文章阶段三）：
 * 支持多种触达方式，从简单的Toast/Snackbar扩展到自定义弹窗和消息透传。
 * 
 * 支持的触达方式：
 * 1. TOAST - 简单的Toast提示
 * 2. SNACKBAR - 底部Snackbar提示
 * 3. DIALOG - 自定义弹窗（支持富文本、图片等）
 * 4. NOTIFICATION - 消息透传/推送通知
 * 5. NONE - 不触达
 * 
 * @author vivo积分任务系统
 * @version 3.0.0
 */
public class TouchConfig {
    
    /**
     * 是否启用触达
     */
    private boolean enabled;
    
    /**
     * 触达方式类型
     */
    private TouchType touchType;
    
    /**
     * 触达标题
     */
    private String title;
    
    /**
     * 触达内容
     * 支持模板变量，如："恭喜您完成{taskName}，获得{points}积分！"
     */
    private String content;
    
    /**
     * 触达图片URL（用于弹窗）
     */
    private String imageUrl;
    
    /**
     * 跳转链接
     */
    private String redirectUrl;
    
    /**
     * 扩展参数
     */
    private Map<String, Object> extraParams;
    
    // ==================== 充血模型：触达配置自我验证 ====================
    
    /**
     * 【演化改进】验证触达配置是否有效
     * 
     * @return 是否有效
     */
    public boolean isValid() {
        if (!enabled) {
            return true; // 禁用时不需要验证
        }
        
        if (touchType == null || touchType == TouchType.NONE) {
            return false;
        }
        
        // 弹窗类型需要标题和内容
        if (touchType == TouchType.DIALOG) {
            return title != null && !title.isEmpty() && 
                   content != null && !content.isEmpty();
        }
        
        return content != null && !content.isEmpty();
    }
    
    /**
     * 【演化改进】渲染触达内容
     * 演化说明：支持模板变量替换
     * 
     * @param variables 模板变量
     * @return 渲染后的内容
     */
    public String renderContent(Map<String, String> variables) {
        if (content == null) {
            return "";
        }
        
        String rendered = content;
        for (Map.Entry<String, String> entry : variables.entrySet()) {
            rendered = rendered.replace("{" + entry.getKey() + "}", entry.getValue());
        }
        
        return rendered;
    }
    
    // ==================== Getter/Setter ====================
    
    public boolean isEnabled() {
        return enabled;
    }
    
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
    
    public TouchType getTouchType() {
        return touchType;
    }
    
    public void setTouchType(TouchType touchType) {
        this.touchType = touchType;
    }
    
    public String getTitle() {
        return title;
    }
    
    public void setTitle(String title) {
        this.title = title;
    }
    
    public String getContent() {
        return content;
    }
    
    public void setContent(String content) {
        this.content = content;
    }
    
    public String getImageUrl() {
        return imageUrl;
    }
    
    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
    
    public String getRedirectUrl() {
        return redirectUrl;
    }
    
    public void setRedirectUrl(String redirectUrl) {
        this.redirectUrl = redirectUrl;
    }
    
    public Map<String, Object> getExtraParams() {
        return extraParams;
    }
    
    public void setExtraParams(Map<String, Object> extraParams) {
        this.extraParams = extraParams;
    }
}
