package com.vivo.pointtask.stage1.repository;

import com.vivo.pointtask.stage1.entity.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 【演化阶段一】任务数据访问层
 * 
 * 演化说明：
 * 模拟数据库存储，提供基本的CRUD操作。
 * 在贫血模型中，Repository只负责数据存取，不包含业务逻辑。
 * 
 * @author vivo积分任务系统
 * @version 1.0.0
 */
public class TaskRepository {
    
    /**
     * 内存存储，模拟数据库
     */
    private Map<Long, Task> taskStore = new HashMap<>();
    private Long idGenerator = 1L;
    
    /**
     * 根据ID查询任务
     * 
     * @param id 任务ID
     * @return 任务实体
     */
    public Task findById(Long id) {
        return taskStore.get(id);
    }
    
    /**
     * 查询所有任务
     * 
     * @return 任务列表
     */
    public List<Task> findAll() {
        return new ArrayList<>(taskStore.values());
    }
    
    /**
     * 保存任务
     * 
     * @param task 任务实体
     * @return 保存后的任务
     */
    public Task save(Task task) {
        if (task.getId() == null) {
            task.setId(idGenerator++);
        }
        taskStore.put(task.getId(), task);
        return task;
    }
    
    /**
     * 更新任务
     * 
     * @param task 任务实体
     */
    public void update(Task task) {
        taskStore.put(task.getId(), task);
    }
    
    /**
     * 删除任务
     * 
     * @param id 任务ID
     */
    public void delete(Long id) {
        taskStore.remove(id);
    }
}
