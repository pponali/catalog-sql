package com.scaler.service.impl;

import com.scaler.entity.Category;
import com.scaler.entity.CategoryFeatureTemplate;
import com.scaler.repository.CategoryFeatureTemplateRepository;
import com.scaler.service.CategoryFeatureTemplateService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class CategoryFeatureTemplateServiceImpl implements CategoryFeatureTemplateService {
    private final CategoryFeatureTemplateRepository templateRepository;

    @Override
    public CategoryFeatureTemplate findById(Long id) {
        return templateRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Template not found with id: " + id));
    }

    @Override
    public List<CategoryFeatureTemplate> findAll() {
        return templateRepository.findAll();
    }

    @Override
    public CategoryFeatureTemplate save(CategoryFeatureTemplate template) {
        validateTemplate(template);
        return templateRepository.save(template);
    }

    @Override
    public void delete(Long id) {
        templateRepository.deleteById(id);
    }

    @Override
    public CategoryFeatureTemplate update(Long id, CategoryFeatureTemplate template) {
        CategoryFeatureTemplate existingTemplate = findById(id);
        
        existingTemplate.setCode(template.getCode());
        existingTemplate.setName(template.getName());
        existingTemplate.setDescription(template.getDescription());
        existingTemplate.setAttributeType(template.getAttributeType());
        existingTemplate.setValidationPattern(template.getValidationPattern());
        existingTemplate.setMinValue(template.getMinValue());
        existingTemplate.setMaxValue(template.getMaxValue());
        existingTemplate.setAllowedValues(template.getAllowedValues());
        existingTemplate.setUnit(template.getUnit());
        existingTemplate.setVisible(template.isVisible());
        existingTemplate.setEditable(template.isEditable());
        existingTemplate.setSearchable(template.isSearchable());
        existingTemplate.setComparable(template.isComparable());
        existingTemplate.setMandatory(template.isMandatory());
        existingTemplate.setMultiValued(template.isMultiValued());
        existingTemplate.setMetadata(template.getMetadata());
        
        return templateRepository.save(existingTemplate);
    }

    @Override
    public List<CategoryFeatureTemplate> findByCategory(Category category) {
        return templateRepository.findByCategory(category);
    }

    private void validateTemplate(CategoryFeatureTemplate template) {
        if (template.getCode() == null || template.getCode().trim().isEmpty()) {
            throw new IllegalArgumentException("Template code is required");
        }
        if (template.getName() == null || template.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("Template name is required");
        }
        if (template.getAttributeType() == null || template.getAttributeType().trim().isEmpty()) {
            throw new IllegalArgumentException("Template attribute type is required");
        }
    }
}
