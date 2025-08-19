package com.example.orderservice.domain.event;

import lombok.Builder;

@Builder
public class OrderEvent {

    private String eventId;

    private String orderId;

    private String eventType;

    private String productId;

    private int quantity;
}
