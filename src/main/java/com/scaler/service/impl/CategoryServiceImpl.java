package com.scaler.service.impl;

import com.scaler.entity.Category;
import com.scaler.entity.CategoryMapping;
import com.scaler.repository.CategoryMappingRepository;
import com.scaler.repository.CategoryRepository;
import com.scaler.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;
    private final CategoryMappingRepository categoryMappingRepository;

    @Override
    public List<Category> findAll() {
        return categoryRepository.findAll();
    }

    @Override
    public Category findById(UUID id) {
        return categoryRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Category not found with id: " + id));
    }

    @Override
    public Category findByCode(String code) {
        return categoryRepository.findByCode(code)
            .orElseThrow(() -> new RuntimeException("Category not found with code: " + code));
    }

    @Override
    public Category save(Category category) {
        validateCategory(category);
        return categoryRepository.save(category);
    }

    @Override
    public Category update(UUID id, Category category) {
        Category existingCategory = findById(id);
        existingCategory.setCode(category.getCode());
        existingCategory.setName(category.getName());
        existingCategory.setDescription(category.getDescription());
        validateCategory(existingCategory);
        return categoryRepository.save(existingCategory);
    }

    @Override
    public void delete(UUID id) {
        Category category = findById(id);
        List<CategoryMapping> mappings = categoryMappingRepository.findByParent(category);
        if (!mappings.isEmpty()) {
            throw new IllegalStateException("Cannot delete category with children");
        }
        categoryMappingRepository.deleteAll(mappings);
        categoryRepository.deleteById(id);
    }

    @Override
    public List<CategoryMapping> findMappingsByParent(Category parent) {
        return categoryMappingRepository.findByParent(parent);
    }

    @Override
    public List<CategoryMapping> findMappingsByChild(Category child) {
        return categoryMappingRepository.findByChild(child);
    }

    @Override
    public CategoryMapping createMapping(Category parent, Category child) {
        CategoryMapping mapping = new CategoryMapping(parent, child);
        return categoryMappingRepository.save(mapping);
    }

    @Override
    public void deleteMapping(CategoryMapping mapping) {
        categoryMappingRepository.delete(mapping);
    }

    private void validateCategory(Category category) {
        if (category.getCode() == null || category.getCode().trim().isEmpty()) {
            throw new IllegalArgumentException("Category code is required");
        }
        if (category.getName() == null || category.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("Category name is required");
        }
    }
}
