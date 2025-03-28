package com.scaler.productread.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Feign client for the catalog service.
 */
@FeignClient(name = "catalog-sqldb-poc", fallback = CatalogServiceClientFallback.class)
public interface CatalogServiceClient {
    
    /**
     * Get a product by ID.
     *
     * @param id Product ID
     * @return Product data
     */
    @GetMapping("/api/products/{id}")
    Map<String, Object> getProduct(@PathVariable("id") UUID id);
    
    /**
     * Get a list of products.
     *
     * @param ids      Product IDs
     * @param page     Page number
     * @param size     Page size
     * @param category Category filter
     * @param brand    Brand filter
     * @return List of products
     */
    @GetMapping("/api/products")
    Map<String, Object> getProducts(@RequestParam(value = "ids", required = false) List<UUID> ids,
                                    @RequestParam(value = "page", required = false, defaultValue = "0") int page,
                                    @RequestParam(value = "size", required = false, defaultValue = "20") int size,
                                    @RequestParam(value = "category", required = false) String category,
                                    @RequestParam(value = "brand", required = false) String brand);
    
    /**
     * Get a category by ID.
     *
     * @param id Category ID
     * @return Category data
     */
    @GetMapping("/api/categories/{id}")
    Map<String, Object> getCategory(@PathVariable("id") UUID id);
    
    /**
     * Get product features.
     *
     * @param productId Product ID
     * @return Product features
     */
    @GetMapping("/api/products/{id}/features")
    List<Map<String, Object>> getProductFeatures(@PathVariable("id") UUID productId);
    
    /**
     * Get products updated since a specific time.
     *
     * @param updatedSince Time threshold for updates
     * @param page         Page number
     * @param size         Page size
     * @return List of updated products
     */
    @GetMapping("/api/products/updated")
    Map<String, Object> getUpdatedProducts(
            @RequestParam("updatedSince") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime updatedSince,
            @RequestParam(value = "page", required = false, defaultValue = "0") int page,
            @RequestParam(value = "size", required = false, defaultValue = "100") int size);
    
    /**
     * Get product sellers information.
     *
     * @param productId Product ID
     * @return List of sellers
     */
    @GetMapping("/api/products/{id}/sellers")
    List<Map<String, Object>> getProductSellers(@PathVariable("id") UUID productId);
    
    /**
     * Get product channel availability information.
     *
     * @param productId Product ID
     * @return List of channels
     */
    @GetMapping("/api/products/{id}/channels")
    List<Map<String, Object>> getProductChannels(@PathVariable("id") UUID productId);
    
    /**
     * Get product images information.
     *
     * @param productId Product ID
     * @return List of images
     */
    @GetMapping("/api/products/{id}/images")
    List<Map<String, Object>> getProductImages(@PathVariable("id") UUID productId);
}