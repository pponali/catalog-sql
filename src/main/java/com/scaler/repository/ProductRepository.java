package com.scaler.repository;

import com.scaler.entity.Product;
import com.scaler.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ProductRepository extends JpaRepository<Product, UUID> {
    Optional<Product> findBySku(String sku);
    
    @Query("SELECT p FROM Product p JOIN p.categories c WHERE c = :category")
    List<Product> findByCategory(@Param("category") Category category);

    List<Product> findByBusinessId(UUID businessId);
    List<Product> findByBusinessIdAndCategories(UUID businessId, Category category);
    Optional<Product> findByBusinessIdAndId(UUID businessId, UUID id);
    List<Product> findByCatalog(UUID catalogId);
}
