package com.vivo.pointtask.iqiyi.entity;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * 【爱奇艺亮点】积分线实体 - 多业务线积分管理模型
 * 
 * 参考来源：爱奇艺积分系统架构演进
 * 文章链接：https://mp.weixin.qq.com/s/vvSLW4EnsYEyHYIbGl8q2Q
 * 
 * 核心概念：
 * 1. 业务方（BusinessUnit）：如极速版、基线业务、国际业务、综合端业务
 * 2. 业务线（BusinessLine）：每个业务方下设多个业务线
 * 3. 积分线（PointLine）：每条业务线关联多个积分线，一个积分线对应一个独立的积分账户维度
 * 
 * 设计亮点：
 * - 支持高度多样化的积分线结构
 * - 一个用户可对应多个积分线
 * - 每条积分线有独立的积分账户（总值+明细）
 * - 支持大规模并发访问
 * 
 * 业务价值：
 * - 多业务线隔离：不同业务线的积分相互独立
 * - 灵活扩展：新增业务线无需修改核心代码
 * - 统一管控：所有积分线在同一平台管理
 * 
 * @author 参考：爱奇艺技术产品团队
 * @version 4.0.0
 */
public class PointLine {
    
    /**
     * 积分线ID
     */
    private Long id;
    
    /**
     * 积分线编码（唯一标识）
     * 格式：业务方_业务线_积分类型，如：BASE_LINE_GOLD_COIN
     */
    private String lineCode;
    
    /**
     * 积分线名称
     */
    private String lineName;
    
    /**
     * 业务方编码
     * 如：极速版、基线业务、国际业务、综合端业务
     */
    private String businessUnit;
    
    /**
     * 业务线编码
     */
    private String businessLine;
    
    /**
     * 积分类型
     * 如：金币、积分、经验值等
     */
    private String pointType;
    
    /**
     * 积分线状态：0-禁用 1-启用
     */
    private Integer status;
    
    /**
     * 积分有效期类型：0-永久有效 1-固定期限 2-滚动期限
     */
    private Integer expireType;
    
    /**
     * 积分有效期（天）
     */
    private Integer expireDays;
    
    /**
     * 扩展配置（JSON格式）
     * 存储积分线的特殊配置，如兑换比例、使用范围等
     */
    private Map<String, Object> config;
    
    /**
     * 创建时间
     */
    private LocalDateTime createTime;
    
    /**
     * 更新时间
     */
    private LocalDateTime updateTime;
    
    // ==================== 充血模型：积分线业务行为 ====================
    
    /**
     * 【爱奇艺亮点】检查积分线是否有效
     * 
     * @return 是否有效
     */
    public boolean isValid() {
        return status != null && status == 1;
    }
    
    /**
     * 【爱奇艺亮点】检查积分是否过期
     * 
     * @param pointTime 积分获得时间
     * @return 是否过期
     */
    public boolean isExpired(LocalDateTime pointTime) {
        if (expireType == null || expireType == 0) {
            return false; // 永久有效
        }
        
        if (expireDays == null || expireDays <= 0) {
            return false;
        }
        
        LocalDateTime expireTime = pointTime.plusDays(expireDays);
        return LocalDateTime.now().isAfter(expireTime);
    }
    
    /**
     * 【爱奇艺亮点】获取积分过期时间
     * 
     * @param pointTime 积分获得时间
     * @return 过期时间
     */
    public LocalDateTime getExpireTime(LocalDateTime pointTime) {
        if (expireType == null || expireType == 0 || expireDays == null) {
            return null;
        }
        return pointTime.plusDays(expireDays);
    }
    
    /**
     * 【爱奇艺亮点】检查是否支持指定业务
     * 
     * @param businessUnit 业务方
     * @param businessLine 业务线
     * @return 是否支持
     */
    public boolean supportsBusiness(String businessUnit, String businessLine) {
        if (this.businessUnit == null || !this.businessUnit.equals(businessUnit)) {
            return false;
        }
        // 业务线为空表示支持该业务方下所有业务线
        if (this.businessLine == null || this.businessLine.isEmpty()) {
            return true;
        }
        return this.businessLine.equals(businessLine);
    }
    
    /**
     * 【爱奇艺亮点】获取配置项
     * 
     * @param key 配置键
     * @return 配置值
     */
    public Object getConfigItem(String key) {
        if (config == null) {
            return null;
        }
        return config.get(key);
    }
    
    // ==================== Getter/Setter ====================
    
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getLineCode() {
        return lineCode;
    }
    
    public void setLineCode(String lineCode) {
        this.lineCode = lineCode;
    }
    
    public String getLineName() {
        return lineName;
    }
    
    public void setLineName(String lineName) {
        this.lineName = lineName;
    }
    
    public String getBusinessUnit() {
        return businessUnit;
    }
    
    public void setBusinessUnit(String businessUnit) {
        this.businessUnit = businessUnit;
    }
    
    public String getBusinessLine() {
        return businessLine;
    }
    
    public void setBusinessLine(String businessLine) {
        this.businessLine = businessLine;
    }
    
    public String getPointType() {
        return pointType;
    }
    
    public void setPointType(String pointType) {
        this.pointType = pointType;
    }
    
    public Integer getStatus() {
        return status;
    }
    
    public void setStatus(Integer status) {
        this.status = status;
    }
    
    public Integer getExpireType() {
        return expireType;
    }
    
    public void setExpireType(Integer expireType) {
        this.expireType = expireType;
    }
    
    public Integer getExpireDays() {
        return expireDays;
    }
    
    public void setExpireDays(Integer expireDays) {
        this.expireDays = expireDays;
    }
    
    public Map<String, Object> getConfig() {
        return config;
    }
    
    public void setConfig(Map<String, Object> config) {
        this.config = config;
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
