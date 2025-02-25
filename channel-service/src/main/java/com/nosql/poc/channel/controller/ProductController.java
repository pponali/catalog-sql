package com.nosql.poc.channel.controller;

import com.nosql.poc.channel.model.ChannelProduct;
import com.nosql.poc.channel.service.ChannelProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping(/channels")
@RequiredArgsConstructor
public class ChannelProductController {

    private final ChannelProductService channelProductService;

    @GetMapping("/{channelId}/products/{productId}")
    public ResponseEntity<ChannelProduct> getChannelProductDetails(
            @PathVariable String channelId,
            @PathVariable String productId) {
        return ResponseEntity.ok(channelProductService.getChannelProduct(channelId, productId));
    }

    @GetMapping("/{channelId}/products")
    public ResponseEntity<List<ChannelProduct>> getChannelProducts(
            @PathVariable String channelId,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer size) {
        return ResponseEntity.ok(channelProductService.getChannelProducts(channelId, category, page, size));
    }

    @PostMapping("/{channelId}/products/{productId}/activate")
    public ResponseEntity<Void> activateProductInChannel(
            @PathVariable String channelId,
            @PathVariable String productId) {
        channelProductService.activateProductInChannel(channelId, productId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{channelId}/products/{productId}/deactivate")
    public ResponseEntity<Void> deactivateProductInChannel(
            @PathVariable String channelId,
            @PathVariable String productId) {
        channelProductService.deactivateProductInChannel(channelId, productId);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{channelId}/products/{productId}/price")
    public ResponseEntity<Void> updateChannelPrice(
            @PathVariable String channelId,
            @PathVariable String productId,
            @Valid @RequestBody ChannelPrice price) {
        channelProductService.updateChannelPrice(channelId, productId, price);
        return ResponseEntity.ok().build();
    }
}
