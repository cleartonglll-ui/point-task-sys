package com.user.growth.reward.point.context.service;

import com.user.growth.reward.common.exception.BusinessException;
import com.user.growth.reward.common.exception.ErrorCode;
import com.user.growth.reward.point.context.domain.PointAccount;
import com.user.growth.reward.point.context.domain.PointDetail;
import com.user.growth.reward.point.context.enums.PointType;
import com.user.growth.reward.point.context.repository.PointAccountRepository;

import java.time.LocalDateTime;

/**
 * 积分账户领域服务
 *
 * 提供积分账户的核心业务逻辑：开户、增减积分、查询等
 *
 * @author system
 * @since 1.0.0
 */
public class PointAccountService {

    private final PointAccountRepository pointAccountRepository;

    public PointAccountService(PointAccountRepository pointAccountRepository) {
        this.pointAccountRepository = pointAccountRepository;
    }

    /**
     * 获取或创建用户积分账户
     *
     * @param userId 用户ID
     * @param userName 用户昵称
     * @return 积分账户
     */
    public PointAccount getOrCreateAccount(Long userId, String userName) {
        // 查询现有账户
        PointAccount account = pointAccountRepository.findByUserId(userId);
        if (account != null) {
            return account;
        }

        // 创建新账户
        account = PointAccount.builder()
                .userId(userId)
                .userName(userName)
                .totalPoints(0)
                .availablePoints(0)
                .frozenPoints(0)
                .totalEarned(0)
                .totalSpent(0)
                .level(1)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        return pointAccountRepository.createAccount(account);
    }

    /**
     * 增加积分
     *
     * @param userId 用户ID
     * @param amount 积分数量
     * @param bizType 业务类型
     * @param bizId 业务ID
     * @param remark 备注
     * @return 变动后的积分
     */
    public Integer addPoints(Long userId, Integer amount, String bizType, String bizId, String remark) {
        if (amount == null || amount <= 0) {
            throw new BusinessException(ErrorCode.PARAM_ERROR, "积分数量必须大于0");
        }

        // 获取或创建账户
        PointAccount account = pointAccountRepository.findByUserId(userId);
        if (account == null) {
            throw new BusinessException(ErrorCode.USER_ACCOUNT_NOT_FOUND);
        }

        // 记录变动前积分
        Integer beforePoints = account.getAvailablePoints() != null ? account.getAvailablePoints() : 0;

        // 增加积分
        account.addPoints(amount);

        // 更新账户
        pointAccountRepository.updateAccount(account);

        // 记录流水
        PointDetail detail = PointDetail.builder()
                .userId(userId)
                .pointType(PointType.EARN)
                .bizType(bizType)
                .bizId(bizId)
                .pointAmount(amount)
                .beforePoints(beforePoints)
                .afterPoints(account.getAvailablePoints())
                .remark(remark)
                .createdAt(LocalDateTime.now())
                .build();

        pointAccountRepository.recordPointDetail(detail);

        return account.getAvailablePoints();
    }

    /**
     * 扣减积分
     *
     * @param userId 用户ID
     * @param amount 积分数量
     * @param bizType 业务类型
     * @param bizId 业务ID
     * @param remark 备注
     * @return 变动后的积分
     */
    public Integer deductPoints(Long userId, Integer amount, String bizType, String bizId, String remark) {
        if (amount == null || amount <= 0) {
            throw new BusinessException(ErrorCode.PARAM_ERROR, "积分数量必须大于0");
        }

        // 获取账户
        PointAccount account = pointAccountRepository.findByUserId(userId);
        if (account == null) {
            throw new BusinessException(ErrorCode.USER_ACCOUNT_NOT_FOUND);
        }

        // 检查余额
        if (!account.isBalanceEnough(amount)) {
            throw new BusinessException(ErrorCode.POINT_NOT_ENOUGH);
        }

        // 记录变动前积分
        Integer beforePoints = account.getAvailablePoints();

        // 扣减积分
        account.deductPoints(amount);

        // 更新账户
        pointAccountRepository.updateAccount(account);

        // 记录流水
        PointDetail detail = PointDetail.builder()
                .userId(userId)
                .pointType(PointType.SPEND)
                .bizType(bizType)
                .bizId(bizId)
                .pointAmount(-amount)
                .beforePoints(beforePoints)
                .afterPoints(account.getAvailablePoints())
                .remark(remark)
                .createdAt(LocalDateTime.now())
                .build();

        pointAccountRepository.recordPointDetail(detail);

        return account.getAvailablePoints();
    }

    /**
     * 查询账户
     *
     * @param userId 用户ID
     * @return 积分账户
     */
    public PointAccount getAccount(Long userId) {
        PointAccount account = pointAccountRepository.findByUserId(userId);
        if (account == null) {
            throw new BusinessException(ErrorCode.USER_ACCOUNT_NOT_FOUND);
        }
        return account;
    }

    /**
     * 查询用户积分明细
     *
     * @param userId 用户ID
     * @param limit 限制数量
     * @return 积分明细列表
     */
    public java.util.List<PointDetail> getPointDetails(Long userId, int limit) {
        return pointAccountRepository.findUserPointDetails(userId, limit);
    }
}
