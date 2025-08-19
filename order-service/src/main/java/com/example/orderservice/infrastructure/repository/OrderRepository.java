package com.example.orderservice.infrastructure.repository;

import com.example.orderservice.domain.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface OrderRepository extends JpaRepository<Order, String> {

    @Query(value = "select quantity from inventory where product_id = :productId", nativeQuery = true)
    int countQuantityProduct(String productId);
}
