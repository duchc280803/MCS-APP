package com.example.productservice.domain.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "size")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Size {

    @Id
    private String id;

    private Integer size;

    @Column(length = 100)
    private String description;
}
