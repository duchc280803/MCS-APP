package com.example.orderservice.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "orders")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Order {

    @Id
    @Column(nullable = false, updatable = false)
    private String id = UUID.randomUUID().toString();

    @Column(nullable = false)
    private String customerId;

    @Column(nullable = false)
    private String customerName;

    @Column(nullable = false)
    private String phoneNumber;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String shippingAddress;

    @Column(nullable = false)
    private String billingAddress;

    @Column
    private String note;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OrderStatus status;

    @Column(nullable = false)
    private BigDecimal totalAmount;

    @Column(nullable = false)
    private BigDecimal shippingFee;

    @Column(nullable = false)
    private BigDecimal discount;

    @Column(nullable = false)
    private String paymentMethod; // COD, CREDIT_CARD, VNPAY, etc.

    @Column(nullable = false)
    private boolean isPaid;

    @Column
    private LocalDateTime paidAt;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Column
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItem> items;
}
