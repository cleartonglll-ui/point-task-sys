package com.vivo.pointtask.stage3.collector;

import java.util.Map;

/**
 * 【演化阶段三】数据源配置实体
 * 
 * 演化逻辑（对应文章阶段三）：
 * 配置化定义数据源，支持多种数据源类型的统一配置。
 * 
 * 配置示例：
 * 1. 数据库源：{"url": "jdbc:mysql://...", "table": "orders", "pollInterval": 60}
 * 2. MQ源：{"topic": "order_completed", "consumerGroup": "point_task"}
 * 3. API源：{"endpoint": "http://api.xxx.com/orders", "method": "GET"}
 * 
 * @author vivo积分任务系统
 * @version 3.0.0
 */
public class DataSourceConfig {
    
    /**
     * 数据源ID
     */
    private Long id;
    
    /**
     * 数据源名称
     */
    private String name;
    
    /**
     * 数据源类型
     */
    private DataSourceType type;
    
    /**
     * 连接配置（JSON格式）
     * 根据数据源类型不同，配置内容不同
     */
    private Map<String, Object> connectionConfig;
    
    /**
     * 数据预处理表达式（Aviator表达式）
     * 演化说明：在数据进入规则计算层前进行过滤和转换
     * 示例："originEvent.pay_status == 1 && originEvent.amount > 0"
     */
    private String preprocessExpression;
    
    /**
     * 数据映射配置
     * 演化说明：将不同数据源的数据格式统一映射为标准格式
     */
    private Map<String, String> fieldMapping;
    
    /**
     * 是否启用
     */
    private boolean enabled;
    
    /**
     * 轮询间隔（秒），用于数据库类型
     */
    private Integer pollInterval;
    
    // ==================== 充血模型：配置验证 ====================
    
    /**
     * 【演化改进】验证数据源配置是否有效
     * 
     * @return 是否有效
     */
    public boolean isValid() {
        if (!enabled) {
            return true;
        }
        
        if (type == null) {
            return false;
        }
        
        if (connectionConfig == null || connectionConfig.isEmpty()) {
            return false;
        }
        
        // 根据类型验证必要配置
        switch (type) {
            case DATABASE:
                return connectionConfig.containsKey("url") && 
                       connectionConfig.containsKey("table");
            case MESSAGE_QUEUE:
                return connectionConfig.containsKey("topic");
            case API:
                return connectionConfig.containsKey("endpoint");
            case TRACKING:
                return true; // 埋点数据源不需要额外配置
            default:
                return false;
        }
    }
    
    /**
     * 【演化改进】获取标准化的事件数据
     * 演化说明：将不同数据源的数据格式统一为标准格式
     * 
     * @param rawData 原始数据
     * @return 标准化数据
     */
    public Map<String, Object> normalizeData(Map<String, Object> rawData) {
        if (fieldMapping == null || fieldMapping.isEmpty()) {
            return rawData;
        }
        
        Map<String, Object> normalized = new java.util.HashMap<>();
        
        for (Map.Entry<String, String> mapping : fieldMapping.entrySet()) {
            String sourceField = mapping.getKey();
            String targetField = mapping.getValue();
            Object value = rawData.get(sourceField);
            normalized.put(targetField, value);
        }
        
        // 保留原始数据
        normalized.put("_raw", rawData);
        normalized.put("_sourceType", type.name());
        
        return normalized;
    }
    
    // ==================== Getter/Setter ====================
    
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public DataSourceType getType() {
        return type;
    }
    
    public void setType(DataSourceType type) {
        this.type = type;
    }
    
    public Map<String, Object> getConnectionConfig() {
        return connectionConfig;
    }
    
    public void setConnectionConfig(Map<String, Object> connectionConfig) {
        this.connectionConfig = connectionConfig;
    }
    
    public String getPreprocessExpression() {
        return preprocessExpression;
    }
    
    public void setPreprocessExpression(String preprocessExpression) {
        this.preprocessExpression = preprocessExpression;
    }
    
    public Map<String, String> getFieldMapping() {
        return fieldMapping;
    }
    
    public void setFieldMapping(Map<String, String> fieldMapping) {
        this.fieldMapping = fieldMapping;
    }
    
    public boolean isEnabled() {
        return enabled;
    }
    
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
    
    public Integer getPollInterval() {
        return pollInterval;
    }
    
    public void setPollInterval(Integer pollInterval) {
        this.pollInterval = pollInterval;
    }
}
