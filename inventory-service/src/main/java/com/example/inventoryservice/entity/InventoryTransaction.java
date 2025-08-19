package com.example.inventoryservice.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "inventory_transactions")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InventoryTransaction {

    public enum TransactionType {
        INBOUND,    // Nhập kho
        OUTBOUND,   // Xuất kho
        RESERVE,    // Tạm giữ
        ADJUSTMENT, // Điều chỉnh
        RESTORE     // Hoàn trả
    }

    @Id
    private String id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "inventory_item_id", nullable = false)
    private InventoryItem inventoryItem;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private TransactionType type;

    @Column(nullable = false)
    private int quantity;

    @Column(name = "reference_id", length = 50)
    private String referenceId; // OrderID/PurchaseID

    @Column(name = "notes", length = 500)
    private String notes; // Ghi chú thủ công

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;
}