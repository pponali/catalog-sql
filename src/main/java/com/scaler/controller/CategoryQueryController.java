package com.scaler.controller;

import com.scaler.entity.Category;
import com.scaler.service.ProductCategoryQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/category/query")
@RequiredArgsConstructor
public class CategoryQueryController {
    
    private final ProductCategoryQueryService productCategoryQueryService;
    
    @GetMapping
    public ResponseEntity<List<Category>> getAllCategories() {
        // Get all categories from product categories
        return ResponseEntity.ok(productCategoryQueryService.getAllProductCategories().stream()
                .map(pc -> pc.getCategory())
                .distinct()
                .collect(Collectors.toList()));
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<Category> getCategoryById(@PathVariable UUID id) {
        // Get category by ID
        return productCategoryQueryService.getProductCategoriesByCategoryId(id).stream()
                .findFirst()
                .map(pc -> ResponseEntity.ok(pc.getCategory()))
                .orElse(ResponseEntity.notFound().build());
    }
}