package com.vivo.pointtask.stage2.repository;

import com.vivo.pointtask.stage2.entity.Behavior;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 【演化阶段二】行为数据访问层
 * 
 * @author vivo积分任务系统
 * @version 2.0.0
 */
public class BehaviorRepository {
    
    private Map<Long, Behavior> behaviorStore = new HashMap<>();
    private Long idGenerator = 1L;
    
    public Behavior findById(Long id) {
        return behaviorStore.get(id);
    }
    
    public List<Behavior> findAll() {
        return new ArrayList<>(behaviorStore.values());
    }
    
    public Behavior save(Behavior behavior) {
        if (behavior.getId() == null) {
            behavior.setId(idGenerator++);
        }
        behaviorStore.put(behavior.getId(), behavior);
        return behavior;
    }
    
    public void update(Behavior behavior) {
        behaviorStore.put(behavior.getId(), behavior);
    }
    
    public void delete(Long id) {
        behaviorStore.remove(id);
    }
}
