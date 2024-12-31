package com.scaler.service;

import com.scaler.entity.Category;
import java.util.List;
import java.util.UUID;

public interface CategoryService {
    List<Category> findAll();
    Category findById(UUID id);
    Category findByCode(String code);
    Category save(Category category);
    Category update(UUID id, Category category);
    void delete(UUID id);
}
