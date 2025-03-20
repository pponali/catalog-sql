package com.nosql.poc.vendor.client;

import com.nosql.poc.vendor.model.Channel;
import com.nosql.poc.vendor.model.VendorChannel;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(
    name = "channel-service", 
    fallback = com.nosql.poc.vendor.client.fallback.ChannelServiceFallback.class,
    configuration = com.nosql.poc.vendor.config.FeignConfig.class
)
public interface ChannelServiceClient {
    
    @GetMapping("/api/channels/{channelId}")
    @CircuitBreaker(name = "channelService")
    @Retry(name = "channelService")
    Channel getChannel(@PathVariable("channelId") String channelId);
    
    @GetMapping("/api/channels")
    @CircuitBreaker(name = "channelService")
    List<Channel> getChannels(@RequestParam(required = false) String vendorId);
    
    @PostMapping("/api/channels/{channelId}/vendors/{vendorId}")
    @CircuitBreaker(name = "channelService")
    VendorChannel addVendorToChannel(@PathVariable("channelId") String channelId,
                                     @PathVariable("vendorId") String vendorId,
                                     @RequestBody VendorChannel vendorChannel);
    
    @PutMapping("/api/channels/{channelId}/vendors/{vendorId}")
    @CircuitBreaker(name = "channelService")
    VendorChannel updateVendorInChannel(@PathVariable("channelId") String channelId,
                                      @PathVariable("vendorId") String vendorId,
                                      @RequestBody VendorChannel vendorChannel);
}
