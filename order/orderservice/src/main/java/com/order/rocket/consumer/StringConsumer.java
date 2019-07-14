package com.order.rocket.consumer;

import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.stereotype.Service;

/**
 * RocketMQMessageListener
 * @author admin
 */
@Slf4j
@Service
@RocketMQMessageListener(topic = "${demo.rocketmq.topic}", consumerGroup = "string_consumer")
public class StringConsumer implements RocketMQListener<String> {
    @Override
    public void onMessage(String message) {
        log.info("------- StringConsumer received: {}", message);
    }
}
