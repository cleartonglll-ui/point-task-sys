package com.user.growth.reward.point.context.repository.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.user.growth.reward.point.context.domain.PointAccount;
import com.user.growth.reward.point.context.domain.PointDetail;
import com.user.growth.reward.point.context.entity.PointAccountDO;
import com.user.growth.reward.point.context.entity.PointDetailDO;
import com.user.growth.reward.point.context.mapper.PointAccountMapper;
import com.user.growth.reward.point.context.mapper.PointDetailMapper;
import com.user.growth.reward.point.context.repository.PointAccountRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 积分账户仓储实现
 *
 * @author system
 * @since 1.0.0
 */
@Repository
public class PointAccountRepositoryImpl implements PointAccountRepository {

    @Autowired
    private PointAccountMapper pointAccountMapper;

    @Autowired
    private PointDetailMapper pointDetailMapper;

    @Override
    public PointAccount findByUserId(Long userId) {
        LambdaQueryWrapper<PointAccountDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(PointAccountDO::getUserId, userId);
        PointAccountDO accountDO = pointAccountMapper.selectOne(wrapper);
        return convertToDomain(accountDO);
    }

    @Override
    public PointAccount createAccount(PointAccount account) {
        if (account == null || account.getUserId() == null) {
            return null;
        }
        PointAccountDO accountDO = convertToDO(account);
        pointAccountMapper.insert(accountDO);
        account.setId(accountDO.getId());
        return account;
    }

    @Override
    public boolean updateAccount(PointAccount account) {
        if (account == null || account.getId() == null) {
            return false;
        }
        PointAccountDO accountDO = convertToDO(account);
        return pointAccountMapper.updateById(accountDO) > 0;
    }

    @Override
    public boolean recordPointDetail(PointDetail detail) {
        if (detail == null) {
            return false;
        }
        PointDetailDO detailDO = convertDetailToDO(detail);
        return pointDetailMapper.insert(detailDO) > 0;
    }

    @Override
    public int batchRecordPointDetails(List<PointDetail> details) {
        if (details == null || details.isEmpty()) {
            return 0;
        }
        List<PointDetailDO> detailDOList = details.stream()
                .map(this::convertDetailToDO)
                .collect(Collectors.toList());
        return detailDOList.stream()
                .mapToInt(detailDO -> pointDetailMapper.insert(detailDO))
                .sum();
    }

    @Override
    public List<PointDetail> findUserPointDetails(Long userId, int limit) {
        LambdaQueryWrapper<PointDetailDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(PointDetailDO::getUserId, userId)
                .orderByDesc(PointDetailDO::getCreatedAt)
                .last("LIMIT " + limit);
        List<PointDetailDO> detailDOList = pointDetailMapper.selectList(wrapper);
        return detailDOList.stream()
                .map(this::convertDetailToDomain)
                .collect(Collectors.toList());
    }

    /**
     * 转换 DO 为领域模型
     */
    private PointAccount convertToDomain(PointAccountDO accountDO) {
        if (accountDO == null) {
            return null;
        }
        PointAccount account = new PointAccount();
        BeanUtils.copyProperties(accountDO, account);
        return account;
    }

    /**
     * 转换领域模型为 DO
     */
    private PointAccountDO convertToDO(PointAccount account) {
        if (account == null) {
            return null;
        }
        PointAccountDO accountDO = new PointAccountDO();
        BeanUtils.copyProperties(account, accountDO);
        return accountDO;
    }

    /**
     * 转换 DO 为领域模型
     */
    private PointDetail convertDetailToDomain(PointDetailDO detailDO) {
        if (detailDO == null) {
            return null;
        }
        PointDetail detail = new PointDetail();
        BeanUtils.copyProperties(detailDO, detail);
        if (detailDO.getPointType() != null) {
            detail.setPointType(
                com.user.growth.reward.point.context.enums.PointType.getByCode(detailDO.getPointType())
            );
        }
        return detail;
    }

    /**
     * 转换领域模型为 DO
     */
    private PointDetailDO convertDetailToDO(PointDetail detail) {
        if (detail == null) {
            return null;
        }
        PointDetailDO detailDO = new PointDetailDO();
        BeanUtils.copyProperties(detail, detailDO);
        if (detail.getPointType() != null) {
            detailDO.setPointType(detail.getPointType().getCode());
        }
        return detailDO;
    }
}
