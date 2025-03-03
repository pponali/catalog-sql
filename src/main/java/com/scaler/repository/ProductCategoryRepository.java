package com.scaler.repository;

import com.scaler.entity.ProductCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ProductCategoryRepository extends JpaRepository<ProductCategory, UUID> {
    
    List<ProductCategory> findByProductId(UUID productId);
    
    List<ProductCategory> findByCategoryId(UUID categoryId);
    
    List<ProductCategory> findByMerchantId(UUID merchantId);
    
    Optional<ProductCategory> findByProductIdAndCategoryId(UUID productId, UUID categoryId);
    
    Optional<ProductCategory> findByProductIdAndCategoryIdAndMerchantId(UUID productId, UUID categoryId, UUID merchantId);
    
    List<ProductCategory> findByCategoryIdAndMerchantId(UUID categoryId, UUID merchantId);
}
