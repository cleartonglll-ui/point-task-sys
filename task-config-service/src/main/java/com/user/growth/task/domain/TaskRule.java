package com.user.growth.task.domain;

/**
 * Domain object representing a task rule configuration.
 *
 * Fields are intentionally minimal for the template. A real system should
 * include fields for visibility, targeting, reward definitions, frequency
 * limits, start/end times, and more.
 */
public class TaskRule {
    private Long id;
    private String name;
    private String eventType; // which event triggers this rule
    private String expression; // Aviator expression to evaluate eligibility
    private Integer points; // points to grant if the rule matches

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public String getExpression() {
        return expression;
    }

    public void setExpression(String expression) {
        this.expression = expression;
    }

    public Integer getPoints() {
        return points;
    }

    public void setPoints(Integer points) {
        this.points = points;
    }
}
