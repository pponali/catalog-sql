package com.scaler.controller;

import com.scaler.entity.Platform;
import com.scaler.entity.Product;
import com.scaler.entity.ProductPlatform;
import com.scaler.service.ProductPlatformQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/product-platform/query")
@RequiredArgsConstructor
public class ProductPlatformQueryController {
    
    private final ProductPlatformQueryService productPlatformQueryService;
    
    @GetMapping
    public ResponseEntity<List<ProductPlatform>> getAllProductPlatforms() {
        return ResponseEntity.ok(productPlatformQueryService.getAllProductPlatforms());
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<ProductPlatform> getProductPlatformById(@PathVariable UUID id) {
        return productPlatformQueryService.getProductPlatformById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping("/product/{productId}")
    public ResponseEntity<List<ProductPlatform>> getProductPlatformsByProductId(@PathVariable UUID productId) {
        return ResponseEntity.ok(productPlatformQueryService.getProductPlatformsByProductId(productId));
    }
    
    @GetMapping("/platform/{platformId}")
    public ResponseEntity<List<ProductPlatform>> getProductPlatformsByPlatformId(@PathVariable UUID platformId) {
        return ResponseEntity.ok(productPlatformQueryService.getProductPlatformsByPlatformId(platformId));
    }
    
    @GetMapping("/product/{productId}/platform/{platformId}")
    public ResponseEntity<ProductPlatform> getProductPlatformByProductAndPlatform(
            @PathVariable UUID productId,
            @PathVariable UUID platformId) {
        return productPlatformQueryService.getProductPlatformByProductAndPlatform(productId, platformId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping("/product/{productId}/platforms")
    public ResponseEntity<List<Platform>> getPlatformsByProductId(@PathVariable UUID productId) {
        return ResponseEntity.ok(productPlatformQueryService.getPlatformsByProductId(productId));
    }
    
    @GetMapping("/platform/{platformId}/products")
    public ResponseEntity<List<Product>> getProductsByPlatformId(@PathVariable UUID platformId) {
        return ResponseEntity.ok(productPlatformQueryService.getProductsByPlatformId(platformId));
    }
    
    @GetMapping("/platform/code/{platformCode}/products")
    public ResponseEntity<List<Product>> getProductsByPlatformCode(@PathVariable String platformCode) {
        return ResponseEntity.ok(productPlatformQueryService.getProductsByPlatformCode(platformCode));
    }
    
    @GetMapping("/platform/{platformId}/active/{isActive}/products")
    public ResponseEntity<List<Product>> getProductsByPlatformIdAndActive(
            @PathVariable UUID platformId,
            @PathVariable boolean isActive) {
        return ResponseEntity.ok(productPlatformQueryService.getProductsByPlatformIdAndActive(platformId, isActive));
    }
}