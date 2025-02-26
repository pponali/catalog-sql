package com.scaler.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.scaler.dto.ProductFeatureDTO;
import com.scaler.dto.ProductFeatureValueDTO;
import com.scaler.entity.*;
import com.scaler.dto.ValidationResultDTO;
import com.scaler.exception.ValidationException;
import com.scaler.service.ProductFeatureService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/product-features")
@RequiredArgsConstructor
public class ProductFeatureController {

    private final ProductFeatureService productFeatureService;
    private final ObjectMapper objectMapper;

    @GetMapping("/{id}")
    public ResponseEntity<ProductFeatureDTO> getById(@PathVariable UUID id) {
        log.debug("REST request to get ProductFeature by id: {}", id);
        ProductFeature feature = productFeatureService.findById(id);
        return ResponseEntity.ok(mapToDTO(feature));
    }

    @GetMapping("/product/{productId}")
    public ResponseEntity<List<ProductFeatureDTO>> getByProductId(@PathVariable UUID productId) {
        log.debug("REST request to get ProductFeatures by product id: {}", productId);
        List<ProductFeature> features = productFeatureService.findByProductId(productId);
        return ResponseEntity.ok(features.stream().map(this::mapToDTO).toList());
    }

    @GetMapping("/product/{productId}/template/{templateId}")
    public ResponseEntity<ProductFeatureDTO> getByProductAndTemplate(
            @PathVariable UUID productId,
            @PathVariable UUID templateId) {
        log.debug("REST request to get ProductFeature by product id: {} and template id: {}", productId, templateId);
        ProductFeature feature = productFeatureService.findByProductIdAndTemplateId(productId, templateId);
        return ResponseEntity.ok(mapToDTO(feature));
    }

