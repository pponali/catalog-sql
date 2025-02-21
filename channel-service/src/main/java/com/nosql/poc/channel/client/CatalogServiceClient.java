package com.nosql.poc.channel.client;

import com.nosql.poc.channel.model.Product;
import com.nosql.poc.channel.model.ProductCatalog;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(name = "catalog-service")
public interface CatalogServiceClient {
    
    @GetMapping("/api/products/{productId}")
    @CircuitBreaker(name = "catalogService")
    @Retry(name = "catalogService")
    Product getProduct(@PathVariable("productId") String productId);
    
    @GetMapping("/api/products")
    @CircuitBreaker(name = "catalogService")
    List<Product> getProducts(@RequestParam(required = false) String channelId);
    
    @PostMapping("/api/products")
    @CircuitBreaker(name = "catalogService")
    Product createProduct(@RequestBody Product product);
    
    @PutMapping("/api/products/{productId}")
    @CircuitBreaker(name = "catalogService")
    Product updateProduct(@PathVariable("productId") String productId, @RequestBody Product product);
    
    @GetMapping("/api/catalogs/{catalogId}")
    @CircuitBreaker(name = "catalogService")
    ProductCatalog getCatalog(@PathVariable("catalogId") String catalogId);
}
