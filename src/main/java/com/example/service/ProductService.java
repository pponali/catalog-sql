package com.example.service;

import com.example.entity.Product;
import java.util.List;

public interface ProductService {
    List<Product> findAll();
    Product findById(Long id);
    Product save(Product product);
    void delete(Long id);
    Product update(Long id, Product product);
}
