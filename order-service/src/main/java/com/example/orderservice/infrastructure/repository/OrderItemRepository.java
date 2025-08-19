package com.example.orderservice.infrastructure.repository;

import com.example.orderservice.domain.entity.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderItemRepository extends JpaRepository<OrderItem, String> {
}
