package com.scaler.controller;

import com.scaler.dto.ProductDTO;
import com.scaler.dto.ProductFeatureDTO;
import com.scaler.dto.ProductFeatureValueDTO;
import com.scaler.dto.ProductFeatureValueMappingDTO;
import com.scaler.entity.Product;
import com.scaler.entity.ProductFeature;
import com.scaler.entity.ProductFeatureValue;
import com.scaler.entity.ProductFeatureValueMapping;
import com.scaler.mapper.ProductFeatureMapper;
import com.scaler.mapper.ProductFeatureValueMapper;
import com.scaler.mapper.ProductFeatureValueMappingMapper;
import com.scaler.mapper.ProductMapper;
import com.scaler.service.ProductFeatureValueMappingQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * DTO-based controller for ProductFeatureValueMapping queries.
 * This controller demonstrates the use of DTOs instead of directly exposing entities.
 */
@RestController
@RequestMapping("/product-feature-value-mapping/query/dto")
@RequiredArgsConstructor
public class ProductFeatureValueMappingQueryDTOController {
    
    private final ProductFeatureValueMappingQueryService productFeatureValueMappingQueryService;
    private final ProductFeatureValueMappingMapper productFeatureValueMappingMapper;
    private final ProductFeatureMapper productFeatureMapper;
    private final ProductFeatureValueMapper productFeatureValueMapper;
    private final ProductMapper productMapper;
    
    @GetMapping
    public ResponseEntity<List<ProductFeatureValueMappingDTO>> getAllProductFeatureValueMappings() {
        List<ProductFeatureValueMapping> mappings = productFeatureValueMappingQueryService.getAllProductFeatureValueMappings();
        return ResponseEntity.ok(productFeatureValueMappingMapper.toDtoList(mappings));
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<ProductFeatureValueMappingDTO> getProductFeatureValueMappingById(@PathVariable UUID id) {
        return productFeatureValueMappingQueryService.getProductFeatureValueMappingById(id)
                .map(productFeatureValueMappingMapper::toDto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping("/product/{productId}")
    public ResponseEntity<List<ProductFeatureValueMappingDTO>> getProductFeatureValueMappingsByProductId(@PathVariable UUID productId) {
        List<ProductFeatureValueMapping> mappings = productFeatureValueMappingQueryService.getProductFeatureValueMappingsByProductId(productId);
        return ResponseEntity.ok(productFeatureValueMappingMapper.toDtoList(mappings));
    }
    
    @GetMapping("/feature/{featureId}")
    public ResponseEntity<List<ProductFeatureValueMappingDTO>> getProductFeatureValueMappingsByFeatureId(@PathVariable UUID featureId) {
        List<ProductFeatureValueMapping> mappings = productFeatureValueMappingQueryService.getProductFeatureValueMappingsByFeatureId(featureId);
        return ResponseEntity.ok(productFeatureValueMappingMapper.toDtoList(mappings));
    }
    
    @GetMapping("/feature-value/{featureValueId}")
    public ResponseEntity<List<ProductFeatureValueMappingDTO>> getProductFeatureValueMappingsByFeatureValueId(@PathVariable UUID featureValueId) {
        List<ProductFeatureValueMapping> mappings = productFeatureValueMappingQueryService.getProductFeatureValueMappingsByFeatureValueId(featureValueId);
        return ResponseEntity.ok(productFeatureValueMappingMapper.toDtoList(mappings));
    }
    
    @GetMapping("/template/{templateId}")
    public ResponseEntity<List<ProductFeatureValueMappingDTO>> getProductFeatureValueMappingsByTemplateId(@PathVariable UUID templateId) {
        List<ProductFeatureValueMapping> mappings = productFeatureValueMappingQueryService.getProductFeatureValueMappingsByTemplateId(templateId);
        return ResponseEntity.ok(productFeatureValueMappingMapper.toDtoList(mappings));
    }
    
    @GetMapping("/product/{productId}/feature/{featureId}")
    public ResponseEntity<ProductFeatureValueMappingDTO> getProductFeatureValueMappingByProductAndFeature(
            @PathVariable UUID productId,
            @PathVariable UUID featureId) {
        return productFeatureValueMappingQueryService.getProductFeatureValueMappingByProductAndFeature(productId, featureId)
                .map(productFeatureValueMappingMapper::toDto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping("/product/{productId}/template/{templateId}")
    public ResponseEntity<List<ProductFeatureValueMappingDTO>> getProductFeatureValueMappingsByProductAndTemplate(
            @PathVariable UUID productId,
            @PathVariable UUID templateId) {
        List<ProductFeatureValueMapping> mappings = productFeatureValueMappingQueryService.getProductFeatureValueMappingsByProductAndTemplate(productId, templateId);
        return ResponseEntity.ok(productFeatureValueMappingMapper.toDtoList(mappings));
    }
    
    @GetMapping("/product/{productId}/features")
    public ResponseEntity<List<ProductFeatureDTO>> getFeaturesByProductId(@PathVariable UUID productId) {
        List<ProductFeature> features = productFeatureValueMappingQueryService.getFeaturesByProductId(productId);
        return ResponseEntity.ok(productFeatureMapper.toDTOList(features));
    }
    
    @GetMapping("/product/{productId}/feature-values")
    public ResponseEntity<List<ProductFeatureValueDTO>> getFeatureValuesByProductId(@PathVariable UUID productId) {
        List<ProductFeatureValue> featureValues = productFeatureValueMappingQueryService.getFeatureValuesByProductId(productId);
        return ResponseEntity.ok(productFeatureValueMapper.toDTOList(featureValues));
    }
    
    @GetMapping("/product/{productId}/feature/{featureId}/feature-values")
    public ResponseEntity<List<ProductFeatureValueDTO>> getFeatureValuesByProductAndFeature(
            @PathVariable UUID productId,
            @PathVariable UUID featureId) {
        List<ProductFeatureValue> featureValues = productFeatureValueMappingQueryService.getFeatureValuesByProductAndFeature(productId, featureId);
        return ResponseEntity.ok(productFeatureValueMapper.toDTOList(featureValues));
    }
    
    @GetMapping("/feature/{featureId}/products")
    public ResponseEntity<List<ProductDTO>> getProductsByFeatureId(@PathVariable UUID featureId) {
        List<Product> products = productFeatureValueMappingQueryService.getProductsByFeatureId(featureId);
        return ResponseEntity.ok(products.stream()
                .map(productMapper::toDTO)
                .collect(Collectors.toList()));
    }
    
    @GetMapping("/feature-value/{featureValueId}/products")
    public ResponseEntity<List<ProductDTO>> getProductsByFeatureValueId(@PathVariable UUID featureValueId) {
        List<Product> products = productFeatureValueMappingQueryService.getProductsByFeatureValueId(featureValueId);
        return ResponseEntity.ok(products.stream()
                .map(productMapper::toDTO)
                .collect(Collectors.toList()));
    }
    
    @GetMapping("/template/{templateId}/products")
    public ResponseEntity<List<ProductDTO>> getProductsByTemplateId(@PathVariable UUID templateId) {
        List<Product> products = productFeatureValueMappingQueryService.getProductsByTemplateId(templateId);
        return ResponseEntity.ok(products.stream()
                .map(productMapper::toDTO)
                .collect(Collectors.toList()));
    }
}