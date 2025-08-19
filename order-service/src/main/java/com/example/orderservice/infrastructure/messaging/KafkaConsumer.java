package com.example.orderservice.infrastructure.messaging;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class KafkaConsumer {

    @KafkaListener(topics = "order-id", groupId = "mcs-group")
    public void listen() {

    }
}
