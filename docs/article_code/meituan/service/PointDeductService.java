package com.vivo.pointtask.meituan.service;

import com.vivo.pointtask.meituan.entity.PointDetailBucket;
import com.vivo.pointtask.meituan.entity.PointFlowLog;
import com.vivo.pointtask.meituan.entity.UserPointWallet;

import java.util.ArrayList;
import java.util.List;

/**
 * 【美团亮点】积分扣减服务 - FIFO扣减机制实现
 * 
 * 参考来源：美团面试文章《用户积分系统怎么设计》
 * 文章链接：https://mp.weixin.qq.com/s/vvSLW4EnsYEyHYIbGl8q2Q
 * 
 * 核心设计：
 * FIFO（First In First Out）先进先出扣减机制，优先扣除快过期的积分。
 * 
 * 扣减流程：
 * 1. 查询：按expire_time ASC排序查询point_detail_bucket
 * 2. 扣减：找到第一条快过期的记录，current_balance够扣就扣，不够扣完再找下一条
 * 3. 更新：更新user_point_wallet总数
 * 
 * 业务规则：
 * - 优先扣除快过期的积分（对用户最有利原则）
 * - 一笔积分可以分多次消费
 * - 支持部分扣减（一个Bucket不够扣时，继续扣下一个）
 * 
 * 资损防控：
 * - 使用乐观锁防止并发扣减超卖
 * - 三表同步更新：明细表、流水表、总额表
 * - 拒绝Redis异步写，坚持DB强一致性
 * 
 * @author 参考：Fox爱分享
 * @version 5.0.0
 */
public interface PointDeductService {
    
    /**
     * 【核心方法】FIFO扣减积分
     * 
     * 演化说明：
     * 这是积分扣减的核心方法，遵循FIFO原则，优先扣除快过期的积分。
     * 
     * 执行流程：
     * 1. 校验用户积分是否充足
     * 2. 按过期时间排序查询有效的Bucket
     * 3. 依次扣减Bucket，直到扣满所需积分
     * 4. 生成流水记录
     * 5. 更新总额表
     * 
     * @param userId 用户ID
     * @param points 需要扣除的积分
     * @param type 扣减类型（兑换/消费等）
     * @param refId 关联业务单号
     * @return 扣减结果
     */
    DeductResult deductPoints(Long userId, int points, int type, String refId);
    
    /**
     * 【核心方法】查询用户可扣减的Bucket列表（按过期时间排序）
     * 
     * 演化说明：
     * 按expire_time ASC排序，优先返回快过期的Bucket。
     * 关键索引：idx_user_expire (user_id, expire_time)
     * 
     * @param userId 用户ID
     * @return 按过期时间排序的Bucket列表
     */
    List<PointDetailBucket> getDeductibleBuckets(Long userId);
    
    /**
     * 【核心方法】计算用户可用积分总额
     * 
     * 演化说明：
     * 通过汇总所有有效Bucket的current_balance计算总额。
     * 用于与UserPointWallet的total_balance进行对账。
     * 
     * @param userId 用户ID
     * @return 可用积分总额
     */
    long calculateAvailablePoints(Long userId);
    
    /**
     * 【核心方法】退款返还积分
     * 
     * 演化说明：
     * 原路退回原则。扣减时记录consumption_log，退款时逆向恢复。
     * - 未过期Bucket：恢复余额
     * - 已过期Bucket：不退或延期7天（看业务良心）
     * 
     * @param userId 用户ID
     * @param originalFlowId 原扣减流水ID
     * @param refundPoints 返还积分
     * @return 返还结果
     */
    RefundResult refundPoints(Long userId, Long originalFlowId, int refundPoints);
}
