package com.vivo.pointtask.stage3.repository;

import com.vivo.pointtask.stage3.entity.UserTask;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 【演化阶段三】用户任务记录数据访问层
 * 
 * @author vivo积分任务系统
 * @version 3.0.0
 */
public class UserTaskRepository {
    
    private Map<Long, UserTask> userTaskStore = new HashMap<>();
    private Long idGenerator = 1L;
    
    public UserTask findById(Long id) {
        return userTaskStore.get(id);
    }
    
    public UserTask findByUserIdAndTaskId(Long userId, Long taskId) {
        return userTaskStore.values().stream()
                .filter(ut -> ut.getUserId().equals(userId) && ut.getTaskId().equals(taskId))
                .findFirst()
                .orElse(null);
    }
    
    public List<UserTask> findByUserId(Long userId) {
        return userTaskStore.values().stream()
                .filter(ut -> ut.getUserId().equals(userId))
                .collect(Collectors.toList());
    }
    
    public UserTask save(UserTask userTask) {
        if (userTask.getId() == null) {
            userTask.setId(idGenerator++);
        }
        userTaskStore.put(userTask.getId(), userTask);
        return userTask;
    }
    
    public void update(UserTask userTask) {
        userTaskStore.put(userTask.getId(), userTask);
    }
    
    public List<UserTask> findAll() {
        return new ArrayList<>(userTaskStore.values());
    }
}
