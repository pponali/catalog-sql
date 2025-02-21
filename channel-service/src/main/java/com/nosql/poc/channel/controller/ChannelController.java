package com.nosql.poc.channel.controller;

import com.nosql.poc.channel.model.*;
import com.nosql.poc.channel.service.ChannelService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/v1/channels")
@RequiredArgsConstructor
public class ChannelController {
    
    private final ChannelService channelService;
    
    @PostMapping
    public ResponseEntity<Channel> createChannel(@Valid @RequestBody Channel channel) {
        return ResponseEntity.ok(channelService.createChannel(channel));
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<Channel> updateChannel(@PathVariable String id, 
        @Valid @RequestBody Channel channel) {
        return ResponseEntity.ok(channelService.updateChannel(id, channel));
    }
    
    @PutMapping("/{id}/activate")
    public ResponseEntity<Void> activateChannel(@PathVariable String id) {
        channelService.activateChannel(id);
        return ResponseEntity.ok().build();
    }
    
    @PutMapping("/{id}/deactivate")
    public ResponseEntity<Void> deactivateChannel(@PathVariable String id) {
        channelService.deactivateChannel(id);
        return ResponseEntity.ok().build();
    }
    
    @PutMapping("/{id}/metrics")
    public ResponseEntity<Void> updateChannelMetrics(@PathVariable String id, 
        @Valid @RequestBody ChannelMetrics metrics) {
        channelService.updateChannelMetrics(id, metrics);
        return ResponseEntity.ok().build();
    }
    
    @GetMapping
    public ResponseEntity<List<Channel>> getChannels(
        @RequestParam(required = false) String type,
        @RequestParam(required = false) String entityId,
        @RequestParam(required = false) Double minQualityScore) {
        
        List<Channel> channels;
        if (type != null) {
            channels = channelService.getChannelsByType(type);
        } else if (entityId != null) {
            channels = channelService.getChannelsByEntityId(entityId);
        } else if (minQualityScore != null) {
            channels = channelService.getChannelsByMinimumQualityScore(minQualityScore);
        } else {
            channels = channelService.getActiveChannels();
        }
        
        return ResponseEntity.ok(channels);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<Channel> getChannel(@PathVariable String id) {
        return ResponseEntity.ok(channelService.getChannel(id));
    }
}
