package com.scaler.service;

import com.scaler.entity.Category;
import java.util.List;

public interface CategoryService {
    List<Category> findAll();
    Category findById(Long id);
    Category findByCode(String code);
    Category save(Category category);
    Category update(Long id, Category category);
    void delete(Long id);
}
