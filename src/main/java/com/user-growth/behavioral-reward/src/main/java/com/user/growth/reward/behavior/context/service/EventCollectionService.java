package com.user.growth.reward.behavior.context.service;

import com.user.growth.reward.behavior.context.domain.BehaviorEvent;
import com.user.growth.reward.behavior.context.domain.BehaviorEventDocument;
import com.user.growth.reward.behavior.context.repository.BehaviorEventRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * 事件采集服务（领域服务）
 *
 * 负责采集用户行为事件，进行基础校验后存储并发送到 MQ
 *
 * @author system
 * @since 1.0.0
 */
public class EventCollectionService {

    private final BehaviorEventRepository behaviorEventRepository;

    public EventCollectionService(BehaviorEventRepository behaviorEventRepository) {
        this.behaviorEventRepository = behaviorEventRepository;
    }

    /**
     * 采集单个行为事件
     *
     * @param event 行为事件
     * @return 是否采集成功
     */
    public boolean collectEvent(BehaviorEvent event) {
        if (event == null) {
            return false;
        }

        // 1. 基础校验
        if (!validateEvent(event)) {
            return false;
        }

        // 2. 补充缺失字段
        enrichEvent(event);

        // 3. 保存到 MongoDB
        BehaviorEventDocument savedDoc = behaviorEventRepository.save(event);

        // 4. 发送到 MQ（由应用层处理）
        // 这里只负责采集和持久化，发送 MQ 交给应用层

        return savedDoc != null && savedDoc.getId() != null;
    }

    /**
     * 批量采集行为事件
     *
     * @param events 事件列表
     * @return 成功采集的数量
     */
    public int batchCollectEvents(List<BehaviorEvent> events) {
        if (events == null || events.isEmpty()) {
            return 0;
        }

        // 过滤无效事件
        List<BehaviorEvent> validEvents = events.stream()
                .filter(this::validateEvent)
                .peek(this::enrichEvent)
                .collect(java.util.stream.Collectors.toList());

        if (validEvents.isEmpty()) {
            return 0;
        }

        // 批量保存
        List<BehaviorEventDocument> savedDocs = behaviorEventRepository.saveAll(validEvents);

        return savedDocs.size();
    }

    /**
     * 事件基础校验
     *
     * @param event 行为事件
     * @return 是否有效
     */
    private boolean validateEvent(BehaviorEvent event) {
        if (event.getUserId() == null) {
            return false;
        }
        if (event.getEventType() == null) {
            return false;
        }
        if (event.getEventTime() == null) {
            event.setEventTime(LocalDateTime.now());
        }
        if (event.getEventId() == null || event.getEventId().isEmpty()) {
            event.setEventId(generateEventId(event.getUserId(), event.getEventType()));
        }
        return true;
    }

    /**
     * 补充事件缺失字段
     *
     * @param event 行为事件
     */
    private void enrichEvent(BehaviorEvent event) {
        if (event.getStatus() == null) {
            event.setStatus(0); // 待处理
        }
        if (event.getPointAwarded() == null) {
            event.setPointAwarded(false);
        }
    }

    /**
     * 生成事件ID
     *
     * @param userId 用户ID
     * @param eventType 事件类型
     * @return 事件ID
     */
    private String generateEventId(Long userId, com.user.growth.reward.behavior.context.enums.EventType eventType) {
        return String.format("%s_%s_%d_%d",
                eventType.getTaskCode(),
                userId,
                System.currentTimeMillis(),
                (int) (Math.random() * 1000));
    }
}
