package com.user.growth.reward.reward.context.repository;

import com.user.growth.reward.reward.context.domain.RewardClaimRecord;
import com.user.growth.reward.reward.context.domain.RewardConfig;

import java.util.List;

/**
 * 奖励仓储接口
 *
 * @author system
 * @since 1.0.0
 */
public interface RewardRepository {

    /**
     * 获取所有上架的奖励
     *
     * @return 奖励配置列表
     */
    List<RewardConfig> findAllOnShelf();

    /**
     * 根据 id 获取奖励配置
     *
     * @param id 奖励ID
     * @return 奖励配置
     */
    RewardConfig findById(Long id);

    /**
     * 减少库存（原子操作）
     *
     * @param rewardId 奖励ID
     * @param count 减少数量
     * @return 是否成功
     */
    boolean decreaseStock(Long rewardId, Integer count);

    /**
     * 创建兑换记录
     *
     * @param record 兑换记录
     * @return 保存后的记录
     */
    RewardClaimRecord createClaimRecord(RewardClaimRecord record);

    /**
     * 更新兑换记录状态
     *
     * @param recordId 记录ID
     * @param status 状态
     * @return 是否成功
     */
    boolean updateClaimStatus(Long recordId, Integer status);

    /**
     * 查询用户兑换记录
     *
     * @param userId 用户ID
     * @param limit 限制数量
     * @return 兑换记录列表
     */
    List<RewardClaimRecord> findUserClaimRecords(Long userId, int limit);

    /**
     * 查询用户兑换指定奖励的次数
     *
     * @param userId 用户ID
     * @param rewardId 奖励ID
     * @return 兑换次数
     */
    Long countUserClaimByRewardId(Long userId, Long rewardId);
}
