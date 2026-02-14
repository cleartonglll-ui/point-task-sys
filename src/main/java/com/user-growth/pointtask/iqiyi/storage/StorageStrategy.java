package com.vivo.pointtask.iqiyi.storage;

/**
 * 【爱奇艺亮点】存储策略枚举 - 存储架构演进
 * 
 * 参考来源：爱奇艺积分系统架构演进
 * 文章链接：https://mp.weixin.qq.com/s/vvSLW4EnsYEyHYIbGl8q2Q
 * 
 * 存储架构演进：
 * 
 * 【阶段一】分离式存储（原有架构）
 * - MySQL：存储积分总值（频繁读写）
 * - MongoDB：存储积分明细（海量数据）
 * - 问题：一致性问题、维护成本高、性能瓶颈
 * 
 * 【阶段二】统一存储（演进后架构）
 * - MongoDB 7.0：统一存储总值+明细
 * - 优势：简化架构、提升性能、保证一致性
 * 
 * 选型理由（MongoDB 7.0）：
 * 1. 扩展能力：原生分片机制支持水平扩展
 * 2. 并发能力：支持高并发积分操作
 * 3. 事务能力：支持多文档事务，保证数据一致性
 * 4. 冗灾能力：高可用、自动故障转移
 * 5. 建模能力：灵活的文档模型
 * 
 * @author 参考：爱奇艺技术产品团队
 * @version 4.0.0
 */
public enum StorageStrategy {
    
    /**
     * 【原有架构】分离式存储
     * MySQL存储总值 + MongoDB存储明细
     */
    SEPARATED("分离式存储", "MySQL+MongoDB", false),
    
    /**
     * 【演进架构】MongoDB统一存储
     * MongoDB 7.0统一存储总值+明细
     */
    UNIFIED_MONGODB("统一存储", "MongoDB 7.0", true),
    
    /**
     * 【混合模式】双写过渡阶段
     * 新旧系统同时写入，用于数据迁移阶段
     */
    DUAL_WRITE("双写过渡", "MySQL+MongoDB双写", false);
    
    private final String displayName;
    private final String storageType;
    private final boolean recommended;
    
    StorageStrategy(String displayName, String storageType, boolean recommended) {
        this.displayName = displayName;
        this.storageType = storageType;
        this.recommended = recommended;
    }
    
    public String getDisplayName() {
        return displayName;
    }
    
    public String getStorageType() {
        return storageType;
    }
    
    public boolean isRecommended() {
        return recommended;
    }
}
