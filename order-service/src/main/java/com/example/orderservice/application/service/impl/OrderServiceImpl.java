package com.example.orderservice.application.service.impl;

import com.example.orderservice.application.command.CreatedOrderCommand;
import com.example.orderservice.application.service.OrderService;
import com.example.orderservice.domain.entity.Order;
import com.example.orderservice.domain.entity.OrderItem;
import com.example.orderservice.domain.entity.OrderStatus;
import com.example.orderservice.domain.event.OrderEvent;
import com.example.orderservice.infrastructure.repository.OrderItemRepository;
import com.example.orderservice.infrastructure.repository.OrderRepository;
import com.example.orderservice.shared.CommonResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final KafkaTemplate<String, Object> jsonKafkaTemplate;
    private final OrderItemRepository orderItemRepository;
    private final RedisTemplate<String, Object> redisTemplate;

    @Override
    public CommonResponse createdOrder(CreatedOrderCommand command) {
        Long remainingInRedis = this.redisTemplate.opsForValue().decrement("product:stock:" + command.getProductId());

        if (remainingInRedis == null || remainingInRedis < 0) {
            // Rollback Redis nếu hết hàng
            if (remainingInRedis != null)
                redisTemplate.opsForValue().increment("product:stock:" + command.getProductId());
            return CommonResponse.builder().code(CommonResponse.CODE_NOT_FOUND).build();
        }

        try {
            // 2. Verify stock in Database (source of truth)
            int remainingInDB = orderRepository.countQuantityProduct(command.getProductId());
            if (remainingInDB <= 0) {
                return CommonResponse.builder().code(CommonResponse.CODE_NOT_FOUND).build();
            }
            Order order = orderRepository.save(Order.builder()
                    .status(OrderStatus.CREATED)
                    .build());
            orderItemRepository.save(OrderItem
                    .builder()
                    .productId(command.getProductId())
                    .quantity(command.getQuantity())
                    .order(order)
                    .build());

            jsonKafkaTemplate.send("order-events", OrderEvent.builder()
                    .eventId(UUID.randomUUID().toString())
                    .orderId(order.getId())
                    .eventType(OrderStatus.CREATED.getValue())
                    .productId(command.getProductId())
                    .quantity(command.getQuantity())
                    .build());

            return CommonResponse.builder().code(CommonResponse.CODE_SUCCESS).build();
        } catch (Exception e) {
            redisTemplate.opsForValue().increment("product:stock:" + command.getProductId());
            return CommonResponse.builder().code(CommonResponse.CODE_NOT_FOUND).build();
        }
    }
}