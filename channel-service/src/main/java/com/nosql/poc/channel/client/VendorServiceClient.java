package com.nosql.poc.channel.client;

import com.nosql.poc.channel.model.Vendor;
import com.nosql.poc.channel.model.VendorChannel;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(
    name = "vendor-service", 
    fallback = VendorServiceFallback.class
)
public interface VendorServiceClient {
    
    @GetMapping("/api/vendors/{vendorId}")
    @CircuitBreaker(name = "vendorService")
    @Retry(name = "vendorService")
    Vendor getVendor(@PathVariable("vendorId") String vendorId);
    
    @GetMapping("/api/vendors/{vendorId}/channels")
    @CircuitBreaker(name = "vendorService")
    List<VendorChannel> getVendorChannels(@PathVariable("vendorId") String vendorId);
    
    @PostMapping("/api/vendors/{vendorId}/channels")
    @CircuitBreaker(name = "vendorService")
    VendorChannel addVendorChannel(@PathVariable("vendorId") String vendorId, 
                                 @RequestBody VendorChannel channel);
    
    @PutMapping("/api/vendors/{vendorId}/channels/{channelId}")
    @CircuitBreaker(name = "vendorService")
    VendorChannel updateVendorChannel(@PathVariable("vendorId") String vendorId,
                                    @PathVariable("channelId") String channelId,
                                    @RequestBody VendorChannel channel);
}
