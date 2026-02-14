package com.vivo.pointtask.meituan.service;

import com.vivo.pointtask.meituan.entity.PointDetailBucket;

import java.util.List;

/**
 * 【美团亮点】Bucket定期合并服务 - Compaction机制
 * 
 * 参考来源：美团面试文章《用户积分系统怎么设计》
 * 文章链接：https://mp.weixin.qq.com/s/vvSLW4EnsYEyHYIbGl8q2Q
 * 
 * 核心问题：
 * 连续签到3年 = 1000条Bucket，消费500分需要Update 500行 = 死锁风险
 * 
 * 解决方案：
 * 后台Job定期扫描，把同一用户、相同/相近过期时间的多个Bucket合并成一个大Bucket。
 * 
 * 合并策略：
 * 1. 按用户分组
 * 2. 找出同一用户、相同/相近过期时间（7天内）的多个Bucket
 * 3. 合并成一个大Bucket
 * 4. 删除旧的Bucket
 * 
 * 业务价值：
 * - 减少Bucket数量，降低数据库压力
 * - 减少锁冲突，提升并发性能
 * - 维持数据库性能，防止碎片化
 * 
 * 合并条件：
 * - 同一用户
 * - 都有效（status = 0）
 * - 过期时间相同或相近（7天内）
 * 
 * @author 参考：Fox爱分享
 * @version 5.0.0
 */
public interface BucketCompactionService {
    
    /**
     * 【核心方法】执行定期合并
     * 
     * 演化说明：
     * 扫描所有用户的Bucket，将满足合并条件的Bucket进行合并。
     * 
     * @return 合并结果
     */
    CompactionResult compact();
    
    /**
     * 【核心方法】合并指定用户的Bucket
     * 
     * @param userId 用户ID
     * @return 合并结果
     */
    UserCompactionResult compactUser(Long userId);
    
    /**
     * 【核心方法】查询可合并的Bucket组
     * 
     * 演化说明：
     * 找出同一用户、相同/相近过期时间的Bucket列表。
     * 
     * @param userId 用户ID
     * @return 可合并的Bucket组列表
     */
    List<MergeableBucketGroup> findMergeableBuckets(Long userId);
    
    /**
     * 【核心方法】合并一组Bucket
     * 
     * @param buckets 待合并的Bucket列表
     * @return 合并后的新Bucket
     */
    PointDetailBucket mergeBuckets(List<PointDetailBucket> buckets);
    
    /**
     * 【核心方法】获取合并统计信息
     * 
     * @return 统计信息
     */
    CompactionStatistics getStatistics();
}
