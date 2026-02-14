package com.user.growth.task.repository;

import com.user.growth.task.domain.TaskRule;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

/**
 * TaskRule repository stub.
 *
 * Replace with a MyBatis-Plus mapper or Spring Data JPA repository in production.
 */
@Repository
public class TaskRuleRepository {

    public void save(TaskRule rule) {
        // persist to MySQL in production
    }

    public List<TaskRule> findByEventType(String eventType) {
        // return empty list in template; replace with DB query
        return new ArrayList<>();
    }
}
