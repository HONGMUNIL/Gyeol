package com.example.gyeol.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "categories")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Category {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long categoryId;

    // 카테고리명 (예: 스킨케어, 헤어, 메이크업)
    @Column(nullable = false, unique = true, length = 50)
    private String name;


    @Column(length = 255)
    private String description;
}
