package com.nosql.poc.vendor.client.fallback;

import com.nosql.poc.vendor.client.CatalogServiceClient;
import com.nosql.poc.vendor.model.Product;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Fallback implementation for CatalogServiceClient to handle service unavailability.
 */
@Component
public class CatalogServiceFallback implements CatalogServiceClient {
    private static final Logger logger = LoggerFactory.getLogger(CatalogServiceFallback.class);
    
    @Override
    public List<Product> getVendorProducts(String vendorId) {
        logger.warn("Falling back to stub response for getVendorProducts({})", vendorId);
        return new ArrayList<>(); // Return empty list as fallback
    }
    
    @Override
    public Product getProduct(String productId) {
        logger.warn("Falling back to stub response for getProduct({})", productId);
        Product fallbackProduct = new Product();
        fallbackProduct.setId(productId);
        fallbackProduct.setSku("FB-" + productId);
        fallbackProduct.setName("Fallback Product");
        fallbackProduct.setDescription("This is a fallback response when catalog service is unavailable");
        fallbackProduct.setStatus("FALLBACK");
        return fallbackProduct;
    }
    
    @Override
    public Product createProduct(String vendorId, Product product) {
        logger.warn("Falling back for createProduct({})", vendorId);
        product.setId(UUID.randomUUID().toString());
        return product;
    }
    
    @Override
    public Product updateProduct(String vendorId, String productId, Product product) {
        logger.warn("Falling back for updateProduct({}, {})", vendorId, productId);
        // Return the same object since we can't make actual update
        return product;
    }
}