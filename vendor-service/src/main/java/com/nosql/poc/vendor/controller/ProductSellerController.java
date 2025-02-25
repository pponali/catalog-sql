package com.nosql.poc.vendor.controller;

import com.nosql.poc.vendor.model.ProductSeller;
import com.nosql.poc.vendor.service.ProductSellerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping(/product-sellers")
@RequiredArgsConstructor
@Validated
@Tag(name = "Product Seller Management", description = "APIs for managing product sellers")
public class ProductSellerController {

    private final ProductSellerService productSellerService;

    @PostMapping
    @Operation(summary = "Add a seller to a product")
    public ResponseEntity<ProductSeller> addSellerToProduct(@Valid @RequestBody ProductSeller productSeller) {
        ProductSeller saved = productSellerService.addSellerToProduct(productSeller);
        return ResponseEntity
            .created(URI.create(/product-sellers/" + saved.getId()))
            .body(saved);
    }

    @GetMapping("/products/{productId}")
    @Operation(summary = "Get all sellers for a product")
    public ResponseEntity<List<ProductSeller>> getProductSellers(@PathVariable String productId) {
        return ResponseEntity.ok(productSellerService.getProductSellers(productId));
    }

    @GetMapping("/products/{productId}/active")
    @Operation(summary = "Get active sellers for a product")
    public ResponseEntity<List<ProductSeller>> getActiveProductSellers(@PathVariable String productId) {
        return ResponseEntity.ok(productSellerService.getActiveProductSellers(productId));
    }

    @GetMapping("/products/{productId}/primary")
    @Operation(summary = "Get primary seller for a product")
    public ResponseEntity<ProductSeller> getPrimarySeller(@PathVariable String productId) {
        return productSellerService.getPrimarySeller(productId)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/products/{productId}/secondary")
    @Operation(summary = "Get secondary sellers for a product")
    public ResponseEntity<List<ProductSeller>> getSecondarySellers(@PathVariable String productId) {
        return ResponseEntity.ok(productSellerService.getSecondarySellers(productId));
    }

    @DeleteMapping("/products/{productId}/vendors/{vendorId}")
    @Operation(summary = "Deactivate a seller for a product")
    public ResponseEntity<Void> deactivateSeller(
            @PathVariable String productId,
            @PathVariable String vendorId) {
        productSellerService.deactivateSeller(productId, vendorId);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/products/{productId}/vendors/{vendorId}")
    @Operation(summary = "Update seller information for a product")
    public ResponseEntity<ProductSeller> updateSellerInfo(
            @PathVariable String productId,
            @PathVariable String vendorId,
            @Valid @RequestBody ProductSeller updatedInfo) {
        return ResponseEntity.ok(productSellerService.updateSellerInfo(productId, vendorId, updatedInfo));
    }
}
