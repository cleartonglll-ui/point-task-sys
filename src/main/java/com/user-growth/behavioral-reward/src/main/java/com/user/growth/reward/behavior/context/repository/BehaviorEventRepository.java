package com.user.growth.reward.behavior.context.repository;

import com.user.growth.reward.behavior.context.domain.BehaviorEvent;
import com.user.growth.reward.behavior.context.domain.BehaviorEventDocument;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 行为事件仓储接口
 *
 * 定义行为事件的持久化操作
 *
 * @author system
 * @since 1.0.0
 */
public interface BehaviorEventRepository {

    /**
     * 保存行为事件到 MongoDB
     *
     * @param event 行为事件
     * @return 保存后的文档
     */
    BehaviorEventDocument save(BehaviorEvent event);

    /**
     * 根据 eventId 查询事件
     *
     * @param eventId 事件ID
     * @return 事件文档
     */
    BehaviorEventDocument findByEventId(String eventId);

    /**
     * 根据用户ID和时间范围查询事件列表
     *
     * @param userId 用户ID
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return 事件列表
     */
    List<BehaviorEventDocument> findByUserIdAndTimeRange(Long userId, LocalDateTime startTime, LocalDateTime endTime);

    /**
     * 更新事件处理状态
     *
     * @param eventId 事件ID
     * @param status 状态
     * @param processTime 处理时间
     * @param failReason 失败原因
     * @return 是否更新成功
     */
    boolean updateStatus(String eventId, Integer status, LocalDateTime processTime, String failReason);

    /**
     * 查询待处理的事件列表
     *
     * @param limit 限制数量
     * @return 待处理事件列表
     */
    List<BehaviorEventDocument> findPendingEvents(int limit);

    /**
     * 批量保存事件
     *
     * @param events 事件列表
     * @return 保存后的文档列表
     */
    List<BehaviorEventDocument> saveAll(List<BehaviorEvent> events);
}
