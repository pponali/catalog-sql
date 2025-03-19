package com.scaler.service;

import com.scaler.entity.Category;
import com.scaler.entity.CategoryFeatureTemplate;
import java.util.List;

public interface CategoryFeatureTemplateService {
    CategoryFeatureTemplate findById(Long id);
    List<CategoryFeatureTemplate> findAll();
    CategoryFeatureTemplate save(CategoryFeatureTemplate template);
    void delete(Long id);
    CategoryFeatureTemplate update(Long id, CategoryFeatureTemplate template);
    List<CategoryFeatureTemplate> findByCategory(Category category);
}
