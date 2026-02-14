package com.user.growth.collection.domain;

/**
 * Domain model representing a user behavior event.
 *
 * This is a minimal POJO used to transport event data through the collection
 * service. In a real system we would annotate this with MongoDB mapping
 * annotations (e.g. @Document) and include indexes for common queries.
 */
public class BehaviorEvent {
    private String id;
    private Long userId;
    private String eventType;
    private Long timestamp;
    private String payload; // JSON string with event-specific fields

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

    public String getPayload() {
        return payload;
    }

    public void setPayload(String payload) {
        this.payload = payload;
    }
}
