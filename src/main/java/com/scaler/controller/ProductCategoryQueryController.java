package com.scaler.controller;

import com.scaler.entity.Category;
import com.scaler.entity.Product;
import com.scaler.entity.ProductCategory;
import com.scaler.service.ProductCategoryQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/product-category/query")
@RequiredArgsConstructor
public class ProductCategoryQueryController {
    
    private final ProductCategoryQueryService productCategoryQueryService;
    
    @GetMapping
    public ResponseEntity<List<ProductCategory>> getAllProductCategories() {
        return ResponseEntity.ok(productCategoryQueryService.getAllProductCategories());
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<ProductCategory> getProductCategoryById(@PathVariable UUID id) {
        return productCategoryQueryService.getProductCategoryById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping("/product/{productId}")
    public ResponseEntity<List<ProductCategory>> getProductCategoriesByProductId(@PathVariable UUID productId) {
        return ResponseEntity.ok(productCategoryQueryService.getProductCategoriesByProductId(productId));
    }
    
    @GetMapping("/category/{categoryId}")
    public ResponseEntity<List<ProductCategory>> getProductCategoriesByCategoryId(@PathVariable UUID categoryId) {
        return ResponseEntity.ok(productCategoryQueryService.getProductCategoriesByCategoryId(categoryId));
    }
    
    @GetMapping("/merchant/{merchantId}")
    public ResponseEntity<List<ProductCategory>> getProductCategoriesByMerchantId(@PathVariable UUID merchantId) {
        return ResponseEntity.ok(productCategoryQueryService.getProductCategoriesByMerchantId(merchantId));
    }
    
    @GetMapping("/product/{productId}/category/{categoryId}")
    public ResponseEntity<ProductCategory> getProductCategoryByProductAndCategory(
            @PathVariable UUID productId,
            @PathVariable UUID categoryId) {
        return productCategoryQueryService.getProductCategoryByProductAndCategory(productId, categoryId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping("/product/{productId}/category/{categoryId}/merchant/{merchantId}")
    public ResponseEntity<ProductCategory> getProductCategoryByProductCategoryAndMerchant(
            @PathVariable UUID productId,
            @PathVariable UUID categoryId,
            @PathVariable UUID merchantId) {
        return productCategoryQueryService.getProductCategoryByProductCategoryAndMerchant(productId, categoryId, merchantId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping("/product/{productId}/categories")
    public ResponseEntity<List<Category>> getCategoriesByProductId(@PathVariable UUID productId) {
        return ResponseEntity.ok(productCategoryQueryService.getCategoriesByProductId(productId));
    }
    
    @GetMapping("/category/{categoryId}/products")
    public ResponseEntity<List<Product>> getProductsByCategoryId(@PathVariable UUID categoryId) {
        return ResponseEntity.ok(productCategoryQueryService.getProductsByCategoryId(categoryId));
    }
    
    @GetMapping("/category/{categoryId}/merchant/{merchantId}/products")
    public ResponseEntity<List<Product>> getProductsByCategoryIdAndMerchantId(
            @PathVariable UUID categoryId,
            @PathVariable UUID merchantId) {
        return ResponseEntity.ok(productCategoryQueryService.getProductsByCategoryIdAndMerchantId(categoryId, merchantId));
    }
}