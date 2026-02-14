package com.user.growth.calculation;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class EventCalculationApplication {
    public static void main(String[] args) {
        SpringApplication.run(EventCalculationApplication.class, args);
    }
}
package com.user.growth.calculation;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.kafka.annotation.EnableKafka;

/**
 * 事件计算服务启动类
 * 监听MQ，根据任务配置进行积分计算，支持高并发防超发
 */
@SpringBootApplication
@EnableKafka
public class EventCalculationApplication {

    public static void main(String[] args) {
        SpringApplication.run(EventCalculationApplication.class, args);
    }
}
