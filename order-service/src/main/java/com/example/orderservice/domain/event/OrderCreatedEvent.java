package com.example.orderservice.domain.event;

import com.example.orderservice.domain.entity.Order;

public class OrderCreatedEvent {

    private String productId;

    private int quantity;

    private Order order;

}
