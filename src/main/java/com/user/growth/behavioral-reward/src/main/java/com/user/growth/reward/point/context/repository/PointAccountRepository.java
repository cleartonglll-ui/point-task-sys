package com.user.growth.reward.point.context.repository;

import com.user.growth.reward.point.context.domain.PointAccount;
import com.user.growth.reward.point.context.domain.PointDetail;

import java.util.List;

/**
 * 积分账户仓储接口
 *
 * @author system
 * @since 1.0.0
 */
public interface PointAccountRepository {

    /**
     * 根据 userId 获取积分账户
     *
     * @param userId 用户ID
     * @return 积分账户
     */
    PointAccount findByUserId(Long userId);

    /**
     * 创建积分账户
     *
     * @param account 积分账户
     * @return 创建的账户
     */
    PointAccount createAccount(PointAccount account);

    /**
     * 更新积分账户
     *
     * @param account 积分账户
     * @return 是否更新成功
     */
    boolean updateAccount(PointAccount account);

    /**
     * 记录积分流水
     *
     * @param detail 积分明细
     * @return 是否记录成功
     */
    boolean recordPointDetail(PointDetail detail);

    /**
     * 批量记录积分流水
     *
     * @param details 积分明细列表
     * @return 记录成功的数量
     */
    int batchRecordPointDetails(List<PointDetail> details);

    /**
     * 查询用户积分明细
     *
     * @param userId 用户ID
     * @param limit 限制数量
     * @return 积分明细列表
     */
    List<PointDetail> findUserPointDetails(Long userId, int limit);
}
