package com.example.gyeol.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "products")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Product {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long productId;

    // 상품명
    @Column(nullable = false, length = 120)
    private String name;

    // 브랜드명
    @Column(length = 80)
    private String brand;

    // 설명
    @Column(columnDefinition = "TEXT")
    private String description;

    // 가격
    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal price;

    // 재고 수량
    @Column(nullable = false)
    private Integer stock;

    // 썸네일 이미지 URL
    @Column(length = 300)
    private String thumbnailUrl;

    // 카테고리
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false) // FK
    private Category category;

    // 생성/수정 시각
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @PrePersist
    void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = createdAt;
    }
    @PreUpdate
    void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}