package com.user.growth.delivery.domain;

/**
 * Domain entity for user point account. In production this class should be
 * annotated with MyBatis-Plus or JPA mappings to persist to MySQL.
 */
public class PointAccount {
    private Long id;
    private Long userId;
    private Long points;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getPoints() {
        return points;
    }

    public void setPoints(Long points) {
        this.points = points;
    }
}
