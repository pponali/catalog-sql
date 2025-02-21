package com.scaler.controller;

import com.scaler.entity.*;
import com.scaler.service.ProductQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/products")
@RequiredArgsConstructor
public class ProductQueryController {
    
    private final ProductQueryService productQueryService;
    
    @GetMapping("/merchant/{merchantId}/channel/{channelId}")
    public ResponseEntity<List<Product>> getProductsByMerchantAndChannel(
            @PathVariable Long merchantId,
            @PathVariable Long channelId) {
        return ResponseEntity.ok(productQueryService.getProductsByMerchantAndChannel(merchantId, channelId));
    }
    
    @GetMapping("/{productId}/pricing")
    public ResponseEntity<List<ProductPrice>> getProductPricing(
            @PathVariable Long productId,
            @RequestParam Long channelId,
            @RequestParam Long lineOfBusinessId) {
        return ResponseEntity.ok(productQueryService.getProductPricing(productId, channelId, lineOfBusinessId));
    }
    
    @GetMapping("/{productId}/inventory")
    public ResponseEntity<ProductInventory> getProductInventory(
            @PathVariable Long productId,
            @RequestParam Long merchantId,
            @RequestParam Long channelId) {
        return productQueryService.getProductInventory(productId, merchantId, channelId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping("/catalog")
    public ResponseEntity<Catalog> getCatalog(
            @RequestParam Long merchantId,
            @RequestParam Long channelId,
            @RequestParam Long lineOfBusinessId) {
        return productQueryService.getCatalogByMerchantChannelAndLob(merchantId, channelId, lineOfBusinessId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
