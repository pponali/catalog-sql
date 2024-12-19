package com.example.service;

import com.example.entity.Category;
import com.example.entity.ClassificationClass;
import java.util.List;

public interface CategoryService {
    Category save(Category category);
    Category findById(Long id);
    List<Category> findAll();
    void deleteById(Long id);
    Category findByCode(String code);
    Category getCategoryById(Long id);
    ClassificationClass getClassificationClassById(Long id);
    Category saveCategory(Category category);
    void deleteCategory(Long id);
}
