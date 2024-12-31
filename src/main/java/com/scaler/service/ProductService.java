package com.scaler.service;

import com.scaler.dto.ProductDTO;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ProductService {
    ProductDTO create(ProductDTO product);
    ProductDTO update(UUID id, ProductDTO product);
    Optional<ProductDTO> findById(UUID id);
    List<ProductDTO> findAll();
    void delete(UUID id);
    boolean existsById(UUID id);
    void deleteById(UUID id);
}
