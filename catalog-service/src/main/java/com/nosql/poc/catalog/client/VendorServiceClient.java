package com.nosql.poc.catalog.client;

import com.nosql.poc.catalog.model.Vendor;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "vendor-service")
public interface VendorServiceClient {
    
    @GetMapping("/api/vendors/{vendorId}")
    @CircuitBreaker(name = "vendorService")
    @Retry(name = "vendorService")
    Vendor getVendor(@PathVariable("vendorId") String vendorId);
    
    @GetMapping("/api/vendors/{vendorId}/validate")
    @CircuitBreaker(name = "vendorService")
    boolean validateVendor(@PathVariable("vendorId") String vendorId);
    
    @PostMapping("/api/vendors/{vendorId}/products/{productId}")
    @CircuitBreaker(name = "vendorService")
    void notifyProductUpdate(@PathVariable("vendorId") String vendorId,
                           @PathVariable("productId") String productId);
}
