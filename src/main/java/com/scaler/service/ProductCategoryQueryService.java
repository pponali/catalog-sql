package com.scaler.service;

import com.scaler.entity.Category;
import com.scaler.entity.Product;
import com.scaler.entity.ProductCategory;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ProductCategoryQueryService {
    
    List<ProductCategory> getAllProductCategories();
    
    Optional<ProductCategory> getProductCategoryById(UUID id);
    
    List<ProductCategory> getProductCategoriesByProductId(UUID productId);
    
    List<ProductCategory> getProductCategoriesByCategoryId(UUID categoryId);
    
    List<ProductCategory> getProductCategoriesByMerchantId(UUID merchantId);
    
    Optional<ProductCategory> getProductCategoryByProductAndCategory(UUID productId, UUID categoryId);
    
    Optional<ProductCategory> getProductCategoryByProductCategoryAndMerchant(UUID productId, UUID categoryId, UUID merchantId);
    
    List<Category> getCategoriesByProductId(UUID productId);
    
    List<Product> getProductsByCategoryId(UUID categoryId);
    
    List<Product> getProductsByCategoryIdAndMerchantId(UUID categoryId, UUID merchantId);
}