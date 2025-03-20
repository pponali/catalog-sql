package com.nosql.poc.vendor.client;

import com.nosql.poc.vendor.model.Product;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(
    name = "catalog-service", 
    fallback = com.nosql.poc.vendor.client.fallback.CatalogServiceFallback.class,
    configuration = com.nosql.poc.vendor.config.FeignConfig.class
)
public interface CatalogServiceClient {
    
    @GetMapping("/api/vendors/{vendorId}/products")
    @CircuitBreaker(name = "catalogService")
    @Retry(name = "catalogService")
    List<Product> getVendorProducts(@PathVariable("vendorId") String vendorId);
    
    @GetMapping("/api/products/{productId}")
    @CircuitBreaker(name = "catalogService")
    Product getProduct(@PathVariable("productId") String productId);
    
    @PostMapping("/api/vendors/{vendorId}/products")
    @CircuitBreaker(name = "catalogService")
    Product createProduct(@PathVariable("vendorId") String vendorId,
                        @RequestBody Product product);
    
    @PutMapping("/api/vendors/{vendorId}/products/{productId}")
    @CircuitBreaker(name = "catalogService")
    Product updateProduct(@PathVariable("vendorId") String vendorId,
                        @PathVariable("productId") String productId,
                        @RequestBody Product product);
}
