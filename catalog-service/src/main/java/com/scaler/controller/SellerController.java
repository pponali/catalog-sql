package com.scaler.controller;

import com.scaler.dto.ProductDTO;
import com.scaler.dto.SellerDTO;
import com.scaler.service.SellerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/sellers")
@RequiredArgsConstructor
public class SellerController {
    
    private final SellerService sellerService;

    @PostMapping
    public ResponseEntity<SellerDTO> createSeller(@Valid @RequestBody SellerDTO sellerDTO) {
        SellerDTO createdSeller = sellerService.createSeller(sellerDTO);
        URI location = ServletUriComponentsBuilder
            .fromCurrentRequest()
            .path("/{id}")
            .buildAndExpand(createdSeller.getId())
            .toUri();
        return ResponseEntity.created(location).body(createdSeller);
    }

    @PutMapping("/{id}")
    public ResponseEntity<SellerDTO> updateSeller(
            @PathVariable UUID id,
            @Valid @RequestBody SellerDTO sellerDTO) {
        return ResponseEntity.ok(sellerService.updateSeller(id, sellerDTO));
    }

    @GetMapping("/{id}")
    public ResponseEntity<SellerDTO> getSeller(@PathVariable UUID id) {
        return ResponseEntity.ok(sellerService.getSeller(id));
    }

    @GetMapping
    public ResponseEntity<List<SellerDTO>> getAllSellers(
            @RequestParam(required = false) UUID merchantId) {
        if (merchantId != null) {
            return ResponseEntity.ok(sellerService.getSellersByMerchant(merchantId));
        }
        return ResponseEntity.ok(sellerService.getAllSellers());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSeller(@PathVariable UUID id) {
        sellerService.deleteSeller(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}/products")
    public ResponseEntity<List<ProductDTO>> getSellerProducts(@PathVariable UUID id) {
        return ResponseEntity.ok(sellerService.getSellerProducts(id));
    }

    @PostMapping("/{id}/products/{productId}")
    public ResponseEntity<Void> addProductToSeller(
            @PathVariable UUID id,
            @PathVariable UUID productId) {
        sellerService.addProductToSeller(id, productId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}/products/{productId}")
    public ResponseEntity<Void> removeProductFromSeller(
            @PathVariable UUID id,
            @PathVariable UUID productId) {
        sellerService.removeProductFromSeller(id, productId);
        return ResponseEntity.noContent().build();
    }
}
