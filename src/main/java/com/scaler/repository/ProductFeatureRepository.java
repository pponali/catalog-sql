package com.scaler.repository;

import com.scaler.entity.CategoryFeatureTemplate;
import com.scaler.entity.Product;
import com.scaler.entity.ProductFeature;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ProductFeatureRepository extends JpaRepository<ProductFeature, UUID> {
    
    Optional<ProductFeature> findByProductMappingsProductIdAndTemplateId(UUID productId, UUID templateId);
    
    List<ProductFeature> findByProductMappingsProductId(UUID productId);
    
    void deleteByProductMappingsProductId(UUID productId);
}
