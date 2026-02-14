package com.vivo.pointtask.stage1.repository;

import com.vivo.pointtask.stage1.entity.UserTask;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 【演化阶段一】用户任务记录数据访问层
 * 
 * 演化说明：
 * 模拟数据库存储，提供基本的CRUD操作。
 * 
 * @author vivo积分任务系统
 * @version 1.0.0
 */
public class UserTaskRepository {
    
    /**
     * 内存存储，模拟数据库
     */
    private Map<Long, UserTask> userTaskStore = new HashMap<>();
    private Long idGenerator = 1L;
    
    /**
     * 根据ID查询
     * 
     * @param id 记录ID
     * @return 用户任务记录
     */
    public UserTask findById(Long id) {
        return userTaskStore.get(id);
    }
    
    /**
     * 根据用户ID和任务ID查询
     * 
     * @param userId 用户ID
     * @param taskId 任务ID
     * @return 用户任务记录
     */
    public UserTask findByUserIdAndTaskId(Long userId, Long taskId) {
        return userTaskStore.values().stream()
                .filter(ut -> ut.getUserId().equals(userId) && ut.getTaskId().equals(taskId))
                .findFirst()
                .orElse(null);
    }
    
    /**
     * 根据用户ID查询所有记录
     * 
     * @param userId 用户ID
     * @return 用户任务记录列表
     */
    public List<UserTask> findByUserId(Long userId) {
        return userTaskStore.values().stream()
                .filter(ut -> ut.getUserId().equals(userId))
                .collect(Collectors.toList());
    }
    
    /**
     * 保存记录
     * 
     * @param userTask 用户任务记录
     * @return 保存后的记录
     */
    public UserTask save(UserTask userTask) {
        if (userTask.getId() == null) {
            userTask.setId(idGenerator++);
        }
        userTaskStore.put(userTask.getId(), userTask);
        return userTask;
    }
    
    /**
     * 更新记录
     * 
     * @param userTask 用户任务记录
     */
    public void update(UserTask userTask) {
        userTaskStore.put(userTask.getId(), userTask);
    }
    
    /**
     * 查询所有记录
     * 
     * @return 所有用户任务记录
     */
    public List<UserTask> findAll() {
        return new ArrayList<>(userTaskStore.values());
    }
}
