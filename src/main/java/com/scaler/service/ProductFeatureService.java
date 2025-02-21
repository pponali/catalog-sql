package com.scaler.service;

import com.scaler.entity.CategoryFeatureTemplate;
import com.scaler.entity.Product;
import com.scaler.entity.ProductFeature;

import java.util.List;
import java.util.UUID;

/**
 * Service interface for managing product features
 */
public interface ProductFeatureService {
    
    /**
     * Find a product feature by id
     * @param id The id to find feature for
     * @return The product feature
     */
    ProductFeature findById(UUID id);
    
    /**
     * Find a product feature by product id and category feature template id
     * @param productId The id of the product to find feature for
     * @param templateId The id of the category feature template to find feature for
     * @return The product feature
     */
    ProductFeature findByProductIdAndTemplateId(UUID productId, UUID templateId);
    
    /**
     * Find all features for a product
     * @param productId The id of the product to find features for
     * @return List of product features
     */
    List<ProductFeature> findByProductId(UUID productId);
    
    /**
     * Save a product feature
     * @param feature The product feature to save
     * @return The saved product feature
     */
    ProductFeature save(ProductFeature feature);
    
    /**
     * Delete a product feature by id
     * @param id The id of the product feature to delete
     */
    void deleteById(UUID id);
    
    /**
     * Delete all features for a product by product id
     * @param productId The id of the product to delete features for
     */
    void deleteByProductId(UUID productId);
}
