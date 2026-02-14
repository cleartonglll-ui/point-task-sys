package com.user.growth.collection.repository;

import com.user.growth.collection.domain.BehaviorEvent;
import org.springframework.stereotype.Repository;

/**
 * Repository abstraction for BehaviorEvent persistence.
 *
 * In a real system this would extend a Spring Data MongoRepository or use a
 * custom mapper for high-performance writes. Here we provide a simple
 * placeholder implementation that should be implemented to call MongoDB.
 */
@Repository
public class BehaviorEventRepository {
    /**
     * Persist the event into storage (MongoDB expected).
     *
     * For this template project, implementers should wire a MongoTemplate or
     * Spring Data repository and replace this method body.
     */
    public void save(BehaviorEvent event) {
        // TODO: persist to MongoDB. This is left as a stub so the project
        // compiles without requiring MongoDB during initial development.
    }
}
