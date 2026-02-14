package com.user.growth.reward.reward.context.repository.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.user.growth.reward.reward.context.domain.RewardClaimRecord;
import com.user.growth.reward.reward.context.domain.RewardConfig;
import com.user.growth.reward.reward.context.entity.RewardClaimRecordDO;
import com.user.growth.reward.reward.context.entity.RewardConfigDO;
import com.user.growth.reward.reward.context.mapper.RewardClaimRecordMapper;
import com.user.growth.reward.reward.context.mapper.RewardConfigMapper;
import com.user.growth.reward.reward.context.repository.RewardRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 奖励仓储实现
 *
 * @author system
 * @since 1.0.0
 */
@Repository
public class RewardRepositoryImpl implements RewardRepository {

    @Autowired
    private RewardConfigMapper rewardConfigMapper;

    @Autowired
    private RewardClaimRecordMapper rewardClaimRecordMapper;

    @Override
    public List<RewardConfig> findAllOnShelf() {
        LambdaQueryWrapper<RewardConfigDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(RewardConfigDO::getStatus, 1)
                .gt(RewardConfigDO::getStock, 0)
                .orderByAsc(RewardConfigDO::getPointCost);
        List<RewardConfigDO> configDOList = rewardConfigMapper.selectList(wrapper);
        return configDOList.stream()
                .map(this::convertToDomain)
                .collect(Collectors.toList());
    }

    @Override
    public RewardConfig findById(Long id) {
        RewardConfigDO configDO = rewardConfigMapper.selectById(id);
        return convertToDomain(configDO);
    }

    @Override
    public boolean decreaseStock(Long rewardId, Integer count) {
        if (rewardId == null || count == null || count <= 0) {
            return false;
        }
        LambdaUpdateWrapper<RewardConfigDO> wrapper = new LambdaUpdateWrapper<>();
        wrapper.eq(RewardConfigDO::getId, rewardId)
                .ge(RewardConfigDO::getStock, count)
                .setSql("stock = stock - " + count);
        return rewardConfigMapper.update(null, wrapper) > 0;
    }

    @Override
    public RewardClaimRecord createClaimRecord(RewardClaimRecord record) {
        if (record == null || record.getUserId() == null || record.getRewardId() == null) {
            return null;
        }
        RewardClaimRecordDO recordDO = convertRecordToDO(record);
        rewardClaimRecordMapper.insert(recordDO);
        record.setId(recordDO.getId());
        return record;
    }

    @Override
    public boolean updateClaimStatus(Long recordId, Integer status) {
        if (recordId == null || status == null) {
            return false;
        }
        LambdaUpdateWrapper<RewardClaimRecordDO> wrapper = new LambdaUpdateWrapper<>();
        wrapper.eq(RewardClaimRecordDO::getId, recordId)
                .set(RewardClaimRecordDO::getClaimStatus, status);
        if (status == 2) {
            wrapper.set(RewardClaimRecordDO::getDeliveryTime, java.time.LocalDateTime.now());
        }
        return rewardClaimRecordMapper.update(null, wrapper) > 0;
    }

    @Override
    public List<RewardClaimRecord> findUserClaimRecords(Long userId, int limit) {
        LambdaQueryWrapper<RewardClaimRecordDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(RewardClaimRecordDO::getUserId, userId)
                .orderByDesc(RewardClaimRecordDO::getCreatedAt)
                .last("LIMIT " + limit);
        List<RewardClaimRecordDO> recordDOList = rewardClaimRecordMapper.selectList(wrapper);
        return recordDOList.stream()
                .map(this::convertRecordToDomain)
                .collect(Collectors.toList());
    }

    @Override
    public Long countUserClaimByRewardId(Long userId, Long rewardId) {
        LambdaQueryWrapper<RewardClaimRecordDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(RewardClaimRecordDO::getUserId, userId)
                .eq(RewardClaimRecordDO::getRewardId, rewardId);
        return rewardClaimRecordMapper.selectCount(wrapper);
    }

    /**
     * 转换 DO 为领域模型
     */
    private RewardConfig convertToDomain(RewardConfigDO configDO) {
        if (configDO == null) {
            return null;
        }
        RewardConfig config = new RewardConfig();
        BeanUtils.copyProperties(configDO, config);
        if (configDO.getRewardType() != null) {
            config.setRewardType(
                com.user.growth.reward.reward.context.enums.RewardType.getByCode(configDO.getRewardType())
            );
        }
        return config;
    }

    /**
     * 转换领域模型为 DO
     */
    private RewardConfigDO convertToDO(RewardConfig config) {
        if (config == null) {
            return null;
        }
        RewardConfigDO configDO = new RewardConfigDO();
        BeanUtils.copyProperties(config, configDO);
        if (config.getRewardType() != null) {
            configDO.setRewardType(config.getRewardType().getCode());
        }
        return configDO;
    }

    /**
     * 转换记录 DO 为领域模型
     */
    private RewardClaimRecord convertRecordToDomain(RewardClaimRecordDO recordDO) {
        if (recordDO == null) {
            return null;
        }
        RewardClaimRecord record = new RewardClaimRecord();
        BeanUtils.copyProperties(recordDO, record);
        return record;
    }

    /**
     * 转换领域模型为 DO
     */
    private RewardClaimRecordDO convertRecordToDO(RewardClaimRecord record) {
        if (record == null) {
            return null;
        }
        RewardClaimRecordDO recordDO = new RewardClaimRecordDO();
        BeanUtils.copyProperties(record, recordDO);
        return recordDO;
    }
}
