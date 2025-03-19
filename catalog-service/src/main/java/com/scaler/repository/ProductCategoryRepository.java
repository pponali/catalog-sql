package com.scaler.repository;

import com.scaler.entity.Category;
import com.scaler.entity.Product;
import com.scaler.entity.ProductCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Repository for ProductCategory entity
 */
@Repository
public interface ProductCategoryRepository extends JpaRepository<ProductCategory, UUID> {
    
    /**
     * Find a product-category mapping by product and category
     * 
     * @param product Product entity
     * @param category Category entity
     * @return Optional containing the mapping if found
     */
    Optional<ProductCategory> findByProductAndCategory(Product product, Category category);
    
    /**
     * Find all product-category mappings for a product
     * 
     * @param product Product entity
     * @return List of product-category mappings
     */
    List<ProductCategory> findByProduct(Product product);
    
    /**
     * Find all product-category mappings for a category
     * 
     * @param category Category entity
     * @return List of product-category mappings
     */
    List<ProductCategory> findByCategory(Category category);
    
    /**
     * Find all primary product-category mappings for a product
     * 
     * @param product Product entity
     * @return List of primary product-category mappings
     */
    List<ProductCategory> findByProductAndIsPrimaryTrue(Product product);
    
    /**
     * Find all product-category mappings for a product by product ID
     * 
     * @param productId Product ID
     * @return List of product-category mappings
     */
    @Query("SELECT pc FROM ProductCategory pc WHERE pc.product.id = :productId")
    List<ProductCategory> findByProductId(@Param("productId") UUID productId);
    
    /**
     * Find all product-category mappings for a category by category ID
     * 
     * @param categoryId Category ID
     * @return List of product-category mappings
     */
    @Query("SELECT pc FROM ProductCategory pc WHERE pc.category.id = :categoryId")
    List<ProductCategory> findByCategoryId(@Param("categoryId") UUID categoryId);
    
    /**
     * Find a product-category mapping by product ID and category ID
     * 
     * @param productId Product ID
     * @param categoryId Category ID
     * @return Optional containing the mapping if found
     */
    @Query("SELECT pc FROM ProductCategory pc WHERE pc.product.id = :productId AND pc.category.id = :categoryId")
    Optional<ProductCategory> findByProductIdAndCategoryId(
            @Param("productId") UUID productId,
            @Param("categoryId") UUID categoryId);
}
