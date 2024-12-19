package com.example.service;

import com.example.entity.Category;
import com.example.entity.CategoryFeatureTemplate;
import java.util.List;

public interface CategoryFeatureTemplateService {
    CategoryFeatureTemplate findById(Long id);
    List<CategoryFeatureTemplate> findAll();
    CategoryFeatureTemplate save(CategoryFeatureTemplate template);
    void delete(Long id);
    CategoryFeatureTemplate update(Long id, CategoryFeatureTemplate template);
    List<CategoryFeatureTemplate> findByCategory(Category category);
}
