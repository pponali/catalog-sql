package com.scaler.controller;

import com.scaler.entity.Catalog;
import com.scaler.entity.Channel;
import com.scaler.entity.ChannelCatalog;
import com.scaler.service.ChannelCatalogQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/channel-catalog/query")
@RequiredArgsConstructor
public class ChannelCatalogQueryController {
    
    private final ChannelCatalogQueryService channelCatalogQueryService;
    
    @GetMapping
    public ResponseEntity<List<ChannelCatalog>> getAllChannelCatalogs() {
        return ResponseEntity.ok(channelCatalogQueryService.getAllChannelCatalogs());
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<ChannelCatalog> getChannelCatalogById(@PathVariable UUID id) {
        return channelCatalogQueryService.getChannelCatalogById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping("/channel/{channelId}")
    public ResponseEntity<List<ChannelCatalog>> getChannelCatalogsByChannelId(@PathVariable UUID channelId) {
        return ResponseEntity.ok(channelCatalogQueryService.getChannelCatalogsByChannelId(channelId));
    }
    
    @GetMapping("/catalog/{catalogId}")
    public ResponseEntity<List<ChannelCatalog>> getChannelCatalogsByCatalogId(@PathVariable UUID catalogId) {
        return ResponseEntity.ok(channelCatalogQueryService.getChannelCatalogsByCatalogId(catalogId));
    }
    
    @GetMapping("/channel/{channelId}/catalog/{catalogId}")
    public ResponseEntity<ChannelCatalog> getChannelCatalogByChannelAndCatalog(
            @PathVariable UUID channelId,
            @PathVariable UUID catalogId) {
        return channelCatalogQueryService.getChannelCatalogByChannelAndCatalog(channelId, catalogId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping("/channel/{channelId}/catalogs")
    public ResponseEntity<List<Catalog>> getCatalogsByChannelId(@PathVariable UUID channelId) {
        return ResponseEntity.ok(channelCatalogQueryService.getCatalogsByChannelId(channelId));
    }
    
    @GetMapping("/catalog/{catalogId}/channels")
    public ResponseEntity<List<Channel>> getChannelsByCatalogId(@PathVariable UUID catalogId) {
        return ResponseEntity.ok(channelCatalogQueryService.getChannelsByCatalogId(catalogId));
    }
    
    @GetMapping("/active")
    public ResponseEntity<List<ChannelCatalog>> getActiveChannelCatalogs() {
        return ResponseEntity.ok(channelCatalogQueryService.getActiveChannelCatalogs());
    }
    
    @GetMapping("/channel/{channelId}/active")
    public ResponseEntity<List<ChannelCatalog>> getActiveChannelCatalogsByChannelId(@PathVariable UUID channelId) {
        return ResponseEntity.ok(channelCatalogQueryService.getActiveChannelCatalogsByChannelId(channelId));
    }
    
    @GetMapping("/catalog/{catalogId}/active")
    public ResponseEntity<List<ChannelCatalog>> getActiveChannelCatalogsByCatalogId(@PathVariable UUID catalogId) {
        return ResponseEntity.ok(channelCatalogQueryService.getActiveChannelCatalogsByCatalogId(catalogId));
    }
}