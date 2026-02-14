package com.user.growth.collection.application.service;

import com.user.growth.collection.api.dto.BehaviorEventDTO;
import com.user.growth.collection.domain.aggregate.BehaviorEvent;
import com.user.growth.collection.domain.repository.IBehaviorEventRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 事件采集应用服务
 * 应用层：编排领域服务，处理事务边界
 * 
 * 该应用服务负责协调行为事件的采集流程，包括接收DTO数据、转换为领域对象、
 * 执行领域校验、保存到仓储等操作，确保整个采集流程的完整性和一致性。
 */
@Slf4j
@Service
public class EventCollectionService {

    @Autowired
    private IBehaviorEventRepository behaviorEventRepository;

    /**
     * 采集事件 - 应用层编排方法
     * 处理完整的事件采集流程，包括数据转换、校验、保存等步骤
     * 
     * @param eventDTO 从接口层传入的行为事件数据传输对象
     */
    public void collectEvent(BehaviorEventDTO eventDTO) {
        log.info("正在采集事件: userId={}, eventType={}", eventDTO.getUserId(), eventDTO.getEventType());

        // 1. DTO转领域对象
        BehaviorEvent event = convertToDomain(eventDTO);

        // 2. 领域校验
        if (!event.isValid()) {
            throw new IllegalArgumentException("事件数据无效");
        }

        // 3. 保存到仓储
        behaviorEventRepository.save(event);

        // 4. 标记为已处理
        event.markAsProcessed();

        log.info("事件采集完成: id={}", event.getId());
    }

    /**
     * DTO转领域对象
     * 将接口层的数据传输对象转换为领域层的聚合根对象
     * 
     * @param dto 从接口层传入的数据传输对象
     * @return 转换后的领域对象
     */
    private BehaviorEvent convertToDomain(BehaviorEventDTO dto) {
        BehaviorEvent event = new BehaviorEvent();
        event.setEventId(dto.getEventId());
        event.setUserId(dto.getUserId());
        event.setEventType(dto.getEventType());
        event.setSource(dto.getSource());
        event.setEventTime(dto.getEventTime());
        event.setEventData(dto.getEventData());
        event.setDeviceId(dto.getDeviceId());
        event.setIpAddress(dto.getIpAddress());
        event.setStatus(0); // 待处理
        return event;
    }
}