package com.user.growth.task.controller;

import com.user.growth.task.domain.TaskRule;
import com.user.growth.task.service.TaskRuleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller exposing CRUD endpoints for task rules.
 */
@RestController
@RequestMapping("/api/v1/tasks")
public class TaskConfigController {

    private final TaskRuleService taskRuleService;

    @Autowired
    public TaskConfigController(TaskRuleService taskRuleService) {
        this.taskRuleService = taskRuleService;
    }

    @PostMapping
    public ResponseEntity<TaskRule> createTask(@RequestBody TaskRule rule) {
        TaskRule created = taskRuleService.createTask(rule);
        return ResponseEntity.ok(created);
    }

    @GetMapping("/event-type/{eventType}")
    public ResponseEntity<List<TaskRule>> findByEventType(@PathVariable String eventType) {
        return ResponseEntity.ok(taskRuleService.findByEventType(eventType));
    }

    @GetMapping("/health")
    public ResponseEntity<String> health() {
        return ResponseEntity.ok("ok");
    }
}
