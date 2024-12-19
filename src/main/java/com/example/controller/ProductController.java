package com.example.controller;

import com.example.dto.ProductDTO;
import com.example.entity.Category;
import com.example.entity.CategoryFeatureTemplate;
import com.example.entity.Product;
import com.example.mapper.ProductMapper;
import com.example.service.CategoryService;
import com.example.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;
    private final CategoryService categoryService;
    private final ProductMapper productMapper;

    @GetMapping
    public ResponseEntity<List<ProductDTO>> getAllProducts() {
        List<Product> products = productService.findAll();
        return ResponseEntity.ok(products.stream()
            .map(productMapper::toDTO)
            .collect(Collectors.toList()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductDTO> getProduct(@PathVariable Long id) {
        Product product = productService.findById(id);
        return ResponseEntity.ok(productMapper.toDTO(product));
    }

    @PostMapping
    public ResponseEntity<ProductDTO> createProduct(@RequestBody ProductDTO productDTO) {
        Product product = productMapper.toEntity(productDTO);
        product = productService.save(product);
        return ResponseEntity.ok(productMapper.toDTO(product));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductDTO> updateProduct(
        @PathVariable Long id,
        @RequestBody ProductDTO productDTO
    ) {
        Product product = productMapper.toEntity(productDTO);
        product = productService.update(id, product);
        return ResponseEntity.ok(productMapper.toDTO(product));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        productService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/category/{categoryId}/templates")
    public ResponseEntity<Map<String, CategoryFeatureTemplate>> getCategoryTemplates(
        @PathVariable Long categoryId
    ) {
        Category category = categoryService.findById(categoryId);
        return ResponseEntity.ok(category.getTemplates().stream()
            .collect(Collectors.toMap(
                CategoryFeatureTemplate::getCode,
                template -> template
            )));
    }
}
