package com.scaler.service;

import com.scaler.entity.Platform;
import com.scaler.entity.Product;
import com.scaler.entity.ProductPlatform;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ProductPlatformQueryService {
    
    List<ProductPlatform> getAllProductPlatforms();
    
    Optional<ProductPlatform> getProductPlatformById(UUID id);
    
    List<ProductPlatform> getProductPlatformsByProductId(UUID productId);
    
    List<ProductPlatform> getProductPlatformsByPlatformId(UUID platformId);
    
    Optional<ProductPlatform> getProductPlatformByProductAndPlatform(UUID productId, UUID platformId);
    
    List<Platform> getPlatformsByProductId(UUID productId);
    
    List<Product> getProductsByPlatformId(UUID platformId);
    
    List<Product> getProductsByPlatformCode(String platformCode);
    
    List<Product> getProductsByPlatformIdAndActive(UUID platformId, boolean isActive);
}