package com.example.orderservice.entity;

public enum OrderStatus {
    CREATED,
    PROCESSING,
    INVENTORY_RESERVED,
    PAYMENT_SUCCESS,
    PAYMENT_FAILED,
    CANCELLED,
    SHIPPED,
    DELIVERED
}