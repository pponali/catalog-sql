package com.scaler.service;

import com.scaler.dto.ProductDTO;
import com.scaler.entity.Product;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Service interface for Product operations
 */
public interface ProductService {
    /**
     * Create a new product
     * @param productDTO The product data to create
     * @return The created product entity
     */
    Product createProduct(ProductDTO productDTO);
    
    /**
     * Update an existing product
     * @param id The ID of the product to update
     * @param productDTO The updated product data
     * @return The updated product entity
     */
    Product updateProduct(UUID id, ProductDTO productDTO);
    
    /**
     * Get a product by its ID
     * @param id The ID of the product
     * @return The product entity
     */
    Product getProductById(UUID id);
    
    /**
     * Get all products in a specific category
     * @param categoryId The category ID
     * @return List of product entities in the category
     */
    List<Product> getProductsByCategory(UUID categoryId);
    
    /**
     * Search for products based on a search query and filters
     * @param query The search query
     * @param categoryIds Optional category IDs to filter by
     * @param filters Additional filters as key-value pairs
     * @param page The page number (for pagination)
     * @param size The page size (for pagination)
     * @return List of matching product entities
     */
    List<Product> searchProducts(String query, List<UUID> categoryIds, List<String> filters, int page, int size);
    
    /**
     * Delete a product
     * @param id The ID of the product to delete
     * @return true if deletion was successful, false otherwise
     */
    boolean deleteProduct(UUID id);
    
    /**
     * Check if a product exists
     * @param id The ID of the product
     * @return true if the product exists, false otherwise
     */
    boolean existsById(UUID id);
    
    /**
     * LEGACY METHODS - these should be migrated to the new methods above
     */
    ProductDTO create(ProductDTO product);
    ProductDTO update(UUID id, ProductDTO product);
    Optional<ProductDTO> findById(UUID id);
    List<ProductDTO> findAll();
    void delete(UUID id);
    void deleteById(UUID id);
}
