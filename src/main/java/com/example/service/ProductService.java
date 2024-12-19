package com.example.service;

import com.example.entity.Product;
import com.example.entity.Category;

import java.util.List;

public interface ProductService {
    Product saveProduct(Product product);
    Product getProductById(Long id);
    List<Product> getAllProducts();
    void deleteProduct(Long id);
    List<Product> getProductsByCategory(Category category);
}
