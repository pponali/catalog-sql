package com.scaler.controller;

import com.scaler.entity.*;
import com.scaler.service.ProductFeatureValueMappingQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/product-feature-value-mapping/query")
@RequiredArgsConstructor
public class ProductFeatureValueMappingQueryController {
    
    private final ProductFeatureValueMappingQueryService productFeatureValueMappingQueryService;
    
    @GetMapping
    public ResponseEntity<List<ProductFeatureValueMapping>> getAllProductFeatureValueMappings() {
        return ResponseEntity.ok(productFeatureValueMappingQueryService.getAllProductFeatureValueMappings());
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<ProductFeatureValueMapping> getProductFeatureValueMappingById(@PathVariable UUID id) {
        return productFeatureValueMappingQueryService.getProductFeatureValueMappingById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping("/product/{productId}")
    public ResponseEntity<List<ProductFeatureValueMapping>> getProductFeatureValueMappingsByProductId(@PathVariable UUID productId) {
        return ResponseEntity.ok(productFeatureValueMappingQueryService.getProductFeatureValueMappingsByProductId(productId));
    }
    
    @GetMapping("/feature/{featureId}")
    public ResponseEntity<List<ProductFeatureValueMapping>> getProductFeatureValueMappingsByFeatureId(@PathVariable UUID featureId) {
        return ResponseEntity.ok(productFeatureValueMappingQueryService.getProductFeatureValueMappingsByFeatureId(featureId));
    }
    
    @GetMapping("/feature-value/{featureValueId}")
    public ResponseEntity<List<ProductFeatureValueMapping>> getProductFeatureValueMappingsByFeatureValueId(@PathVariable UUID featureValueId) {
        return ResponseEntity.ok(productFeatureValueMappingQueryService.getProductFeatureValueMappingsByFeatureValueId(featureValueId));
    }
    
    @GetMapping("/template/{templateId}")
    public ResponseEntity<List<ProductFeatureValueMapping>> getProductFeatureValueMappingsByTemplateId(@PathVariable UUID templateId) {
        return ResponseEntity.ok(productFeatureValueMappingQueryService.getProductFeatureValueMappingsByTemplateId(templateId));
    }
    
    @GetMapping("/product/{productId}/feature/{featureId}")
    public ResponseEntity<ProductFeatureValueMapping> getProductFeatureValueMappingByProductAndFeature(
            @PathVariable UUID productId,
            @PathVariable UUID featureId) {
        return productFeatureValueMappingQueryService.getProductFeatureValueMappingByProductAndFeature(productId, featureId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping("/product/{productId}/template/{templateId}")
    public ResponseEntity<List<ProductFeatureValueMapping>> getProductFeatureValueMappingsByProductAndTemplate(
            @PathVariable UUID productId,
            @PathVariable UUID templateId) {
        return ResponseEntity.ok(productFeatureValueMappingQueryService.getProductFeatureValueMappingsByProductAndTemplate(productId, templateId));
    }
    
    @GetMapping("/product/{productId}/features")
    public ResponseEntity<List<ProductFeature>> getFeaturesByProductId(@PathVariable UUID productId) {
        return ResponseEntity.ok(productFeatureValueMappingQueryService.getFeaturesByProductId(productId));
    }
    
    @GetMapping("/product/{productId}/feature-values")
    public ResponseEntity<List<ProductFeatureValue>> getFeatureValuesByProductId(@PathVariable UUID productId) {
        return ResponseEntity.ok(productFeatureValueMappingQueryService.getFeatureValuesByProductId(productId));
    }
    
    @GetMapping("/product/{productId}/feature/{featureId}/feature-values")
    public ResponseEntity<List<ProductFeatureValue>> getFeatureValuesByProductAndFeature(
            @PathVariable UUID productId,
            @PathVariable UUID featureId) {
        return ResponseEntity.ok(productFeatureValueMappingQueryService.getFeatureValuesByProductAndFeature(productId, featureId));
    }
    
    @GetMapping("/feature/{featureId}/products")
    public ResponseEntity<List<Product>> getProductsByFeatureId(@PathVariable UUID featureId) {
        return ResponseEntity.ok(productFeatureValueMappingQueryService.getProductsByFeatureId(featureId));
    }
    
    @GetMapping("/feature-value/{featureValueId}/products")
    public ResponseEntity<List<Product>> getProductsByFeatureValueId(@PathVariable UUID featureValueId) {
        return ResponseEntity.ok(productFeatureValueMappingQueryService.getProductsByFeatureValueId(featureValueId));
    }
    
    @GetMapping("/template/{templateId}/products")
    public ResponseEntity<List<Product>> getProductsByTemplateId(@PathVariable UUID templateId) {
        return ResponseEntity.ok(productFeatureValueMappingQueryService.getProductsByTemplateId(templateId));
    }
}