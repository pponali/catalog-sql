package com.scaler.service.impl;

import com.scaler.dto.ProductDTO;
import com.scaler.entity.Product;
import com.scaler.repository.ProductRepository;
import com.scaler.service.ProductService;
import com.scaler.service.ProductMappingService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final ProductMappingService productMappingService;

    @Override
    @Transactional
    public ProductDTO create(ProductDTO productDTO) {
        Product product = productMappingService.toEntity(productDTO);
        // Assuming ID generation happens elsewhere or is handled by DB if not UUID
        // product.setId(UUID.randomUUID()); 
        Product savedProduct = productRepository.save(product);
        return productMappingService.toDTO(savedProduct);
    }

    @Override
    @Transactional
    public ProductDTO update(UUID id, ProductDTO productDTO) {
        Product existingProduct = productRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Product not found with id: " + id));
        
        // Map DTO to entity, preserving the existing ID
        Product productToUpdate = productMappingService.toEntity(productDTO);
        productToUpdate.setId(id); // Ensure the ID is set for update
        
        // Merge changes if necessary, or simply save
        // For simplicity, assuming save handles merge/update correctly
        Product savedProduct = productRepository.save(productToUpdate); 
        
        return productMappingService.toDTO(savedProduct);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ProductDTO> findById(UUID id) {
        return productRepository.findById(id)
            .map(productMappingService::toDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductDTO> findAll() {
        return productRepository.findAll().stream()
            .map(productMappingService::toDTO)
            .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void delete(UUID id) {
        if (!productRepository.existsById(id)) {
            throw new RuntimeException("Product not found with id: " + id);
        }
        productRepository.deleteById(id);
    }

    @Override
    @Transactional
    public void deleteById(UUID id) {
        if (!productRepository.existsById(id)) {
            throw new RuntimeException("Product not found with id: " + id);
        }
        productRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsById(UUID id) {
        return productRepository.existsById(id);
    }
}
