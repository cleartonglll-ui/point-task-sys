package com.vivo.pointtask.iqiyi.consistency;

import java.util.List;

/**
 * 【爱奇艺亮点】数据一致性校验器 - 多重校验机制
 * 
 * 参考来源：爱奇艺积分系统架构演进
 * 文章链接：https://mp.weixin.qq.com/s/vvSLW4EnsYEyHYIbGl8q2Q
 * 
 * 核心设计：
 * 在数据迁移和双写阶段，通过多重校验确保数据一致性。
 * 任意不一致触发告警，问题修复后循环验证。
 * 
 * 校验维度：
 * 1. 明细实验表 vs 明细对照表（新旧明细对比）
 * 2. 新总值表 vs 老总值表（新旧总值对比）
 * 3. 新总值表 vs 新明细表（总值与明细汇总对比）
 * 
 * 业务价值：
 * - 保障数据迁移过程中的数据一致性
 * - 及时发现双写不一致问题
 * - 自动化校验，降低人工成本
 * 
 * @author 参考：爱奇艺技术产品团队
 * @version 4.0.0
 */
public interface DataConsistencyChecker {
    
    /**
     * 【核心方法】执行全量一致性校验
     * 
     * 演化说明：
     * 在正式切流前，对全量数据进行校验，确保100%一致
     * 
     * @return 校验结果
     */
    ConsistencyCheckResult checkAll();
    
    /**
     * 【核心方法】执行增量一致性校验
     * 
     * 演化说明：
     * 在双写阶段，持续校验增量数据的一致性
     * 
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return 校验结果
     */
    ConsistencyCheckResult checkIncremental(String startTime, String endTime);
    
    /**
     * 【核心方法】校验单个用户的积分数据
     * 
     * @param userId 用户ID
     * @param pointLineId 积分线ID
     * @return 校验结果
     */
    UserConsistencyResult checkUser(Long userId, Long pointLineId);
    
    /**
     * 【核心方法】修复不一致数据
     * 
     * @param inconsistencies 不一致记录列表
     * @return 修复结果
     */
    RepairResult repairInconsistencies(List<InconsistencyRecord> inconsistencies);
}
