package com.example.repository;

import com.example.entity.CategoryFeatureTemplate;
import com.example.entity.Product;
import com.example.entity.ProductFeature;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductFeatureRepository extends JpaRepository<ProductFeature, Long> {
    
    Optional<ProductFeature> findByProductAndTemplate(Product product, CategoryFeatureTemplate template);
    
    List<ProductFeature> findByProduct(Product product);
    
    void deleteByProduct(Product product);
}
