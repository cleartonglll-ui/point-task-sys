package com.user.growth.calculation.service;

import com.user.growth.task.domain.TaskRule;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * Core calculation service responsible for applying task rules to events,
 * computing rewards and invoking atomic Redis+Lua scripts to grant points.
 *
 * This is an application-layer service. In production it would use a
 * TaskRuleRepository client to fetch rules and a RedisTemplate/KafkaTemplate
 * for atomic updates and messaging.
 */
@Service
public class EventCalculationService {

    /**
     * Process a single event and compute rewards.
     *
     * @param event The incoming event payload (map form to be flexible)
     */
    public void processEvent(Map<String, Object> event) {
        // 1. Lookup task rules for event type (call task-config service)
        // 2. For each rule evaluate expression (use Aviator wrapper)
        // 3. For matching rules, call Redis+Lua to grant points atomically
        // 4. Publish reward delivery message to Kafka
    }

    public List<TaskRule> findMatchingRules(String eventType) {
        // Placeholder for rule lookup
        return List.of();
    }
}
