package com.example.service.impl;

import com.example.entity.*;
import com.example.exception.ResourceNotFoundException;
import com.example.repository.*;
import com.example.service.CategoryFeatureTemplateService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoryFeatureTemplateServiceImpl implements CategoryFeatureTemplateService {

    private final CategoryRepository categoryRepository;
    private final CategoryFeatureTemplateRepository templateRepository;
    private final ClassificationClassRepository classRepository;
    private final ClassAttributeAssignmentRepository assignmentRepository;
    private final ClassificationAttributeRepository attributeRepository;

    @Override
    @Transactional
    public CategoryFeatureTemplate createTemplate(Long categoryId, CategoryFeatureTemplate template) {
        Category category = categoryRepository.findById(categoryId)
            .orElseThrow(() -> new EntityNotFoundException("Category not found: " + categoryId));
        
        template.setCategory(category);
        return save(template);
    }

    @Override
    @Transactional
    public CategoryFeatureTemplate updateTemplate(Long templateId, CategoryFeatureTemplate template) {
        CategoryFeatureTemplate existing = findById(templateId);
        updateTemplateFields(existing, template);
        return save(existing);
    }

    @Override
    @Transactional
    public void deleteTemplate(Long templateId) {
        delete(templateId);
    }

    @Override
    @Transactional(readOnly = true)
    public Set<CategoryFeatureTemplate> getAllTemplates(Long categoryId) {
        Category category = categoryRepository.findById(categoryId)
            .orElseThrow(() -> new EntityNotFoundException("Category not found: " + categoryId));
        
        return category.getAllFeatureTemplates();
    }

    @Override
    @Transactional(readOnly = true)
    public Set<CategoryFeatureTemplate> getDirectTemplates(Long categoryId) {
        Category category = categoryRepository.findById(categoryId)
            .orElseThrow(() -> new EntityNotFoundException("Category not found: " + categoryId));
        
        return category.getFeatureTemplates();
    }

    @Override
    @Transactional
    public List<ClassAttributeAssignment> applyTemplatesToClass(Long classificationClassId) {
        ClassificationClass classificationClass = classRepository.findById(classificationClassId)
            .orElseThrow(() -> new EntityNotFoundException("Classification class not found: " + classificationClassId));
        
        Category category = classificationClass.getCategory();
        if (category == null) {
            return Collections.emptyList();
        }

        Set<CategoryFeatureTemplate> templates = category.getAllFeatureTemplates();
        List<ClassAttributeAssignment> newAssignments = new ArrayList<>();

        for (CategoryFeatureTemplate template : templates) {
            // Create or find classification attribute
            ClassificationAttribute attribute = attributeRepository.findByCode(template.getCode())
                .orElseGet(() -> attributeRepository.save(ClassificationAttribute.builder()
                    .code(template.getCode())
                    .name(template.getName())
                    .description(template.getDescription())
                    .build()));

            // Check if assignment already exists
            Optional<ClassAttributeAssignment> existingAssignment = assignmentRepository
                .findByClassificationClassAndClassificationAttribute(classificationClass, attribute);

            if (existingAssignment.isEmpty()) {
                // Create new assignment from template
                ClassAttributeAssignment assignment = template.createAssignment(classificationClass, attribute);
                newAssignments.add(assignmentRepository.save(assignment));
            }
        }

        return newAssignments;
    }

    @Override
    @Transactional
    public void syncTemplatesWithAssignments(Long classificationClassId) {
        ClassificationClass classificationClass = classRepository.findById(classificationClassId)
            .orElseThrow(() -> new EntityNotFoundException("Classification class not found: " + classificationClassId));
        
        Category category = classificationClass.getCategory();
        if (category == null) {
            return;
        }

        Set<CategoryFeatureTemplate> templates = category.getAllFeatureTemplates();
        Set<String> templateCodes = templates.stream()
            .map(CategoryFeatureTemplate::getCode)
            .collect(Collectors.toSet());

        // Remove assignments that don't have corresponding templates
        classificationClass.getAttributeAssignments().removeIf(assignment -> 
            !templateCodes.contains(assignment.getClassificationAttribute().getCode()));

        // Update existing assignments and create new ones
        applyTemplatesToClass(classificationClassId);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean hasFeatureTemplate(Long categoryId, String templateCode) {
        Category category = categoryRepository.findById(categoryId)
            .orElseThrow(() -> new EntityNotFoundException("Category not found: " + categoryId));
        
        return category.getAllFeatureTemplates().stream()
            .anyMatch(template -> template.getCode().equals(templateCode));
    }

    @Override
    @Transactional(readOnly = true)
    public List<Category> getCategoriesWithTemplate(String templateCode) {
        return categoryRepository.findAll().stream()
            .filter(category -> hasFeatureTemplate(category.getId(), templateCode))
            .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void copyTemplates(Long sourceCategoryId, Long targetCategoryId) {
        Category sourceCategory = categoryRepository.findById(sourceCategoryId)
            .orElseThrow(() -> new EntityNotFoundException("Source category not found: " + sourceCategoryId));
        Category targetCategory = categoryRepository.findById(targetCategoryId)
            .orElseThrow(() -> new EntityNotFoundException("Target category not found: " + targetCategoryId));

        Set<CategoryFeatureTemplate> templates = sourceCategory.getFeatureTemplates();
        for (CategoryFeatureTemplate template : templates) {
            CategoryFeatureTemplate newTemplate = CategoryFeatureTemplate.builder()
                .category(targetCategory)
                .code(template.getCode())
                .name(template.getName())
                .description(template.getDescription())
                .attributeType(template.getAttributeType())
                .mandatory(template.isMandatory())
                .multiValued(template.isMultiValued())
                .sequence(template.getSequence())
                .visible(template.isVisible())
                .editable(template.isEditable())
                .searchable(template.isSearchable())
                .comparable(template.isComparable())
                .unit(template.getUnit())
                .rangeMin(template.getRangeMin())
                .rangeMax(template.getRangeMax())
                .regexValidation(template.getRegexValidation())
                .defaultValue(template.getDefaultValue())
                .allowedValues(new HashSet<>(template.getAllowedValues()))
                .metadata(new HashMap<>(template.getMetadata()))
                .build();
            
            save(newTemplate);
        }
    }

    @Override
    @Transactional
    public void moveTemplates(Long sourceCategoryId, Long targetCategoryId) {
        Category sourceCategory = categoryRepository.findById(sourceCategoryId)
            .orElseThrow(() -> new EntityNotFoundException("Source category not found: " + sourceCategoryId));
        Category targetCategory = categoryRepository.findById(targetCategoryId)
            .orElseThrow(() -> new EntityNotFoundException("Target category not found: " + targetCategoryId));

        Set<CategoryFeatureTemplate> templates = sourceCategory.getFeatureTemplates();
        for (CategoryFeatureTemplate template : templates) {
            template.setCategory(targetCategory);
            save(template);
        }
    }

    @Override
    public CategoryFeatureTemplate findById(Long id) {
        return templateRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Template not found: " + id));
    }

    @Override
    public List<CategoryFeatureTemplate> findByCategory(Category category) {
        return templateRepository.findByCategory(category);
    }

    @Override
    public CategoryFeatureTemplate save(CategoryFeatureTemplate template) {
        validateTemplate(template);
        return templateRepository.save(template);
    }

    @Override
    public void delete(Long id) {
        CategoryFeatureTemplate template = findById(id);
        templateRepository.delete(template);
    }

    @Override
    public CategoryFeatureTemplate update(Long id, CategoryFeatureTemplate updatedTemplate) {
        CategoryFeatureTemplate existingTemplate = findById(id);
        updateTemplateFields(existingTemplate, updatedTemplate);
        validateTemplate(existingTemplate);
        return templateRepository.save(existingTemplate);
    }

    private void validateTemplate(CategoryFeatureTemplate template) {
        if (template.getCode() == null || template.getCode().trim().isEmpty()) {
            throw new IllegalArgumentException("Template code is required");
        }

        if (template.getName() == null || template.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("Template name is required");
        }

        if (template.getAttributeType() == null || template.getAttributeType().trim().isEmpty()) {
            throw new IllegalArgumentException("Attribute type is required");
        }

        validateAttributeTypeSpecificFields(template);
    }

    private void validateAttributeTypeSpecificFields(CategoryFeatureTemplate template) {
        switch (template.getAttributeType().toLowerCase()) {
            case "numeric":
                validateNumericType(template);
                break;
            case "string":
                validateStringType(template);
                break;
            case "enum":
                validateEnumType(template);
                break;
            case "boolean":
                // No additional validation needed for boolean type
                break;
            default:
                throw new IllegalArgumentException("Invalid attribute type: " + template.getAttributeType());
        }
    }

    private void validateNumericType(CategoryFeatureTemplate template) {
        if (template.getMinValue() != null && template.getMaxValue() != null) {
            if (template.getMinValue() > template.getMaxValue()) {
                throw new IllegalArgumentException("Min value cannot be greater than max value");
            }
        }
    }

    private void validateStringType(CategoryFeatureTemplate template) {
        if (template.getValidationPattern() != null && !template.getValidationPattern().trim().isEmpty()) {
            try {
                java.util.regex.Pattern.compile(template.getValidationPattern());
            } catch (java.util.regex.PatternSyntaxException e) {
                throw new IllegalArgumentException("Invalid validation pattern: " + e.getMessage());
            }
        }
    }

    private void validateEnumType(CategoryFeatureTemplate template) {
        if (template.getAllowedValues() == null || template.getAllowedValues().trim().isEmpty()) {
            throw new IllegalArgumentException("Allowed values are required for enum type");
        }
    }

    private void updateTemplateFields(CategoryFeatureTemplate existing, CategoryFeatureTemplate updated) {
        existing.setName(updated.getName());
        existing.setDescription(updated.getDescription());
        existing.setAttributeType(updated.getAttributeType());
        existing.setValidationPattern(updated.getValidationPattern());
        existing.setMinValue(updated.getMinValue());
        existing.setMaxValue(updated.getMaxValue());
        existing.setAllowedValues(updated.getAllowedValues());
        existing.setUnit(updated.getUnit());
        existing.setVisible(updated.isVisible());
        existing.setEditable(updated.isEditable());
        existing.setSearchable(updated.isSearchable());
        existing.setComparable(updated.isComparable());
        existing.setMandatory(updated.isMandatory());
        existing.setMultiValued(updated.isMultiValued());
        existing.setMetadata(updated.getMetadata());
    }
}
