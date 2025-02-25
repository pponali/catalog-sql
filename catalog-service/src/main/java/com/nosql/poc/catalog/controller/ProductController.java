package com.nosql.poc.catalog.controller;

import com.nosql.poc.catalog.model.Product;
import com.nosql.poc.catalog.model.Price;
import com.nosql.poc.catalog.model.Inventory;
import com.nosql.poc.catalog.model.ValidationStatus;
import com.nosql.poc.catalog.service.ProductService;
import com.nosql.poc.catalog.dto.ProductResponse;
import com.nosql.poc.catalog.dto.ProductRequest;
import com.nosql.poc.catalog.dto.PriceRequest;
import com.nosql.poc.catalog.dto.InventoryRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;

import java.net.URI;
import java.util.List;

@Slf4j
@Validated
@RestController
@RequestMapping(/products")
@RequiredArgsConstructor
@Tag(name = "Product Management", description = "APIs for managing product catalog")
public class ProductController {
    
    private final ProductService productService;
    
    @Operation(
        summary = "Create a new product",
        description = "Creates a new product in the catalog with basic validation"
    )
    @ApiResponse(responseCode = "201", description = "Product created successfully")
    @ApiResponse(responseCode = "400", description = "Invalid product data")
    @PostMapping
    public ResponseEntity<ProductResponse> createProduct(
            @Valid @RequestBody ProductRequest request) {
        log.info("Creating new product with name: {}", request.getName());
        Product product = productService.createProduct(request.toProduct());
        return ResponseEntity
            .created(URI.create(/products/" + product.getId()))
            .body(ProductResponse.fromProduct(product));
    }
    
    @Operation(
        summary = "Get product by ID",
        description = "Retrieves detailed product information by its unique identifier"
    )
    @ApiResponse(responseCode = "200", description = "Product found")
    @ApiResponse(responseCode = "404", description = "Product not found")
    @GetMapping("/{id}")
    public ResponseEntity<ProductResponse> getProduct(
            @Parameter(description = "Product ID", required = true)
            @PathVariable @NotBlank String id) {
        log.info("Fetching product with id: {}", id);
        Product product = productService.getProduct(id);
        return ResponseEntity.ok(ProductResponse.fromProduct(product));
    }
    
    @Operation(
        summary = "Search products",
        description = "Search products with optional filters and pagination"
    )
    @GetMapping
    public ResponseEntity<Page<ProductResponse>> searchProducts(
            @Parameter(description = "Entity ID filter")
            @RequestParam(required = false) String entityId,
            @Parameter(description = "Category filter")
            @RequestParam(required = false) String category,
            @Parameter(description = "Channel ID filter")
            @RequestParam(required = false) String channelId,
            @Parameter(description = "Pagination parameters")
            @PageableDefault(size = 20) Pageable pageable) {
        log.info("Searching products with filters - entityId: {}, category: {}, channelId: {}", 
                entityId, category, channelId);
        Page<Product> products = productService.searchProducts(entityId, category, channelId, pageable);
        return ResponseEntity.ok(products.map(ProductResponse::fromProduct));
    }
    
    @Operation(
        summary = "Update product",
        description = "Updates an existing product's information"
    )
    @ApiResponse(responseCode = "200", description = "Product updated successfully")
    @ApiResponse(responseCode = "404", description = "Product not found")
    @PutMapping("/{id}")
    public ResponseEntity<ProductResponse> updateProduct(
            @Parameter(description = "Product ID", required = true)
            @PathVariable @NotBlank String id,
            @Valid @RequestBody ProductRequest request) {
        log.info("Updating product with id: {}", id);
        Product product = productService.updateProduct(id, request.toProduct());
        return ResponseEntity.ok(ProductResponse.fromProduct(product));
    }
    
    @Operation(
        summary = "Update product price",
        description = "Updates the price information for a specific product"
    )
    @PutMapping("/{id}/price")
    public ResponseEntity<Void> updatePrice(
            @Parameter(description = "Product ID", required = true)
            @PathVariable @NotBlank String id,
            @Valid @RequestBody PriceRequest request) {
        log.info("Updating price for product id: {}", id);
        productService.updatePrice(id, request.toPrice());
        return ResponseEntity.ok().build();
    }
    
    @Operation(
        summary = "Update product inventory",
        description = "Updates the inventory information for a specific product"
    )
    @PutMapping("/{id}/inventory")
    public ResponseEntity<Void> updateInventory(
            @Parameter(description = "Product ID", required = true)
            @PathVariable @NotBlank String id,
            @Valid @RequestBody InventoryRequest request) {
        log.info("Updating inventory for product id: {}", id);
        productService.updateInventory(id, request.toInventory());
        return ResponseEntity.ok().build();
    }
    
    @Operation(
        summary = "Delete product",
        description = "Removes a product from the catalog"
    )
    @ApiResponse(responseCode = "200", description = "Product deleted successfully")
    @ApiResponse(responseCode = "404", description = "Product not found")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(
            @Parameter(description = "Product ID", required = true)
            @PathVariable @NotBlank String id) {
        log.info("Deleting product with id: {}", id);
        productService.deleteProduct(id);
        return ResponseEntity.ok().build();
    }
    
    @Operation(
        summary = "Validate product",
        description = "Validates a product against business rules and optionally channel-specific rules"
    )
    @PostMapping("/{id}/validate")
    public ResponseEntity<ValidationStatus> validateProduct(
            @Parameter(description = "Product ID", required = true)
            @PathVariable @NotBlank String id,
            @Parameter(description = "Channel ID for channel-specific validation")
            @RequestParam(required = false) String channelId) {
        log.info("Validating product id: {} for channel: {}", id, channelId);
        return ResponseEntity.ok(productService.validateProduct(id, channelId));
    }
    
    @Operation(
        summary = "Get product variants",
        description = "Retrieves all variants of a specific product"
    )
    @GetMapping("/{id}/variants")
    public ResponseEntity<List<ProductResponse>> getProductVariants(
            @Parameter(description = "Product ID", required = true)
            @PathVariable @NotBlank String id) {
        log.info("Fetching variants for product id: {}", id);
        List<Product> variants = productService.getProductVariants(id);
        return ResponseEntity.ok(variants.stream()
            .map(ProductResponse::fromProduct)
            .toList());
    }
    
    @Operation(
        summary = "Get product by SKU",
        description = "Retrieves product information using its SKU"
    )
    @GetMapping("/sku/{sku}")
    public ResponseEntity<ProductResponse> getProductBySku(
            @Parameter(description = "Product SKU", required = true)
            @PathVariable @NotBlank String sku) {
        log.info("Fetching product with SKU: {}", sku);
        Product product = productService.getProductBySku(sku);
        return ResponseEntity.ok(ProductResponse.fromProduct(product));
    }
}
