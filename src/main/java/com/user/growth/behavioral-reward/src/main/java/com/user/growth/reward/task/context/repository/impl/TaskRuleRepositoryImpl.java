package com.user.growth.reward.task.context.repository.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.user.growth.reward.task.context.domain.TaskRule;
import com.user.growth.reward.task.context.domain.UserTaskRecord;
import com.user.growth.reward.task.context.entity.TaskRuleDO;
import com.user.growth.reward.task.context.entity.UserTaskRecordDO;
import com.user.growth.reward.task.context.mapper.TaskRuleMapper;
import com.user.growth.reward.task.context.mapper.UserTaskRecordMapper;
import com.user.growth.reward.task.context.repository.TaskRuleRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 任务规则仓储实现
 *
 * @author system
 * @since 1.0.0
 */
@Repository
public class TaskRuleRepositoryImpl implements TaskRuleRepository {

    @Autowired
    private TaskRuleMapper taskRuleMapper;

    @Autowired
    private UserTaskRecordMapper userTaskRecordMapper;

    @Override
    public TaskRule findByTaskCode(String taskCode) {
        LambdaQueryWrapper<TaskRuleDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(TaskRuleDO::getTaskCode, taskCode);
        TaskRuleDO taskRuleDO = taskRuleMapper.selectOne(wrapper);
        return convertToDomain(taskRuleDO);
    }

    @Override
    public List<TaskRule> findAllEnabled() {
        LambdaQueryWrapper<TaskRuleDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(TaskRuleDO::getStatus, 1)
                .orderByDesc(TaskRuleDO::getPriority);
        List<TaskRuleDO> taskRuleDOList = taskRuleMapper.selectList(wrapper);
        return taskRuleDOList.stream()
                .map(this::convertToDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<TaskRule> findByTaskType(Integer taskType) {
        LambdaQueryWrapper<TaskRuleDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(TaskRuleDO::getTaskType, taskType)
                .eq(TaskRuleDO::getStatus, 1)
                .orderByDesc(TaskRuleDO::getPriority);
        List<TaskRuleDO> taskRuleDOList = taskRuleMapper.selectList(wrapper);
        return taskRuleDOList.stream()
                .map(this::convertToDomain)
                .collect(Collectors.toList());
    }

    @Override
    public UserTaskRecord findUserTaskRecord(Long userId, Long taskId, LocalDate today) {
        LambdaQueryWrapper<UserTaskRecordDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(UserTaskRecordDO::getUserId, userId)
                .eq(UserTaskRecordDO::getTaskId, taskId)
                .eq(UserTaskRecordDO::getCompleteDate, today);
        UserTaskRecordDO recordDO = userTaskRecordMapper.selectOne(wrapper);
        return convertRecordToDomain(recordDO);
    }

    @Override
    public UserTaskRecord saveOrUpdateUserTaskRecord(UserTaskRecord record) {
        if (record == null || record.getUserId() == null || record.getTaskId() == null) {
            return null;
        }

        // 先查询是否存在记录
        LocalDate today = record.getCompleteDate() != null ? record.getCompleteDate() : LocalDate.now();
        UserTaskRecord existing = findUserTaskRecord(record.getUserId(), record.getTaskId(), today);

        if (existing != null) {
            // 更新现有记录
            existing.incrementCompleteCount(record.getCompleteCount() != null ? record.getCompleteCount() : 1);
            existing.addAwardedPoints(record.getPointAwarded() != null ? record.getPointAwarded() : 0);

            UserTaskRecordDO recordDO = convertRecordToDO(existing);
            userTaskRecordMapper.updateById(recordDO);
            return existing;
        } else {
            // 创建新记录
            if (record.getCompleteCount() == null) {
                record.setCompleteCount(1);
            }
            if (record.getPointAwarded() == null) {
                record.setPointAwarded(0);
            }
            if (record.getCompleteDate() == null) {
                record.setCompleteDate(today);
            }

            UserTaskRecordDO recordDO = convertRecordToDO(record);
            userTaskRecordMapper.insert(recordDO);
            record.setId(recordDO.getId());
            return record;
        }
    }

    @Override
    public List<UserTaskRecord> findUserTaskRecordsToday(Long userId, LocalDate today) {
        LambdaQueryWrapper<UserTaskRecordDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(UserTaskRecordDO::getUserId, userId)
                .eq(UserTaskRecordDO::getCompleteDate, today);
        List<UserTaskRecordDO> recordDOList = userTaskRecordMapper.selectList(wrapper);
        return recordDOList.stream()
                .map(this::convertRecordToDomain)
                .collect(Collectors.toList());
    }

    /**
     * 转换 DO 为领域模型
     */
    private TaskRule convertToDomain(TaskRuleDO taskRuleDO) {
        if (taskRuleDO == null) {
            return null;
        }
        TaskRule taskRule = new TaskRule();
        BeanUtils.copyProperties(taskRuleDO, taskRule);
        if (taskRuleDO.getTaskType() != null) {
            taskRule.setTaskType(
                com.user.growth.reward.task.context.enums.TaskType.getByCode(taskRuleDO.getTaskType())
            );
        }
        return taskRule;
    }

    /**
     * 转换领域模型为 DO
     */
    private TaskRuleDO convertToDO(TaskRule taskRule) {
        if (taskRule == null) {
            return null;
        }
        TaskRuleDO taskRuleDO = new TaskRuleDO();
        BeanUtils.copyProperties(taskRule, taskRuleDO);
        if (taskRule.getTaskType() != null) {
            taskRuleDO.setTaskType(taskRule.getTaskType().getCode());
        }
        return taskRuleDO;
    }

    /**
     * 转换记录 DO 为领域模型
     */
    private UserTaskRecord convertRecordToDomain(UserTaskRecordDO recordDO) {
        if (recordDO == null) {
            return null;
        }
        UserTaskRecord record = new UserTaskRecord();
        BeanUtils.copyProperties(recordDO, record);
        return record;
    }

    /**
     * 转换领域模型为 DO
     */
    private UserTaskRecordDO convertRecordToDO(UserTaskRecord record) {
        if (record == null) {
            return null;
        }
        UserTaskRecordDO recordDO = new UserTaskRecordDO();
        BeanUtils.copyProperties(record, recordDO);
        return recordDO;
    }
}
