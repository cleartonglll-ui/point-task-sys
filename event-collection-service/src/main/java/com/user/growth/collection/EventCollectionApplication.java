package com.user.growth.collection;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Event Collection Service - Spring Boot entry point.
 *
 * This microservice is responsible for receiving raw behavior events from the
 * client-side SDK, performing light validation/normalization, persisting to
 * MongoDB, and publishing messages to Kafka for downstream consumers.
 */
@SpringBootApplication
public class EventCollectionApplication {
    public static void main(String[] args) {
        SpringApplication.run(EventCollectionApplication.class, args);
    }
}
package com.user.growth.collection;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.kafka.annotation.EnableKafka;

/**
 * 事件采集服务启动类
 * 负责采集用户行为事件，清洗标准化后存入MongoDB并发送到Kafka
 */
@SpringBootApplication
@EnableKafka
public class EventCollectionApplication {

    public static void main(String[] args) {
        SpringApplication.run(EventCollectionApplication.class, args);
    }
}
