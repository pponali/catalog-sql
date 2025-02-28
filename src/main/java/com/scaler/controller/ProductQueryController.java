package com.scaler.controller;

import com.scaler.entity.*;
import com.scaler.service.ProductQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/query/products")
@RequiredArgsConstructor
public class ProductQueryController {
    
    private final ProductQueryService productQueryService;
    
    // Get single product by ID
    @GetMapping("/{productId}")
    public ResponseEntity<Product> getProductById(
            @PathVariable UUID productId) {
        return productQueryService.getProductById(productId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Get single product by ID and Merchant
    @GetMapping("/{productId}/merchant/{merchantId}")
    public ResponseEntity<Product> getProductByIdAndMerchant(
            @PathVariable UUID productId,
            @PathVariable UUID merchantId) {
        return productQueryService.getProductByIdAndMerchant(productId, merchantId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Get single product by ID and Channel
    @GetMapping("/{productId}/channel/{channelId}")
    public ResponseEntity<Product> getProductByIdAndChannel(
            @PathVariable UUID productId,
            @PathVariable UUID channelId) {
        return productQueryService.getProductByIdAndChannel(productId, channelId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Get single product by ID and Seller
    @GetMapping("/{productId}/seller/{sellerId}")
    public ResponseEntity<Product> getProductByIdAndSeller(
            @PathVariable UUID productId,
            @PathVariable UUID sellerId) {
        return productQueryService.getProductByIdAndSeller(productId, sellerId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Get single product by ID and Category
    @GetMapping("/{productId}/category/{categoryId}")
    public ResponseEntity<Product> getProductByIdAndCategory(
            @PathVariable UUID productId,
            @PathVariable UUID categoryId) {
        return productQueryService.getProductByIdAndCategory(productId, categoryId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /*// Get single product by ID, Merchant and Channel
    @GetMapping("/{productId}/merchant/{merchantId}/channel/{channelId}")
    public ResponseEntity<Product> getProductByIdMerchantAndChannel(
            @PathVariable UUID productId,
            @PathVariable UUID merchantId,
            @PathVariable UUID channelId) {
        return productQueryService.getProductByIdMerchantAndChannel(productId, merchantId, channelId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Get single product by ID, Merchant and Category
    @GetMapping("/{productId}/merchant/{merchantId}/category/{categoryId}")
    public ResponseEntity<Product> getProductByIdMerchantAndCategory(
            @PathVariable UUID productId,
            @PathVariable UUID merchantId,
            @PathVariable UUID categoryId) {
        return productQueryService.getProductByIdMerchantAndCategory(productId, merchantId, categoryId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }*/

    // Get single product by ID, Seller and Category
    @GetMapping("/{productId}/seller/{sellerId}/category/{categoryId}")
    public ResponseEntity<Product> getProductByIdSellerAndCategory(
            @PathVariable UUID productId,
            @PathVariable UUID sellerId,
            @PathVariable UUID categoryId) {
        return productQueryService.getProductByIdSellerAndCategory(productId, sellerId, categoryId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Get single product by ID, Channel and Category
    @GetMapping("/{productId}/channel/{channelId}/category/{categoryId}")
    public ResponseEntity<Product> getProductByIdChannelAndCategory(
            @PathVariable UUID productId,
            @PathVariable UUID channelId,
            @PathVariable UUID categoryId) {
        return productQueryService.getProductByIdChannelAndCategory(productId, channelId, categoryId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /*// Get single product by ID, Merchant, Channel and Seller
    @GetMapping("/{productId}/merchant/{merchantId}/channel/{channelId}/seller/{sellerId}")
    public ResponseEntity<Product> getProductByIdMerchantChannelAndSeller(
            @PathVariable UUID productId,
            @PathVariable UUID merchantId,
            @PathVariable UUID channelId,
            @PathVariable UUID sellerId) {
        return productQueryService.getProductByIdMerchantChannelAndSeller(productId, merchantId, channelId, sellerId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Get single product by ID, Merchant, Channel and Category
    @GetMapping("/{productId}/merchant/{merchantId}/channel/{channelId}/category/{categoryId}")
    public ResponseEntity<Product> getProductByIdMerchantChannelAndCategory(
            @PathVariable UUID productId,
            @PathVariable UUID merchantId,
            @PathVariable UUID channelId,
            @PathVariable UUID categoryId) {
        return productQueryService.getProductByIdMerchantChannelAndCategory(productId, merchantId, channelId, categoryId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Get single product by ID, Merchant, Seller and Category
    @GetMapping("/{productId}/merchant/{merchantId}/seller/{sellerId}/category/{categoryId}")
    public ResponseEntity<Product> getProductByIdMerchantSellerAndCategory(
            @PathVariable UUID productId,
            @PathVariable UUID merchantId,
            @PathVariable UUID sellerId,
            @PathVariable UUID categoryId) {
        return productQueryService.getProductByIdMerchantSellerAndCategory(productId, merchantId, sellerId, categoryId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }*/

    // Get single product by ID, Channel, Seller and Category
    @GetMapping("/{productId}/channel/{channelId}/seller/{sellerId}/category/{categoryId}")
    public ResponseEntity<Product> getProductByIdChannelSellerAndCategory(
            @PathVariable UUID productId,
            @PathVariable UUID channelId,
            @PathVariable UUID sellerId,
            @PathVariable UUID categoryId) {
        return productQueryService.getProductByIdChannelSellerAndCategory(productId, channelId, sellerId, categoryId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Get single product by ID, Merchant, Channel, Seller and Category
    @GetMapping("/{productId}/merchant/{merchantId}/channel/{channelId}/seller/{sellerId}/category/{categoryId}")
    public ResponseEntity<Product> getProductByIdMerchantChannelSellerAndCategory(
            @PathVariable UUID productId,
            @PathVariable UUID merchantId,
            @PathVariable UUID channelId,
            @PathVariable UUID sellerId,
            @PathVariable UUID categoryId) {
        return productQueryService.getProductByIdMerchantChannelSellerAndCategory(productId, merchantId, channelId, sellerId, categoryId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Product pricing
    @GetMapping("/{productId}/pricing")
    public ResponseEntity<List<ProductPrice>> getProductPricing(
            @PathVariable UUID productId,
            @RequestParam(required = false) UUID channelId,
            @RequestParam(required = false) UUID sellerId) { // sellerId is ignored as ProductPrice no longer has seller relationship
        return ResponseEntity.ok(productQueryService.getProductPricing(productId, channelId, sellerId));
    }

    // Product inventory
    @GetMapping("/{productId}/inventory")
    public ResponseEntity<ProductInventory> getProductInventory(
            @PathVariable UUID productId,
            @RequestParam(required = false) UUID merchantId,
            @RequestParam(required = false) UUID channelId,
            @RequestParam(required = false) UUID sellerId) {
        return productQueryService.getProductInventory(productId, merchantId, channelId, sellerId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Get single product by ID, Channel, Seller and Category (without merchant)
    @GetMapping("/{productId}/channel/{channelId}/seller/{sellerId}/category/{categoryId}/no-merchant")
    public ResponseEntity<Product> getProductByIdChannelSellerAndCategoryNoMerchant(
            @PathVariable UUID productId,
            @PathVariable UUID channelId,
            @PathVariable UUID sellerId,
            @PathVariable UUID categoryId) {
        return productQueryService.getProductByIdChannelSellerAndCategory(productId, channelId, sellerId, categoryId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Get single product by ID, Seller and Category (without merchant)
    @GetMapping("/{productId}/seller/{sellerId}/category/{categoryId}/no-merchant")
    public ResponseEntity<Product> getProductByIdSellerAndCategoryNoMerchant(
            @PathVariable UUID productId,
            @PathVariable UUID sellerId,
            @PathVariable UUID categoryId) {
        return productQueryService.getProductByIdSellerAndCategory(productId, sellerId, categoryId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Get single product by ID, Channel and Category (without merchant)
    @GetMapping("/{productId}/channel/{channelId}/category/{categoryId}/no-merchant")
    public ResponseEntity<Product> getProductByIdChannelAndCategoryNoMerchant(
            @PathVariable UUID productId,
            @PathVariable UUID channelId,
            @PathVariable UUID categoryId) {
        return productQueryService.getProductByIdChannelAndCategory(productId, channelId, categoryId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Get single product by ID, Channel and Seller (without merchant)
    @GetMapping("/{productId}/channel/{channelId}/seller/{sellerId}/no-merchant")
    public ResponseEntity<Product> getProductByIdChannelAndSellerNoMerchant(
            @PathVariable UUID productId,
            @PathVariable UUID channelId,
            @PathVariable UUID sellerId) {
        return productQueryService.getProductByIdChannelAndSeller(productId, channelId, sellerId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
