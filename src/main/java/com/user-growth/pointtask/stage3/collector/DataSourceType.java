package com.vivo.pointtask.stage3.collector;

/**
 * 【演化阶段三】数据源类型枚举
 * 
 * 演化逻辑（对应文章阶段三核心改进）：
 * 阶段二只支持埋点数据源，阶段三扩展到支持多种数据源：
 * 1. TRACKING - 埋点数据（阶段二已支持）
 * 2. DATABASE - 数据库（如MySQL）
 * 3. MESSAGE_QUEUE - 消息队列（如RocketMQ、Kafka）
 * 4. API - 外部API/RPC接口
 * 
 * 业务价值：
 * - 支持更多业务场景，如订单数据、支付数据等
 * - 无需埋点也能采集行为数据
 * - 与现有系统更好集成
 * 
 * @author vivo积分任务系统
 * @version 3.0.0
 */
public enum DataSourceType {
    
    /**
     * 埋点数据（阶段二已支持）
     * 通过SDK采集App端用户行为
     */
    TRACKING("埋点数据"),
    
    /**
     * 【演化新增】数据库
     * 演化说明：从MySQL等数据库采集数据，如订单表、用户表
     * 使用场景：消费返积分、注册送积分等
     */
    DATABASE("数据库"),
    
    /**
     * 【演化新增】消息队列
     * 演化说明：从RocketMQ、Kafka等消息队列消费数据
     * 使用场景：异步处理订单完成事件、支付成功事件等
     */
    MESSAGE_QUEUE("消息队列"),
    
    /**
     * 【演化新增】API/RPC接口
     * 演化说明：通过调用外部API获取数据
     * 使用场景：查询第三方服务数据
     */
    API("API接口");
    
    private final String description;
    
    DataSourceType(String description) {
        this.description = description;
    }
    
    public String getDescription() {
        return description;
    }
}
