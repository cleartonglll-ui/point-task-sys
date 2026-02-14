package com.user.growth.reward.task.context.service;

import com.user.growth.reward.behavior.context.domain.BehaviorEvent;
import com.user.growth.reward.behavior.context.enums.EventType;
import com.user.growth.reward.common.exception.BusinessException;
import com.user.growth.reward.common.exception.ErrorCode;
import com.user.growth.reward.task.context.domain.TaskRule;
import com.user.growth.reward.task.context.domain.UserTaskRecord;
import com.user.growth.reward.task.context.repository.TaskRuleRepository;

import java.time.LocalDate;

/**
 * 任务计算服务（事件计算服务）
 *
 * 根据行为事件计算应发放的积分
 *
 * @author system
 * @since 1.0.0
 */
public class TaskCalculationService {

    private final TaskRuleRepository taskRuleRepository;

    public TaskCalculationService(TaskRuleRepository taskRuleRepository) {
        this.taskRuleRepository = taskRuleRepository;
    }

    /**
     * 计算事件应奖励的积分
     *
     * @param event 行为事件
     * @return 应奖励的积分数量，0 表示不奖励
     */
    public Integer calculatePointAward(BehaviorEvent event) {
        if (event == null || event.getEventType() == null) {
            throw new BusinessException(ErrorCode.EVENT_TYPE_INVALID);
        }

        // 1. 查询任务规则
        String taskCode = event.getEventType().getTaskCode();
        TaskRule taskRule = taskRuleRepository.findByTaskCode(taskCode);

        if (taskRule == null) {
            throw new BusinessException(ErrorCode.TASK_NOT_FOUND);
        }

        // 2. 校验任务状态
        if (!taskRule.isEnabled()) {
            throw new BusinessException(ErrorCode.TASK_DISABLED);
        }

        if (!taskRule.isValid()) {
            throw new BusinessException(ErrorCode.TASK_EXPIRED);
        }

        // 3. 检查今日完成次数限制
        LocalDate today = LocalDate.now();
        UserTaskRecord record = taskRuleRepository.findUserTaskRecord(
                event.getUserId(), taskRule.getId(), today);

        if (record != null && record.getCompleteCount() >= taskRule.getDailyLimit()) {
            throw new BusinessException(ErrorCode.TASK_LIMIT_EXCEEDED);
        }

        // 4. 返回应奖励积分
        return taskRule.getPointAward();
    }

    /**
     * 记录任务完成
     *
     * @param userId 用户ID
     * @param taskCode 任务编码
     * @param awardedPoints 已奖励积分
     * @return 任务完成记录
     */
    public UserTaskRecord recordTaskCompletion(Long userId, String taskCode, Integer awardedPoints) {
        // 查询任务规则
        TaskRule taskRule = taskRuleRepository.findByTaskCode(taskCode);
        if (taskRule == null) {
            throw new BusinessException(ErrorCode.TASK_NOT_FOUND);
        }

        // 创建或更新任务记录
        UserTaskRecord record = UserTaskRecord.builder()
                .userId(userId)
                .taskId(taskRule.getId())
                .taskCode(taskRule.getTaskCode())
                .taskName(taskRule.getTaskName())
                .completeDate(LocalDate.now())
                .completeCount(1)
                .pointAwarded(awardedPoints != null ? awardedPoints : 0)
                .build();

        return taskRuleRepository.saveOrUpdateUserTaskRecord(record);
    }

    /**
     * 查询用户今日任务完成情况
     *
     * @param userId 用户ID
     * @return 今日任务记录列表
     */
    public java.util.List<UserTaskRecord> getTodayTaskRecords(Long userId) {
        return taskRuleRepository.findUserTaskRecordsToday(userId, LocalDate.now());
    }

    /**
     * 判断用户今日是否可以完成指定任务
     *
     * @param userId 用户ID
     * @param taskCode 任务编码
     * @return 是否可以完成
     */
    public boolean canCompleteTask(Long userId, String taskCode) {
        TaskRule taskRule = taskRuleRepository.findByTaskCode(taskCode);
        if (taskRule == null || !taskRule.isEnabled() || !taskRule.isValid()) {
            return false;
        }

        LocalDate today = LocalDate.now();
        UserTaskRecord record = taskRuleRepository.findUserTaskRecord(
                userId, taskRule.getId(), today);

        return record == null || record.getCompleteCount() < taskRule.getDailyLimit();
    }
}
