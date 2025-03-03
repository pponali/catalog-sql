package com.scaler.controller;

import com.scaler.entity.Channel;
import com.scaler.entity.Product;
import com.scaler.entity.ProductChannel;
import com.scaler.service.ProductChannelQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/product-channel/query")
@RequiredArgsConstructor
public class ProductChannelQueryController {
    
    private final ProductChannelQueryService productChannelQueryService;
    
    @GetMapping
    public ResponseEntity<List<ProductChannel>> getAllProductChannels() {
        return ResponseEntity.ok(productChannelQueryService.getAllProductChannels());
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<ProductChannel> getProductChannelById(@PathVariable UUID id) {
        return productChannelQueryService.getProductChannelById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping("/product/{productId}")
    public ResponseEntity<List<ProductChannel>> getProductChannelsByProductId(@PathVariable UUID productId) {
        return ResponseEntity.ok(productChannelQueryService.getProductChannelsByProductId(productId));
    }
    
    @GetMapping("/channel/{channelId}")
    public ResponseEntity<List<ProductChannel>> getProductChannelsByChannelId(@PathVariable UUID channelId) {
        return ResponseEntity.ok(productChannelQueryService.getProductChannelsByChannelId(channelId));
    }
    
    @GetMapping("/product/{productId}/channel/{channelId}")
    public ResponseEntity<ProductChannel> getProductChannelByProductAndChannel(
            @PathVariable UUID productId,
            @PathVariable UUID channelId) {
        return productChannelQueryService.getProductChannelByProductAndChannel(productId, channelId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping("/product/{productId}/channels")
    public ResponseEntity<List<Channel>> getChannelsByProductId(@PathVariable UUID productId) {
        return ResponseEntity.ok(productChannelQueryService.getChannelsByProductId(productId));
    }
    
    @GetMapping("/channel/{channelId}/products")
    public ResponseEntity<List<Product>> getProductsByChannelId(@PathVariable UUID channelId) {
        return ResponseEntity.ok(productChannelQueryService.getProductsByChannelId(channelId));
    }
    
    @GetMapping("/channel/{channelId}/active/{isActive}/products")
    public ResponseEntity<List<Product>> getProductsByChannelIdAndActive(
            @PathVariable UUID channelId,
            @PathVariable boolean isActive) {
        return ResponseEntity.ok(productChannelQueryService.getProductsByChannelIdAndActive(channelId, isActive));
    }
}