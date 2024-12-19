package com.example.repository;

import com.example.entity.Product;
import com.example.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    Optional<Product> findBySku(String sku);
    
    @Query("SELECT p FROM Product p JOIN p.categories c WHERE c = :category")
    List<Product> findByCategory(@Param("category") Category category);
}
