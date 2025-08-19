package com.example.inventoryservice.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "inventory_item")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InventoryItem {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(name = "sku", nullable = false, length = 50)
    private String sku; // Tham chiếu đến SKU từ Product Service (ví dụ: "IPHONE15-RED-128GB")

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "warehouse_id", nullable = false)
    private Warehouse warehouse;

    @Column(name = "available_quantity", nullable = false)
    private int availableQuantity = 0; // Số lượng có thể bán ngay

    @Column(name = "reserved_quantity", nullable = false)
    private int reservedQuantity = 0; // Số lượng đang chờ thanh toán

    @Column(name = "min_stock_level")
    private int minStockLevel = 10; // Ngưỡng cảnh báo hết hàng

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Version
    private Long version; // Optimistic Locking để tránh lost update

    // Helper method để kiểm tra stock
    public boolean canReserve(int quantity) {
        return availableQuantity >= quantity;
    }

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private InventoryStatus status;

}
