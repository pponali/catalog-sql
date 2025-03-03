package com.scaler.repository;

import com.scaler.entity.ProductFeatureValueMapping;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ProductFeatureValueMappingRepository extends JpaRepository<ProductFeatureValueMapping, UUID> {
    
    List<ProductFeatureValueMapping> findByProductId(UUID productId);
    
    List<ProductFeatureValueMapping> findByFeatureId(UUID featureId);
    
    List<ProductFeatureValueMapping> findByFeatureValueId(UUID featureValueId);
    
    List<ProductFeatureValueMapping> findByTemplateId(UUID templateId);
    
    Optional<ProductFeatureValueMapping> findByProductIdAndFeatureId(UUID productId, UUID featureId);
    
    List<ProductFeatureValueMapping> findByProductIdAndTemplateId(UUID productId, UUID templateId);
}
