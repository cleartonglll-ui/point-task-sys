package com.user.growth.delivery.listener;

import com.user.growth.delivery.service.PointDeliveryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * Kafka listener that consumes reward delivery messages produced by the
 * calculation service. It delegates to PointDeliveryService to persist
 * reward/grant and to notify the user via configured channels.
 */
@Component
public class RewardDeliveryListener {

    private final PointDeliveryService deliveryService;

    @Autowired
    public RewardDeliveryListener(PointDeliveryService deliveryService) {
        this.deliveryService = deliveryService;
    }

    @KafkaListener(topics = "reward-deliveries", groupId = "reward-delivery-group")
    public void onMessage(Map<String, Object> message) {
        deliveryService.grantPoints(message);
    }
}
