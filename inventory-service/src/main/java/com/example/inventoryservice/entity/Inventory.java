package com.example.inventoryservice.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "inventory")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Inventory {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(nullable = false)
    private String productId;

    @Column(nullable = false)
    private String productName;

    @Column(nullable = false)
    private int quantityAvailable;

    @Column(nullable = false)
    private int quantityReserved;

    @Column(nullable = false)
    private int quantitySold;

    @Column(nullable = false)
    private String warehouseLocation;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private InventoryStatus status;

    @Column
    private LocalDateTime lastUpdated;

    @Column
    private String lastUpdatedBy;

    @Column
    private BigDecimal unitPrice;
}
