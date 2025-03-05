package com.scaler.controller;

import com.scaler.entity.Product;
import com.scaler.service.SellerProductQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/product/query")
@RequiredArgsConstructor
public class ProductQueryController {
    
    private final SellerProductQueryService sellerProductQueryService;
    
    @GetMapping
    public ResponseEntity<List<Product>> getAllProducts() {
        // Get all products from seller products
        return ResponseEntity.ok(sellerProductQueryService.getAllSellerProducts().stream()
                .map(sp -> sp.getProduct())
                .distinct()
                .collect(Collectors.toList()));
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<Product> getProductById(@PathVariable UUID id) {
        // Get product by ID
        return sellerProductQueryService.getSellerProductsByProductId(id).stream()
                .findFirst()
                .map(sp -> ResponseEntity.ok(sp.getProduct()))
                .orElse(ResponseEntity.notFound().build());
    }
}
