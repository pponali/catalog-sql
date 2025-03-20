package com.scaler.productread.client;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Fallback implementation for the catalog service client.
 * This is used when the catalog service is unavailable.
 */
@Slf4j
@Component
public class CatalogServiceClientFallback implements CatalogServiceClient {
    
    @Override
    public Map<String, Object> getProduct(UUID id) {
        log.warn("Fallback method for getProduct called for ID: {}", id);
        return new HashMap<>();
    }
    
    @Override
    public Map<String, Object> getProducts(List<UUID> ids, int page, int size, String category, String brand) {
        log.warn("Fallback method for getProducts called. IDs: {}, page: {}, size: {}", ids, page, size);
        Map<String, Object> result = new HashMap<>();
        result.put("content", Collections.emptyList());
        result.put("totalElements", 0);
        result.put("totalPages", 0);
        result.put("number", page);
        result.put("size", size);
        return result;
    }
    
    @Override
    public Map<String, Object> getCategory(UUID id) {
        log.warn("Fallback method for getCategory called for ID: {}", id);
        return new HashMap<>();
    }
    
    @Override
    public List<Map<String, Object>> getProductFeatures(UUID productId) {
        log.warn("Fallback method for getProductFeatures called for product ID: {}", productId);
        return Collections.emptyList();
    }
    
    @Override
    public Map<String, Object> getUpdatedProducts(LocalDateTime updatedSince, int page, int size) {
        log.warn("Fallback method for getUpdatedProducts called since: {}, page: {}, size: {}", 
                updatedSince, page, size);
        Map<String, Object> result = new HashMap<>();
        result.put("content", Collections.emptyList());
        result.put("totalElements", 0);
        result.put("totalPages", 0);
        result.put("number", page);
        result.put("size", size);
        return result;
    }
    
    @Override
    public List<Map<String, Object>> getProductSellers(UUID productId) {
        log.warn("Fallback method for getProductSellers called for product ID: {}", productId);
        return Collections.emptyList();
    }
    
    @Override
    public List<Map<String, Object>> getProductChannels(UUID productId) {
        log.warn("Fallback method for getProductChannels called for product ID: {}", productId);
        return Collections.emptyList();
    }
    
    @Override
    public List<Map<String, Object>> getProductImages(UUID productId) {
        log.warn("Fallback method for getProductImages called for product ID: {}", productId);
        return Collections.emptyList();
    }
}