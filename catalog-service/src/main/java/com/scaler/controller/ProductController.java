package com.scaler.controller;

import com.scaler.dto.ProductDTO;
import com.scaler.event.ProductEventPublisher;
import com.scaler.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;
    private final ProductEventPublisher eventPublisher;

    @GetMapping
    public ResponseEntity<List<ProductDTO>> getAllProducts() {
        List<ProductDTO> products = productService.findAll();
        return ResponseEntity.ok(products);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductDTO> getProductById(@PathVariable UUID id) {
        if (!productService.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        Optional<ProductDTO> product = productService.findById(id);
        if (product.isEmpty()) {
            return ResponseEntity.notFound().build();
        } else{
            return ResponseEntity.ok(product.get());
        }
    }

    @PostMapping
    @Transactional
    public ResponseEntity<ProductDTO> createProduct(@RequestBody ProductDTO product) {
        ProductDTO created = productService.create(product);
        // Publish event after successful creation
        eventPublisher.publishProductCreated(created);
        return ResponseEntity.ok(created);
    }

    @PutMapping("/{id}")
    @Transactional
    public ResponseEntity<ProductDTO> updateProduct(@PathVariable UUID id, @RequestBody ProductDTO product) {
        if (!productService.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        ProductDTO updated = productService.update(id, product);
        // Publish event after successful update
        eventPublisher.publishProductUpdated(updated);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity<Void> deleteProduct(@PathVariable UUID id) {
        if (!productService.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        productService.deleteById(id);
        // Publish event after successful deletion
        eventPublisher.publishProductDeleted(id);
        return ResponseEntity.ok().build();
    }
}