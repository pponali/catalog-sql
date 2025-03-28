package com.scaler.productread.controller;

import com.scaler.productread.sync.ProductDataSynchronizer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

/**
 * Controller for managing data synchronization operations.
 */
@Slf4j
@RestController
@RequestMapping("/api/sync")
public class SyncController {
    
    private final ProductDataSynchronizer productDataSynchronizer;
    
    @Autowired
    public SyncController(ProductDataSynchronizer productDataSynchronizer) {
        this.productDataSynchronizer = productDataSynchronizer;
    }
    
    /**
     * Trigger a full synchronization of all products.
     *
     * @return Status response
     */
    @PostMapping("/products/full")
    public ResponseEntity<Map<String, Object>> syncAllProducts() {
        log.info("Full product synchronization requested");
        Map<String, Object> response = new HashMap<>();
        
        // Run synchronization in a separate thread to avoid blocking
        CompletableFuture.runAsync(() -> {
            productDataSynchronizer.forceSyncAllProducts();
        });
        
        response.put("status", "started");
        response.put("message", "Full product synchronization started. This operation may take some time.");
        
        return ResponseEntity.accepted().body(response);
    }
    
    /**
     * Trigger synchronization for a specific product.
     *
     * @param productId Product ID to synchronize
     * @return Status response
     */
    @PostMapping("/products/{id}")
    public ResponseEntity<Map<String, Object>> syncProduct(@PathVariable("id") UUID productId) {
        log.info("Product synchronization requested for product ID: {}", productId);
        Map<String, Object> response = new HashMap<>();
        
        boolean success = productDataSynchronizer.synchronizeProduct(productId);
        
        if (success) {
            response.put("status", "success");
            response.put("message", "Product synchronization completed successfully");
            return ResponseEntity.ok(response);
        } else {
            response.put("status", "error");
            response.put("message", "Failed to synchronize product");
            return ResponseEntity.badRequest().body(response);
        }
    }
    
    /**
     * Trigger incremental synchronization of recently updated products.
     *
     * @return Status response
     */
    @PostMapping("/products/incremental")
    public ResponseEntity<Map<String, Object>> syncUpdatedProducts() {
        log.info("Incremental product synchronization requested");
        Map<String, Object> response = new HashMap<>();
        
        CompletableFuture.runAsync(() -> {
            productDataSynchronizer.synchronizeUpdatedProducts();
        });
        
        response.put("status", "started");
        response.put("message", "Incremental product synchronization started.");
        
        return ResponseEntity.accepted().body(response);
    }
}