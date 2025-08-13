package com.example.userservice.domain.entity.user;


import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.time.OffsetDateTime;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "token_expired")
public class TokenExpired implements Serializable {
    private static final long serialVersionUID = 8158227483000698472L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "access_token", nullable = false)
    private String accessToken;

    @Column(name = "created_at")
    private OffsetDateTime createdAt;

    @Column(name = "updated_at")
    private OffsetDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = OffsetDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = OffsetDateTime.now();
    }

}