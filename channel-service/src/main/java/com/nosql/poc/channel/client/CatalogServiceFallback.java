package com.nosql.poc.channel.client;

import com.nosql.poc.channel.model.Product;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * Fallback implementation for the CatalogServiceClient.
 * This provides degraded functionality when the catalog service is unavailable.
 */
@Component
public class CatalogServiceFallback implements CatalogServiceClient {
    
    private static final Logger log = LoggerFactory.getLogger(CatalogServiceFallback.class);
    
    @Override
    public Product getProduct(String productId) {
        log.warn("Fallback: Unable to get product with ID: {}", productId);
        return null;
    }
    
    @Override
    public List<Product> getProducts(String channelId) {
        log.warn("Fallback: Unable to get products for channel: {}", channelId);
        return new ArrayList<>();
    }
    
    @Override
    public Product createProduct(Product product) {
        log.warn("Fallback: Unable to create product: {}", product.getName());
        return null;
    }
    
    @Override
    public Product updateProduct(String productId, Product product) {
        log.warn("Fallback: Unable to update product with ID: {}", productId);
        return null;
    }
    
    @Override
    public Map<String, Object> getCatalog(String catalogId) {
        log.warn("Fallback: Unable to get catalog with ID: {}", catalogId);
        return Collections.emptyMap();
    }
}