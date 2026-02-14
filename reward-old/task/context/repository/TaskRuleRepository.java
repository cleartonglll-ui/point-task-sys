package com.user.growth.reward.task.context.repository;

import com.user.growth.reward.task.context.domain.TaskRule;
import com.user.growth.reward.task.context.domain.UserTaskRecord;

import java.time.LocalDate;
import java.util.List;

/**
 * 任务规则仓储接口
 *
 * @author system
 * @since 1.0.0
 */
public interface TaskRuleRepository {

    /**
     * 根据 taskCode 获取任务规则
     *
     * @param taskCode 任务编码
     * @return 任务规则
     */
    TaskRule findByTaskCode(String taskCode);

    /**
     * 获取所有启用的任务规则
     *
     * @return 任务规则列表
     */
    List<TaskRule> findAllEnabled();

    /**
     * 根据任务类型获取启用的任务规则
     *
     * @param taskType 任务类型
     * @return 任务规则列表
     */
    List<TaskRule> findByTaskType(Integer taskType);

    /**
     * 获取用户今日的任务完成记录
     *
     * @param userId 用户ID
     * @param taskId 任务ID
     * @param today 今日日期
     * @return 任务完成记录
     */
    UserTaskRecord findUserTaskRecord(Long userId, Long taskId, LocalDate today);

    /**
     * 创建或更新用户任务记录
     *
     * @param record 任务完成记录
     * @return 保存后的记录
     */
    UserTaskRecord saveOrUpdateUserTaskRecord(UserTaskRecord record);

    /**
     * 获取用户今日的任务列表
     *
     * @param userId 用户ID
     * @param today 今日日期
     * @return 任务完成记录列表
     */
    List<UserTaskRecord> findUserTaskRecordsToday(Long userId, LocalDate today);
}
