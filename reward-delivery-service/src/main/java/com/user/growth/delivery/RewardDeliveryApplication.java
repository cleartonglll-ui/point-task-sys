package com.user.growth.delivery;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class RewardDeliveryApplication {
    public static void main(String[] args) {
        SpringApplication.run(RewardDeliveryApplication.class, args);
    }
}
package com.user.growth.delivery;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.kafka.annotation.EnableKafka;

/**
 * 奖励触达服务启动类
 * 负责奖励发放、触达( Push / 短信 / MQ透传 )
 */
@SpringBootApplication
@EnableKafka
public class RewardDeliveryApplication {

    public static void main(String[] args) {
        SpringApplication.run(RewardDeliveryApplication.class, args);
    }
}
