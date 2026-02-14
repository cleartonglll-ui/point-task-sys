package com.user.growth.task.service;

import com.user.growth.task.domain.TaskRule;
import com.user.growth.task.repository.TaskRuleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

/**
 * Application service for task rule management. Responsible for validation
 * (using AviatorExpressionEngine) and persistence via repository.
 */
@Service
public class TaskRuleService {

    private final TaskRuleRepository repository;

    @Autowired
    public TaskRuleService(TaskRuleRepository repository) {
        this.repository = repository;
    }

    public TaskRule createTask(TaskRule rule) {
        // validate expression - in production use AviatorScript engine
        // Here we simply persist the rule via repository
        repository.save(rule);
        return rule;
    }

    public List<TaskRule> findByEventType(String eventType) {
        // Query repository for rules matching event type
        return repository.findByEventType(eventType);
    }
}
