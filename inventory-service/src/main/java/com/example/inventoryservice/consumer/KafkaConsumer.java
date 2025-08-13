package com.example.inventoryservice.consumer;

import com.example.inventoryservice.dto.OrderEvent;
import com.example.inventoryservice.entity.Inventory;
import com.example.inventoryservice.producer.MessageProducer;
import com.example.inventoryservice.repository.InventoryRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class KafkaConsumer {

    private final InventoryRepository inventoryRepository;
    private final ObjectMapper objectMapper;
    private final MessageProducer messageProducer;

    @KafkaListener(topics = "order-created", groupId = "techmaster")
    public void listen(String message) {
        try {
            OrderEvent event = objectMapper.readValue(message, OrderEvent.class);
            System.out.println("Nhận đơn hàng: " + event.getOrderId());

            // Kiểm tra tồn kho
            Optional<Inventory> item = inventoryRepository.findByProductId(event.getProductId());
            if (item.isPresent()) {
                Inventory inventory = item.get();
                if (inventory.getQuantityAvailable() >= event.getQuantity()) {
                    inventory.setQuantityAvailable(inventory.getQuantityAvailable() - event.getQuantity());
                    inventoryRepository.save(inventory);
                    System.out.println("✔ Đã giữ hàng cho order " + event.getOrderId());

                    // Gửi event thành công về topic
                    messageProducer.send("inventory-success", event);
                } else {
                    System.out.println("❌ Không đủ hàng cho order " + event.getOrderId());

                    // Gửi event thất bại về topic
                    messageProducer.send("inventory-failed", event);
                }
            } else {
                System.out.println("❌ Không tìm thấy sản phẩm: " + event.getProductId());
            }

        } catch (Exception e) {
            System.out.println("Lỗi xử lý message: " + e.getMessage());
        }
    }
}

