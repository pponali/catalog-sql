package com.scaler.controller;

import com.scaler.entity.Product;
import com.scaler.entity.Seller;
import com.scaler.entity.SellerProduct;
import com.scaler.service.SellerProductQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/seller-product/query")
@RequiredArgsConstructor
public class SellerProductQueryController {
    
    private final SellerProductQueryService sellerProductQueryService;
    
    @GetMapping
    public ResponseEntity<List<SellerProduct>> getAllSellerProducts() {
        return ResponseEntity.ok(sellerProductQueryService.getAllSellerProducts());
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<SellerProduct> getSellerProductById(@PathVariable UUID id) {
        return sellerProductQueryService.getSellerProductById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping("/product/{productId}")
    public ResponseEntity<List<SellerProduct>> getSellerProductsByProductId(@PathVariable UUID productId) {
        return ResponseEntity.ok(sellerProductQueryService.getSellerProductsByProductId(productId));
    }
    
    @GetMapping("/seller/{sellerId}")
    public ResponseEntity<List<SellerProduct>> getSellerProductsBySellerId(@PathVariable UUID sellerId) {
        return ResponseEntity.ok(sellerProductQueryService.getSellerProductsBySellerId(sellerId));
    }
    
    @GetMapping("/merchant/{merchantId}")
    public ResponseEntity<List<SellerProduct>> getSellerProductsByMerchantId(@PathVariable UUID merchantId) {
        return ResponseEntity.ok(sellerProductQueryService.getSellerProductsByMerchantId(merchantId));
    }
    
    @GetMapping("/product/{productId}/seller/{sellerId}")
    public ResponseEntity<SellerProduct> getSellerProductByProductAndSeller(
            @PathVariable UUID productId,
            @PathVariable UUID sellerId) {
        return sellerProductQueryService.getSellerProductByProductAndSeller(productId, sellerId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping("/product/{productId}/seller/{sellerId}/merchant/{merchantId}")
    public ResponseEntity<SellerProduct> getSellerProductByProductSellerAndMerchant(
            @PathVariable UUID productId,
            @PathVariable UUID sellerId,
            @PathVariable UUID merchantId) {
        return sellerProductQueryService.getSellerProductByProductSellerAndMerchant(productId, sellerId, merchantId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping("/product/{productId}/sellers")
    public ResponseEntity<List<Seller>> getSellersByProductId(@PathVariable UUID productId) {
        return ResponseEntity.ok(sellerProductQueryService.getSellersByProductId(productId));
    }
    
    @GetMapping("/seller/{sellerId}/products")
    public ResponseEntity<List<Product>> getProductsBySellerId(@PathVariable UUID sellerId) {
        return ResponseEntity.ok(sellerProductQueryService.getProductsBySellerId(sellerId));
    }
    
    @GetMapping("/seller/{sellerId}/merchant/{merchantId}/products")
    public ResponseEntity<List<Product>> getProductsBySellerIdAndMerchantId(
            @PathVariable UUID sellerId,
            @PathVariable UUID merchantId) {
        return ResponseEntity.ok(sellerProductQueryService.getProductsBySellerIdAndMerchantId(sellerId, merchantId));
    }
}