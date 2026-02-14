package com.user.growth.collection.domain.repository;

import com.user.growth.collection.domain.aggregate.BehaviorEvent;

import java.util.List;

/**
 * 行为事件仓储接口
 * 领域层：定义仓储契约，不依赖具体实现
 * 
 * 该接口定义了行为事件的持久化操作契约，包括保存、查询、更新等基本操作，
 * 遵循领域驱动设计原则，将领域逻辑与基础设施实现分离。
 */
public interface IBehaviorEventRepository {
    
    /**
     * 保存事件
     * 将行为事件对象持久化到数据存储中
     * 
     * @param event 要保存的行为事件对象
     * @return 保存后的事件对象
     */
    BehaviorEvent save(BehaviorEvent event);
    
    /**
     * 根据ID查询
     * 根据事件ID从数据存储中检索行为事件
     * 
     * @param id 事件的唯一标识符
     * @return 查询到的行为事件对象，如果不存在则返回null
     */
    BehaviorEvent findById(String id);
    
    /**
     * 查询待处理事件
     * 获取指定数量的状态为待处理的行为事件
     * 
     * @param limit 查询结果的最大数量限制
     * @return 待处理事件列表
     */
    List<BehaviorEvent> findPendingEvents(int limit);
    
    /**
     * 更新事件状态
     * 更新指定事件的状态信息
     * 
     * @param id 事件的唯一标识符
     * @param status 新的状态值
     */
    void updateStatus(String id, Integer status);
}