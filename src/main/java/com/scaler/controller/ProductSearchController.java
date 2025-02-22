package com.scaler.controller;

import com.scaler.dto.ProductDTO;
import com.scaler.dto.ProductSearchCriteria;
import com.scaler.service.ProductSearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/products/search")
@RequiredArgsConstructor
public class ProductSearchController {
    
    private final ProductSearchService productSearchService;
    
    @PostMapping
    public ResponseEntity<Page<ProductDTO>> searchProducts(@RequestBody ProductSearchCriteria criteria) {
        return ResponseEntity.ok(productSearchService.searchProducts(criteria));
    }
    
    @GetMapping("/by-seller/{sellerId}")
    public ResponseEntity<Page<ProductDTO>> getProductsBySeller(
            @PathVariable UUID sellerId,
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer size,
            @RequestParam(required = false) String sortBy,
            @RequestParam(required = false) String sortDirection) {
        
        ProductSearchCriteria criteria = ProductSearchCriteria.builder()
            .sellerId(sellerId)
            .page(page)
            .size(size)
            .sortBy(sortBy)
            .sortDirection(sortDirection)
            .build();
            
        return ResponseEntity.ok(productSearchService.searchProducts(criteria));
    }
    
    @GetMapping("/by-merchant/{merchantId}")
    public ResponseEntity<Page<ProductDTO>> getProductsByMerchant(
            @PathVariable UUID merchantId,
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer size,
            @RequestParam(required = false) String sortBy,
            @RequestParam(required = false) String sortDirection) {
        
        ProductSearchCriteria criteria = ProductSearchCriteria.builder()
            .merchantId(merchantId)
            .page(page)
            .size(size)
            .sortBy(sortBy)
            .sortDirection(sortDirection)
            .build();
            
        return ResponseEntity.ok(productSearchService.searchProducts(criteria));
    }
    
    @GetMapping("/by-channel/{channelId}")
    public ResponseEntity<Page<ProductDTO>> getProductsByChannel(
            @PathVariable UUID channelId,
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer size,
            @RequestParam(required = false) String sortBy,
            @RequestParam(required = false) String sortDirection) {
        
        ProductSearchCriteria criteria = ProductSearchCriteria.builder()
            .channelId(channelId)
            .page(page)
            .size(size)
            .sortBy(sortBy)
            .sortDirection(sortDirection)
            .build();
            
        return ResponseEntity.ok(productSearchService.searchProducts(criteria));
    }
}
