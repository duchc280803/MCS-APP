package com.example.productservice.domain.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "brands")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Brand {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    private String logoUrl;

    private String description;

    @Column(nullable = false)
    private String slug;

    @OneToMany(mappedBy = "brand", fetch = FetchType.EAGER)
    private List<Product> products;
}