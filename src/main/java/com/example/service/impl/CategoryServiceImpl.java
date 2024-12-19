package com.example.service.impl;

import com.example.entity.Category;
import com.example.repository.CategoryRepository;
import com.example.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;

    @Override
    public List<Category> findAll() {
        return categoryRepository.findAll();
    }

    @Override
    public Category findById(Long id) {
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
    public Category update(Long id, Category category) {
        Category existingCategory = findById(id);
        existingCategory.setCode(category.getCode());
        existingCategory.setName(category.getName());
        existingCategory.setDescription(category.getDescription());
        if (category.getParent() != null) {
            existingCategory.setParent(findById(category.getParent().getId()));
        }
        validateCategory(existingCategory);
        return categoryRepository.save(existingCategory);
    }

    @Override
    public void delete(Long id) {
        Category category = findById(id);
        if (!category.getChildren().isEmpty()) {
            throw new IllegalStateException("Cannot delete category with children");
        }
        categoryRepository.deleteById(id);
    }

    private void validateCategory(Category category) {
        if (category.getCode() == null || category.getCode().trim().isEmpty()) {
            throw new IllegalArgumentException("Category code is required");
        }
        if (category.getName() == null || category.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("Category name is required");
        }
        if (category.getParent() != null && category.getParent().getId().equals(category.getId())) {
            throw new IllegalArgumentException("Category cannot be its own parent");
        }
    }
}
