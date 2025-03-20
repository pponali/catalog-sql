package com.nosql.poc.channel.client;

import com.nosql.poc.channel.model.Product;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * Feign client for communicating with the catalog service.
 */
@FeignClient(
    name = "catalog-service", 
    fallback = CatalogServiceFallback.class
)
public interface CatalogServiceClient {
    
    /**
     * Get a product by ID.
     * 
     * @param productId the product ID
     * @return the product
     */
    @GetMapping("/api/products/{productId}")
    @CircuitBreaker(name = "catalogService")
    @Retry(name = "catalogService")
    Product getProduct(@PathVariable("productId") String productId);
    
    /**
     * Get products, optionally filtered by channel.
     * 
     * @param channelId optional channel filter
     * @return list of products
     */
    @GetMapping("/api/products")
    @CircuitBreaker(name = "catalogService")
    List<Product> getProducts(@RequestParam(required = false) String channelId);
    
    /**
     * Create a new product.
     * 
     * @param product the product to create
     * @return the created product
     */
    @PostMapping("/api/products")
    @CircuitBreaker(name = "catalogService")
    Product createProduct(@RequestBody Product product);
    
    /**
     * Update an existing product.
     * 
     * @param productId the product ID
     * @param product the updated product
     * @return the updated product
     */
    @PutMapping("/api/products/{productId}")
    @CircuitBreaker(name = "catalogService")
    Product updateProduct(@PathVariable("productId") String productId, @RequestBody Product product);
    
    /**
     * Get catalog information by ID.
     * 
     * @param catalogId the catalog ID
     * @return catalog information
     */
    @GetMapping("/api/catalogs/{catalogId}")
    @CircuitBreaker(name = "catalogService")
    Map<String, Object> getCatalog(@PathVariable("catalogId") String catalogId);
}
