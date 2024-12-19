package com.example.service;

import com.example.entity.Category;
import com.example.entity.CategoryFeatureTemplate;
import com.example.entity.ClassAttributeAssignment;
import com.example.entity.ClassificationClass;

import java.util.List;
import java.util.Set;

public interface CategoryFeatureTemplateService {
    
    /**
     * Create a new feature template for a category
     */
    CategoryFeatureTemplate createTemplate(Long categoryId, CategoryFeatureTemplate template);
    
    /**
     * Update an existing feature template
     */
    CategoryFeatureTemplate updateTemplate(Long templateId, CategoryFeatureTemplate template);
    
    /**
     * Delete a feature template
     */
    void deleteTemplate(Long templateId);
    
    /**
     * Get all feature templates for a category (including inherited ones)
     */
    Set<CategoryFeatureTemplate> getAllTemplates(Long categoryId);
    
    /**
     * Get direct feature templates for a category (excluding inherited ones)
     */
    Set<CategoryFeatureTemplate> getDirectTemplates(Long categoryId);
    
    /**
     * Apply feature templates to a classification class
     */
    List<ClassAttributeAssignment> applyTemplatesToClass(Long classificationClassId);
    
    /**
     * Sync feature templates with existing assignments
     */
    void syncTemplatesWithAssignments(Long classificationClassId);
    
    /**
     * Check if a category inherits a specific feature template
     */
    boolean hasFeatureTemplate(Long categoryId, String templateCode);
    
    /**
     * Get categories that share a specific feature template
     */
    List<Category> getCategoriesWithTemplate(String templateCode);
    
    /**
     * Copy feature templates from one category to another
     */
    void copyTemplates(Long sourceCategoryId, Long targetCategoryId);
    
    /**
     * Move feature templates from one category to another
     */
    void moveTemplates(Long sourceCategoryId, Long targetCategoryId);
}
