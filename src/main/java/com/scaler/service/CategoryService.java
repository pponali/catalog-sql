package com.scaler.service;

import com.scaler.entity.Category;
import com.scaler.entity.CategoryMapping;
import java.util.List;
import java.util.UUID;

public interface CategoryService {
    List<Category> findAll();
    Category findById(UUID id);
    Category findByCode(String code);
    Category save(Category category);
    Category update(UUID id, Category category);
    void delete(UUID id);
    List<CategoryMapping> findMappingsByParent(Category parent);
    List<CategoryMapping> findMappingsByChild(Category child);
    CategoryMapping createMapping(Category parent, Category child);
    void deleteMapping(CategoryMapping mapping);
}
