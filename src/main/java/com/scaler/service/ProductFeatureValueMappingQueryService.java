package com.scaler.service;

import com.scaler.entity.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ProductFeatureValueMappingQueryService {
    
    List<ProductFeatureValueMapping> getAllProductFeatureValueMappings();
    
    Optional<ProductFeatureValueMapping> getProductFeatureValueMappingById(UUID id);
    
    List<ProductFeatureValueMapping> getProductFeatureValueMappingsByProductId(UUID productId);
    
    List<ProductFeatureValueMapping> getProductFeatureValueMappingsByFeatureId(UUID featureId);
    
    List<ProductFeatureValueMapping> getProductFeatureValueMappingsByFeatureValueId(UUID featureValueId);
    
    List<ProductFeatureValueMapping> getProductFeatureValueMappingsByTemplateId(UUID templateId);
    
    Optional<ProductFeatureValueMapping> getProductFeatureValueMappingByProductAndFeature(UUID productId, UUID featureId);
    
    List<ProductFeatureValueMapping> getProductFeatureValueMappingsByProductAndTemplate(UUID productId, UUID templateId);
    
    List<ProductFeature> getFeaturesByProductId(UUID productId);
    
    List<ProductFeatureValue> getFeatureValuesByProductId(UUID productId);
    
    List<ProductFeatureValue> getFeatureValuesByProductAndFeature(UUID productId, UUID featureId);
    
    List<Product> getProductsByFeatureId(UUID featureId);
    
    List<Product> getProductsByFeatureValueId(UUID featureValueId);
    
    List<Product> getProductsByTemplateId(UUID templateId);
}