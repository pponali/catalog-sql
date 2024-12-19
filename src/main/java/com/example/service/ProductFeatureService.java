package com.example.service;

import com.example.entity.CategoryFeatureTemplate;
import com.example.entity.Product;
import com.example.entity.ProductFeature;

import java.util.List;

/**
 * Service interface for managing product features
 */
public interface ProductFeatureService {
    
    /**
     * Find a product feature by id
     * @param id The id to find feature for
     * @return The product feature
     */
    ProductFeature findById(Long id);
    
    /**
     * Find a product feature by product and category feature template
     * @param product The product to find feature for
     * @param template The category feature template to find feature for
     * @return The product feature
     */
    ProductFeature findByProductAndTemplate(Product product, CategoryFeatureTemplate template);
    
    /**
     * Find all features for a product
     * @param product The product to find features for
     * @return List of product features
     */
    List<ProductFeature> findByProduct(Product product);
    
    /**
     * Save a product feature
     * @param feature The product feature to save
     * @return The saved product feature
     */
    ProductFeature save(ProductFeature feature);
    
    /**
     * Delete a product feature
     * @param feature The product feature to delete
     */
    void delete(ProductFeature feature);
    
    /**
     * Delete all features for a product
     * @param product The product to delete features for
     */
    void deleteByProduct(Product product);
}
