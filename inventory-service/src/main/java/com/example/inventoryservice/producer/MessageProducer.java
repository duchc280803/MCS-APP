package com.example.inventoryservice.producer;

import com.example.inventoryservice.dto.OrderEvent;
import lombok.AllArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class MessageProducer {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public void send(String topic, OrderEvent event) {
        kafkaTemplate.send(topic, event);
    }
}