    @PostMapping
    public ResponseEntity<ProductFeatureDTO> create(@Valid @RequestBody ProductFeatureDTO dto) {
        log.debug("REST request to save ProductFeature: {}", dto);
        ProductFeature feature = mapToEntity(dto);
        feature = productFeatureService.save(feature);
        return ResponseEntity.ok(mapToDTO(feature));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductFeatureDTO> update(
            @PathVariable UUID id,
            @Valid @RequestBody ProductFeatureDTO dto) {
        log.debug("REST request to update ProductFeature: {}", dto);
        if (!id.equals(dto.getId())) {
            throw new ValidationException("IDs don't match");
        }
        ProductFeature feature = mapToEntity(dto);
        feature = productFeatureService.save(feature);
        return ResponseEntity.ok(mapToDTO(feature));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        log.debug("REST request to delete ProductFeature: {}", id);
        productFeatureService.deleteById(id);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/product/{productId}")
    public ResponseEntity<Void> deleteByProductId(@PathVariable UUID productId) {
        log.debug("REST request to delete ProductFeatures by product id: {}", productId);
        productFeatureService.deleteByProductId(productId);
        return ResponseEntity.ok().build();
    }

    private ProductFeatureDTO mapToDTO(ProductFeature feature) {
        return ProductFeatureDTO.builder()
                .id(feature.getId())
                .productId(feature.getFeatureMapping().getProduct().getId())
                .templateId(feature.getTemplate().getId())
                .code(feature.getCode())
                .name(feature.getName())
                .description(feature.getDescription())
                .attributeType(feature.getAttributeType())
                .validationPattern(feature.getValidationPattern())
                .minValue(feature.getMinValue())
                .maxValue(feature.getMaxValue())
                .allowedValues(feature.getAllowedValues())
                .defaultValue(feature.getDefaultValue())
                .featureType(feature.getFeatureType())
                .unitOfMeasureId(feature.getUnitOfMeasure() != null ? 
                    feature.getUnitOfMeasure().getId() : null)
                .visible(feature.isVisible())
                .editable(feature.isEditable())
                .searchable(feature.isSearchable())
                .comparable(feature.isComparable())
                .required(feature.isRequired())
                .multiValued(feature.isMultiValued())
                .metadata(feature.getMetadata() != null ? feature.getMetadata().toString() : null)
                .createdDate(feature.getCreatedDate().toString())
                .lastModifiedDate(feature.getLastModifiedDate().toString())
                .createdBy(feature.getCreatedBy())
                .lastModifiedBy(feature.getLastModifiedBy())
                .featureValues(feature.getFeatureValues().stream()
                    .map(this::mapFeatureValueToDTO)
                    .collect(Collectors.toSet()))
                .build();
    }

    private ProductFeature mapToEntity(ProductFeatureDTO dto) {
        ProductFeature feature = new ProductFeature();
        feature.setId(dto.getId());
        
        // Set template reference - this should be validated to exist
        CategoryFeatureTemplate template = new CategoryFeatureTemplate();
        template.setId(dto.getTemplateId());
        feature.setTemplate(template);

        // Create product mapping if product ID is provided
        if (dto.getProductId() != null) {
            Product product = new Product();
            product.setId(dto.getProductId());
            ProductFeatureMapping mapping = ProductFeatureMapping.builder()
                .product(product)
                .feature(feature)
                .displayOrder(1)
                .visible(true)
                .enabled(true)
                .build();
            feature.addProductMapping(mapping);
        }

        // Set feature properties
        feature.setCode(dto.getCode());
        feature.setName(dto.getName());
        feature.setDescription(dto.getDescription());
        feature.setAttributeType(dto.getAttributeType());
        feature.setValidationPattern(dto.getValidationPattern());
        feature.setMinValue(dto.getMinValue());
        feature.setMaxValue(dto.getMaxValue());
        feature.setAllowedValues(dto.getAllowedValues());
        feature.setDefaultValue(dto.getDefaultValue());
        feature.setFeatureType(dto.getFeatureType());
        feature.setVisible(dto.isVisible());
        feature.setEditable(dto.isEditable());
        feature.setSearchable(dto.isSearchable());
        feature.setComparable(dto.isComparable());
        feature.setRequired(dto.isRequired());
        feature.setMultiValued(dto.isMultiValued());
        try {
            feature.setMetadata(dto.getMetadata() != null ? objectMapper.readTree(dto.getMetadata()) : null);
        } catch (Exception e) {
            log.error("Error parsing metadata JSON", e);
            throw new RuntimeException("Invalid metadata JSON format", e);
        }

        if (dto.getUnitOfMeasureId() != null) {
            UnitOfMeasure unitOfMeasure = new UnitOfMeasure();
            unitOfMeasure.setId(dto.getUnitOfMeasureId());
            feature.setUnitOfMeasure(unitOfMeasure);
        }

        // Map feature values if present
        if (dto.getFeatureValues() != null) {
            Set<ProductFeatureValue> values = dto.getFeatureValues().stream()
                .map(this::mapFeatureValueToEntity)
                .collect(Collectors.toSet());
            ProductFeatureMapping mapping = feature.getFeatureMapping();
            if (mapping != null) {
                values.forEach(value -> {
                    value.setFeatureMapping(mapping);
                    mapping.getFeatureValues().add(value);
                });
            }
        }

        return feature;
    }

    private ProductFeatureValueDTO mapFeatureValueToDTO(ProductFeatureValue value) {
        return ProductFeatureValueDTO.builder()
                .id(value.getId())
                .featureId(value.getFeatureMapping().getFeature().getId())
                .attributeValues(value.getValueAsString())
                .type(value.getType())
                .unit(value.getUnit())
                .unitOfMeasure(value.getUnitOfMeasure())
                .validationResult(value.getValidationResult() != null ? 
                    ValidationResultDTO.builder()
                        .id(value.getValidationResult().getId())
                        .entityId(value.getValidationResult().getEntityId())
                        .entityType(value.getValidationResult().getEntityType())
                        .fieldName(value.getValidationResult().getFieldName())
                        .status(value.getValidationResult().getStatus())
                        .errors(value.getValidationResult().getErrors())
                        .build() : null)
                .metadata(value.getMetadata() != null ? value.getMetadata().toString() : null)
                .createdDate(value.getCreatedDate().toString())
                .lastModifiedDate(value.getLastModifiedDate().toString())
                .createdBy(value.getCreatedBy())
                .lastModifiedBy(value.getLastModifiedBy())
                .build();
    }

    private ProductFeatureValue mapFeatureValueToEntity(ProductFeatureValueDTO dto) {
        ProductFeatureValue value = new ProductFeatureValue();
        value.setId(dto.getId());
        value.setType(dto.getType());
        value.setUnit(dto.getUnit());
        value.setUnitOfMeasure(dto.getUnitOfMeasure());
        if (dto.getValidationResult() != null) {
            ValidationResult validationResult = new ValidationResult();
            validationResult.setId(dto.getValidationResult().getId());
            validationResult.setEntityId(dto.getValidationResult().getEntityId());
            validationResult.setEntityType(dto.getValidationResult().getEntityType());
            validationResult.setFieldName(dto.getValidationResult().getFieldName());
            validationResult.setStatus(dto.getValidationResult().getStatus());
            validationResult.setErrors(dto.getValidationResult().getErrors());
            value.setValidationResult(validationResult);
        }
        value.setAttributeValue(dto.getAttributeValues() != null ? 
            new ObjectMapper().valueToTree(dto.getAttributeValues()) : null);
        value.setMetadata(dto.getMetadata() != null ? 
            new ObjectMapper().valueToTree(dto.getMetadata()) : null);
        return value;
    }
}
