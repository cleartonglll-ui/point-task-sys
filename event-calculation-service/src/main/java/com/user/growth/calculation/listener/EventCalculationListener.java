package com.user.growth.calculation.listener;

import com.user.growth.calculation.service.EventCalculationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * Kafka listener that consumes raw events produced by the collection service.
 *
 * The listener delegates processing to the EventCalculationService which
 * applies task rules and triggers reward issuance.
 */
@Component
public class EventCalculationListener {

    private final EventCalculationService calculationService;

    @Autowired
    public EventCalculationListener(EventCalculationService calculationService) {
        this.calculationService = calculationService;
    }

    @KafkaListener(topics = "behavior-events", groupId = "event-calculation-group")
    public void onMessage(Map<String, Object> message) {
        // message is the event payload deserialized from Kafka
        calculationService.processEvent(message);
    }
}
