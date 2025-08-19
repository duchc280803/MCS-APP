package com.example.orderservice.domain.entity;

public enum OrderStatus {
    CREATED("CREATED"),
    PROCESSING("PROCESSING"),
    INVENTORY_RESERVED("INVENTORY_RESERVED"),
    PAYMENT_SUCCESS("PAYMENT_SUCCESS"),
    PAYMENT_FAILED("PAYMENT_FAILED"),
    CANCELLED("CANCELLED"),
    SHIPPED("SHIPPED"),
    DELIVERED("DELIVERED");

    private final String value;

    OrderStatus(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
