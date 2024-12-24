package com.scaler.service;

import com.scaler.dto.ProductDTO;
import java.util.List;
import java.util.Optional;

public interface ProductService {
    ProductDTO create(ProductDTO product);
    ProductDTO update(Long id, ProductDTO product);
    Optional<ProductDTO> findById(Long id);
    List<ProductDTO> findAll();
    void delete(Long id);
    boolean existsById(Long id);
    void deleteById(Long id);
}
