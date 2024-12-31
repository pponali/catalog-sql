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
    
    Optional<ProductFeature> findByProductAndTemplate(Product product, CategoryFeatureTemplate template);
    
    List<ProductFeature> findByProduct(Product product);
    
    void deleteByProduct(Product product);
}
