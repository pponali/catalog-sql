package com.nosql.poc.catalog.client;

import com.nosql.poc.catalog.model.Channel;
import com.nosql.poc.catalog.model.ChannelCatalog;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(name = "channel-service")
public interface ChannelServiceClient {
    
    @GetMapping("/api/channels/{channelId}")
    @CircuitBreaker(name = "channelService")
    @Retry(name = "channelService")
    Channel getChannel(@PathVariable("channelId") String channelId);
    
    @GetMapping("/api/channels")
    @CircuitBreaker(name = "channelService")
    List<Channel> getChannels(@RequestParam(required = false) String vendorId);
    
    @GetMapping("/api/channels/{channelId}/catalog")
    @CircuitBreaker(name = "channelService")
    ChannelCatalog getChannelCatalog(@PathVariable("channelId") String channelId);
    
    @PostMapping("/api/channels/{channelId}/products/{productId}/sync")
    @CircuitBreaker(name = "channelService")
    void syncProductToChannel(@PathVariable("channelId") String channelId,
                            @PathVariable("productId") String productId);
}
