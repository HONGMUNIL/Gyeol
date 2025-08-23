package com.example.gyeol.repository;

import com.example.gyeol.entity.Product;
import com.example.gyeol.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {
    // 카테고리별 상품 목록
    List<Product> findByCategory(Category category);
    // 카테고리 ID로 검색
    List<Product> findByCategory_CategoryId(Long categoryId);
}