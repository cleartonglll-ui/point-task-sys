package com.vivo.pointtask.stage2.repository;

import com.vivo.pointtask.stage2.entity.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 【演化阶段二】任务数据访问层
 * 
 * @author vivo积分任务系统
 * @version 2.0.0
 */
public class TaskRepository {
    
    private Map<Long, Task> taskStore = new HashMap<>();
    private Long idGenerator = 1L;
    
    public Task findById(Long id) {
        return taskStore.get(id);
    }
    
    public List<Task> findAll() {
        return new ArrayList<>(taskStore.values());
    }
    
    /**
     * 【演化新增】根据行为ID查询关联的任务
     * 
     * @param behaviorId 行为ID
     * @return 任务列表
     */
    public List<Task> findByBehaviorId(Long behaviorId) {
        return taskStore.values().stream()
                .filter(task -> behaviorId.equals(task.getBehaviorId()))
                .collect(Collectors.toList());
    }
    
    public Task save(Task task) {
        if (task.getId() == null) {
            task.setId(idGenerator++);
        }
        taskStore.put(task.getId(), task);
        return task;
    }
    
    public void update(Task task) {
        taskStore.put(task.getId(), task);
    }
    
    public void delete(Long id) {
        taskStore.remove(id);
    }
}
