package com.example.controller;

import com.example.dto.ProductDTO;
import com.example.dto.ProductFeatureDTO;
import com.example.entity.CategoryFeatureTemplate;
import com.example.entity.Product;
import com.example.entity.ProductFeature;
import com.example.mapper.ProductFeatureMapper;
import com.example.mapper.ProductMapper;
import com.example.service.ProductFeatureService;
import com.example.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;
    private final ProductFeatureService featureService;
    private final ProductMapper productMapper;
    private final ProductFeatureMapper featureMapper;

    @GetMapping
    public ResponseEntity<List<ProductDTO>> getAllProducts() {
        List<Product> products = productService.findAll();
        return ResponseEntity.ok(products.stream()
            .map(productMapper::toDTO)
            .collect(Collectors.toList()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductDTO> getProduct(@PathVariable Long id) {
        Product product = productService.findById(id);
        return ResponseEntity.ok(productMapper.toDTO(product));
    }

    @PostMapping
    public ResponseEntity<ProductDTO> createProduct(@Valid @RequestBody ProductDTO productDTO) {
        Product product = productMapper.toEntity(productDTO);
        product = productService.save(product);
        return ResponseEntity.ok(productMapper.toDTO(product));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductDTO> updateProduct(
            @PathVariable Long id,
            @Valid @RequestBody ProductDTO productDTO) {
        Product existingProduct = productService.findById(id);
        productMapper.updateEntityFromDTO(productDTO, existingProduct);
        existingProduct = productService.save(existingProduct);
        return ResponseEntity.ok(productMapper.toDTO(existingProduct));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        productService.delete(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{id}/features")
    public ResponseEntity<List<ProductFeatureDTO>> getProductFeatures(@PathVariable Long id) {
        Product product = productService.findById(id);
        List<ProductFeature> features = featureService.findByProduct(product);
        return ResponseEntity.ok(features.stream()
            .map(featureMapper::toDTO)
            .collect(Collectors.toList()));
    }

    @PostMapping("/{id}/features")
    public ResponseEntity<ProductFeatureDTO> addProductFeature(
            @PathVariable Long id,
            @Valid @RequestBody ProductFeatureDTO featureDTO) {
        Product product = productService.findById(id);
        CategoryFeatureTemplate template = product.getCategories().stream()
            .flatMap(cat -> cat.getFeatureTemplates().stream())
            .filter(t -> t.getCode().equals(featureDTO.getTemplateCode()))
            .findFirst()
            .orElseThrow(() -> new IllegalArgumentException("Template not found: " + featureDTO.getTemplateCode()));
            
        ProductFeature feature = featureMapper.toEntity(featureDTO);
        feature.setProduct(product);
        feature.setTemplate(template);
        feature = featureService.save(feature);
        return ResponseEntity.ok(featureMapper.toDTO(feature));
    }

    @PutMapping("/{productId}/features/{featureId}")
    public ResponseEntity<ProductFeatureDTO> updateProductFeature(
            @PathVariable Long productId,
            @PathVariable Long featureId,
            @Valid @RequestBody ProductFeatureDTO featureDTO) {
        Product product = productService.findById(productId);
        CategoryFeatureTemplate template = product.getCategories().stream()
            .flatMap(cat -> cat.getFeatureTemplates().stream())
            .filter(t -> t.getCode().equals(featureDTO.getTemplateCode()))
            .findFirst()
            .orElseThrow(() -> new IllegalArgumentException("Template not found: " + featureDTO.getTemplateCode()));
            
        ProductFeature existingFeature = featureService.findByProductAndTemplate(product, template);
        featureMapper.updateEntityFromDTO(featureDTO, existingFeature);
        existingFeature = featureService.save(existingFeature);
        return ResponseEntity.ok(featureMapper.toDTO(existingFeature));
    }

    @DeleteMapping("/{productId}/features/{featureId}")
    public ResponseEntity<Void> deleteProductFeature(
            @PathVariable Long productId,
            @PathVariable Long featureId) {
        Product product = productService.findById(productId);
        ProductFeature feature = featureService.findById(featureId);
        if (!feature.getProduct().getId().equals(productId)) {
            throw new IllegalArgumentException("Feature does not belong to product");
        }
        featureService.delete(feature);
        return ResponseEntity.ok().build();
    }
}
