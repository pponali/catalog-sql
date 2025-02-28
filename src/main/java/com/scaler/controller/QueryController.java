package com.scaler.controller;

import com.scaler.dto.*;
import com.scaler.entity.*;
import com.scaler.service.QueryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/query")
public class QueryController {

    @Autowired
    private QueryService queryService;

    @GetMapping("/products")
    public ResponseEntity<List<Product>> getAllProducts() {
        return ResponseEntity.ok(queryService.getAllProducts());
    }

    @GetMapping("/categories")
    public ResponseEntity<List<Category>> getAllCategories() {
        return ResponseEntity.ok(queryService.getAllCategories());
    }

    @GetMapping("/channels")
    public ResponseEntity<List<Channel>> getAllChannels() {
        return ResponseEntity.ok(queryService.getAllChannels());
    }

    @GetMapping("/merchants")
    public ResponseEntity<List<Merchant>> getAllMerchants() {
        return ResponseEntity.ok(queryService.getAllMerchants());
    }

    @GetMapping("/sellers")
    public ResponseEntity<List<Seller>> getAllSellers() {
        return ResponseEntity.ok(queryService.getAllSellers());
    }

    @GetMapping("/products/category/{categoryId}")
    public ResponseEntity<List<Product>> getProductsByCategory(@PathVariable UUID categoryId) {
        return ResponseEntity.ok(queryService.getProductsByCategory(categoryId));
    }

    @GetMapping("/products/channel/{channelId}")
    public ResponseEntity<List<Product>> getProductsByChannel(@PathVariable UUID channelId) {
        return ResponseEntity.ok(queryService.getProductsByChannel(channelId));
    }

    @GetMapping("/products/seller/{sellerId}")
    public ResponseEntity<List<Product>> getProductsBySeller(@PathVariable UUID sellerId) {
        return ResponseEntity.ok(queryService.getProductsBySeller(sellerId));
    }

    @GetMapping("/products/merchant/{merchantId}")
    public ResponseEntity<List<Product>> getProductsByMerchant(@PathVariable UUID merchantId) {
        return ResponseEntity.ok(queryService.getProductsByMerchant(merchantId));
    }

    @GetMapping("/categories/channel/{channelId}")
    public ResponseEntity<List<Category>> getCategoriesByChannel(@PathVariable UUID channelId) {
        return ResponseEntity.ok(queryService.getCategoriesByChannel(channelId));
    }

    @GetMapping("/sellers/channel/{channelId}")
    public ResponseEntity<List<Seller>> getSellersByChannel(@PathVariable UUID channelId) {
        return ResponseEntity.ok(queryService.getSellersByChannel(channelId));
    }

    @GetMapping("/merchants/channel/{channelId}")
    public ResponseEntity<List<Merchant>> getMerchantsByChannel(@PathVariable UUID channelId) {
        return ResponseEntity.ok(queryService.getMerchantsByChannel(channelId));
    }
}
