package com.user.growth.collection.api.controller;

import com.user.growth.collection.api.dto.BehaviorEventDTO;
import com.user.growth.collection.application.service.EventCollectionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 行为事件采集接口
 * 接口层：处理HTTP请求，参数校验，DTO转换
 * 
 * 该控制器提供用户行为事件的采集接口，接收来自客户端SDK的事件数据，
 * 验证请求参数后委托给应用服务进行处理，确保事件数据能够被正确采集和处理。
 */
@Slf4j
@RestController
@RequestMapping("/api/v1/events")
public class BehaviorEventController {

    @Autowired
    private EventCollectionService eventCollectionService;

    /**
     * 采集单个事件
     * 接收客户端SDK上报的用户行为事件数据
     * 
     * @param eventDTO 包含事件信息的数据传输对象
     * @return 采集结果状态
     */
    @PostMapping("/collect")
    public String collectEvent(@RequestBody BehaviorEventDTO eventDTO) {
        log.info("收到事件采集请求: eventType={}, userId={}", 
                eventDTO.getEventType(), eventDTO.getUserId());
        
        eventCollectionService.collectEvent(eventDTO);
        return "success";
    }

    /**
     * 健康检查
     * 用于检查服务运行状态
     * 
     * @return 服务健康状态
     */
    @GetMapping("/health")
    public String health() {
        return "OK";
    }
}