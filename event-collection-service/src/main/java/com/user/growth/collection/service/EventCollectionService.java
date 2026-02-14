package com.user.growth.collection.service;

import com.user.growth.collection.domain.BehaviorEvent;
import com.user.growth.collection.repository.BehaviorEventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Application service for event collection responsibilities.
 *
 * Responsibilities:
 *  - Validate and normalize incoming events
 *  - Persist to MongoDB through repository
 *  - Publish a Kafka message for downstream consumers (stubbed here)
 */
@Service
public class EventCollectionService {

    private final BehaviorEventRepository repository;

    @Autowired
    public EventCollectionService(BehaviorEventRepository repository) {
        this.repository = repository;
    }

    /**
     * Collect a single behavior event. This method performs lightweight
     * validation and persistence, then publishes an event message to Kafka.
     */
    public void collectEvent(BehaviorEvent event) {
        // Basic validation (in real world more checks would be applied)
        if (event == null || event.getUserId() == null) {
            throw new IllegalArgumentException("event or userId missing");
        }

        // Normalize or enrich event fields if needed (timestamps, source, etc.)
        if (event.getTimestamp() == null) {
            event.setTimestamp(System.currentTimeMillis());
        }

        // Persist into MongoDB via repository
        repository.save(event);

        // Publish to Kafka topic (for simplicity this is a placeholder comment).
        // Integration with a KafkaTemplate should be added in production.
    }
}
