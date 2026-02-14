package com.vivo.pointtask.iqiyi.storage;

import java.util.Map;

/**
 * 【爱奇艺亮点】MongoDB配置 - 统一存储配置
 * 
 * 参考来源：爱奇艺积分系统架构演进
 * 文章链接：https://mp.weixin.qq.com/s/vvSLW4EnsYEyHYIbGl8q2Q
 * 
 * 核心配置：
 * 1. 分片策略：支持水平扩展
 * 2. 写入确认：majority保证强一致性
 * 3. 索引配置：优化查询性能
 * 4. 副本集：高可用配置
 * 
 * 一致性策略：
 * - writeConcern: majority
 *   确保数据在主备节点间强一致传播
 *   用数据强一致性换取少量延迟波动（权衡合理）
 * 
 * @author 参考：爱奇艺技术产品团队
 * @version 4.0.0
 */
public class MongoDBConfig {
    
    /**
     * 连接URI
     */
    private String uri;
    
    /**
     * 数据库名称
     */
    private String database;
    
    /**
     * 是否启用分片
     */
    private boolean shardingEnabled;
    
    /**
     * 分片键（用于水平扩展）
     */
    private String shardKey;
    
    /**
     * 【核心】写入确认级别
     * 演化说明：配置为majority，确保数据在主备节点间强一致传播
     * 可选值：0-不确认 1-主节点确认 majority-大多数节点确认
     */
    private String writeConcern = "majority";
    
    /**
     * 读偏好
     * 可选值：primary primaryPreferred secondary secondaryPreferred nearest
     */
    private String readPreference = "primaryPreferred";
    
    /**
     * 连接池配置
     */
    private int minPoolSize = 10;
    private int maxPoolSize = 100;
    private int maxConnectionLifeTime = 3600000; // 1小时
    
    /**
     * 索引配置
     */
    private Map<String, String> indexConfig;
    
    /**
     * 集合配置
     */
    private Map<String, CollectionConfig> collectionConfigs;
    
    // ==================== 嵌套类：集合配置 ====================
    
    public static class CollectionConfig {
        /**
         * 集合名称
         */
        private String name;
        
        /**
         * 是否分片
         */
        private boolean sharded;
        
        /**
         * 分片键
         */
        private String shardKey;
        
        /**
         * 索引列表
         */
        private String[] indexes;
        
        public CollectionConfig(String name, boolean sharded, String shardKey, String[] indexes) {
            this.name = name;
            this.sharded = sharded;
            this.shardKey = shardKey;
            this.indexes = indexes;
        }
        
        // Getters...
        public String getName() { return name; }
        public boolean isSharded() { return sharded; }
        public String getShardKey() { return shardKey; }
        public String[] getIndexes() { return indexes; }
    }
    
    // ==================== 预定义集合配置 ====================
    
    /**
     * 用户积分账户集合配置
     */
    public static CollectionConfig USER_POINT_ACCOUNT = new CollectionConfig(
            "user_point_account",
            true,  // 启用分片
            "userId",  // 按用户ID分片
            new String[]{"userId_1", "pointLineId_1", "userId_1_pointLineId_1"}  // 复合索引
    );
    
    /**
     * 积分明细集合配置
     */
    public static CollectionConfig POINT_DETAIL = new CollectionConfig(
            "point_detail",
            true,  // 启用分片
            "userId",  // 按用户ID分片
            new String[]{"userId_1", "userId_1_createTime_-1"}  // 支持按时间倒序查询
    );
    
    /**
     * 积分线集合配置
     */
    public static CollectionConfig POINT_LINE = new CollectionConfig(
            "point_line",
            false,  // 不分片（数据量小）
            null,
            new String[]{"lineCode_1"}  // 按编码查询
    );
    
    // ==================== Getter/Setter ====================
    
    public String getUri() {
        return uri;
    }
    
    public void setUri(String uri) {
        this.uri = uri;
    }
    
    public String getDatabase() {
        return database;
    }
    
    public void setDatabase(String database) {
        this.database = database;
    }
    
    public boolean isShardingEnabled() {
        return shardingEnabled;
    }
    
    public void setShardingEnabled(boolean shardingEnabled) {
        this.shardingEnabled = shardingEnabled;
    }
    
    public String getShardKey() {
        return shardKey;
    }
    
    public void setShardKey(String shardKey) {
        this.shardKey = shardKey;
    }
    
    public String getWriteConcern() {
        return writeConcern;
    }
    
    public void setWriteConcern(String writeConcern) {
        this.writeConcern = writeConcern;
    }
    
    public String getReadPreference() {
        return readPreference;
    }
    
    public void setReadPreference(String readPreference) {
        this.readPreference = readPreference;
    }
    
    public int getMinPoolSize() {
        return minPoolSize;
    }
    
    public void setMinPoolSize(int minPoolSize) {
        this.minPoolSize = minPoolSize;
    }
    
    public int getMaxPoolSize() {
        return maxPoolSize;
    }
    
    public void setMaxPoolSize(int maxPoolSize) {
        this.maxPoolSize = maxPoolSize;
    }
    
    public int getMaxConnectionLifeTime() {
        return maxConnectionLifeTime;
    }
    
    public void setMaxConnectionLifeTime(int maxConnectionLifeTime) {
        this.maxConnectionLifeTime = maxConnectionLifeTime;
    }
    
    public Map<String, String> getIndexConfig() {
        return indexConfig;
    }
    
    public void setIndexConfig(Map<String, String> indexConfig) {
        this.indexConfig = indexConfig;
    }
    
    public Map<String, CollectionConfig> getCollectionConfigs() {
        return collectionConfigs;
    }
    
    public void setCollectionConfigs(Map<String, CollectionConfig> collectionConfigs) {
        this.collectionConfigs = collectionConfigs;
    }
}
