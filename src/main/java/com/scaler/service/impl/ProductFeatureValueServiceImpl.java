package com.scaler.service.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.scaler.dto.ProductFeatureValueDTO;
import com.scaler.entity.ProductFeatureValue;
import com.scaler.exception.ResourceNotFoundException;
import com.scaler.mapper.ProductFeatureValueMapper;
import com.scaler.repository.ProductFeatureValueRepository;
import com.scaler.service.ProductFeatureValueService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductFeatureValueServiceImpl implements ProductFeatureValueService {

    private final ProductFeatureValueRepository repository;
    private final ProductFeatureValueMapper mapper;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    @Transactional
    public ProductFeatureValueDTO save(ProductFeatureValueDTO dto) {
        ProductFeatureValue entity = mapper.toEntity(dto);
        entity = repository.save(entity);
        return mapper.toDTO(entity);
    }

    @Override
    @Transactional
    public ProductFeatureValueDTO update(ProductFeatureValueDTO dto) {
        if (!repository.existsById(dto.getId())) {
            throw new ResourceNotFoundException("ProductFeatureValue not found with id: " + dto.getId());
        }
        return save(dto);
    }

    @Override
    @Transactional
    public List<ProductFeatureValueDTO> saveAll(List<ProductFeatureValueDTO> dtos) {
        List<ProductFeatureValue> entities = dtos.stream()
                .map(mapper::toEntity)
                .collect(Collectors.toList());
        entities = repository.saveAll(entities);
        return entities.stream()
                .map(mapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void delete(Long id) {
        repository.deleteById(id);
    }

    @Override
    @Transactional
    public void delete(ProductFeatureValueDTO dto) {
        repository.delete(mapper.toEntity(dto));
    }

    @Override
    public Optional<ProductFeatureValueDTO> findById(Long id) {
        return repository.findById(id).map(mapper::toDTO);
    }

    @Override
    public List<ProductFeatureValueDTO> findAllById(List<Long> ids) {
        return repository.findAllById(ids).stream()
                .map(mapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void deleteByFeature(Long featureId) {
        repository.deleteByFeatureId(featureId);
    }

    @Override
    public void validateFeatureValue(ProductFeatureValueDTO dto) {
        // Implement validation logic
    }

    @Override
    public void validateAndTransform(ProductFeatureValueDTO dto) {
        validateFeatureValue(dto);
        // Add transformation logic if needed
    }

    @Override
    public List<ProductFeatureValueDTO> validateAndTransformBulk(List<ProductFeatureValueDTO> dtos) {
        dtos.forEach(this::validateAndTransform);
        return dtos;
    }

    @Override
    public void validateBulkUpdate(Map<String, Object> updates) {
        // Implement bulk update validation
    }

    @Override
    public List<ProductFeatureValueDTO> findByFeature(Long featureId) {
        return repository.findByFeatureId(featureId).stream()
                .map(mapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public Page<ProductFeatureValueDTO> findByFeature(Long featureId, Pageable pageable) {
        return repository.findByFeatureId(featureId, pageable)
                .map(mapper::toDTO);
    }

    @Override
    public Optional<ProductFeatureValueDTO> findByFeatureAndId(Long featureId, Long valueId) {
        return repository.findByFeatureIdAndId(featureId, valueId)
                .map(mapper::toDTO);
    }

    @Override
    public List<ProductFeatureValueDTO> findByFeatureWithValues(Long featureId) {
        return repository.findByFeatureIdAndValueIsNotNull(featureId).stream()
                .map(mapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<ProductFeatureValueDTO> findByFeatureAndValueType(Long featureId, String type) {
        return repository.findByFeatureIdAndValueType(featureId, type).stream()
                .map(mapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<ProductFeatureValueDTO> findByFeatureAndValue(Long featureId, String value) {
        return repository.findByFeatureIdAndValue(featureId, value).stream()
                .map(mapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<ProductFeatureValueDTO> findByFeatureAndNumericValueGreaterThan(Long featureId, Double value) {
        return repository.findByFeatureIdAndNumericValueGreaterThan(featureId, value).stream()
                .map(mapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<ProductFeatureValueDTO> findByFeatureAndNumericValueLessThan(Long featureId, Double value) {
        return repository.findByFeatureIdAndNumericValueLessThan(featureId, value).stream()
                .map(mapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<ProductFeatureValueDTO> findByFeatureAndNumericValueBetween(Long featureId, Double minValue, Double maxValue) {
        return repository.findByFeatureIdAndNumericValueBetween(featureId, minValue, maxValue).stream()
                .map(mapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<ProductFeatureValueDTO> findByFeatureAndArrayContains(Long featureId, String arrayElement) {
        return repository.findByFeatureIdAndArrayContains(featureId, arrayElement).stream()
                .map(mapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<ProductFeatureValueDTO> findByFeatureAndJsonPattern(Long featureId, String jsonPattern) {
        return repository.findByFeatureIdAndJsonPattern(featureId, jsonPattern).stream()
                .map(mapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<ProductFeatureValueDTO> findByFeatureAndValueContaining(Long featureId, String searchText) {
        return repository.findByFeatureIdAndValueContaining(featureId, searchText).stream()
                .map(mapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<ProductFeatureValueDTO> findByFeatureAndNestedKeyValue(Long featureId, String key, String value) {
        return repository.findByFeatureIdAndNestedKeyValue(featureId, key, value).stream()
                .map(mapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public Page<ProductFeatureValueDTO> findByFeatureAndValueTypePaged(Long featureId, String type, Pageable pageable) {
        return repository.findByFeatureIdAndValueType(featureId, type, pageable)
                .map(mapper::toDTO);
    }

    @Override
    public List<ProductFeatureValueDTO> findByProductSkuAndTemplateCode(String productSku, String templateCode) {
        return repository.findByProductSkuAndTemplateCode(productSku, templateCode).stream()
                .map(mapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<ProductFeatureValueDTO> findByUnitOfMeasureCode(String unitCode) {
        return repository.findByUnitOfMeasureCode(unitCode).stream()
                .map(mapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<ProductFeatureValueDTO> findByFeatureAndUnitOfMeasureCode(Long featureId, String unitCode) {
        return repository.findByFeatureIdAndUnitOfMeasureCode(featureId, unitCode).stream()
                .map(mapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<ProductFeatureValueDTO> findByTemplate(Long templateId) {
        return repository.findByTemplateId(templateId).stream()
                .map(mapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<ProductFeatureValueDTO> findByProduct(Long productId) {
        return repository.findByProductId(productId).stream()
                .map(mapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public Long countByFeatureAndValueType(Long featureId, String type) {
        return repository.countByFeatureIdAndValueType(featureId, type);
    }

    @Override
    public Double averageNumericValueByFeature(Long featureId) {
        return repository.averageNumericValueByFeatureId(featureId);
    }

    @Override
    public Map<String, Long> countValuesByType(Long featureId) {
        Map<String, Long> result = repository.countValuesByType(featureId);
        return result;
    }

    @Override
    public Map<String, Double> getNumericValueStatistics(Long featureId) {
        Map<String, Double> result = repository.getNumericValueStatistics(featureId);
        return result;
    }

    @Override
    public List<Map<String, Object>> getValueTrends(Long featureId, String interval) {
        List<ProductFeatureValue> values = repository.getValueTrends(featureId, interval);
        if (values == null || values.isEmpty()) {
            return List.of();
        }
        return values.stream()
                .map(value -> {
                    Map<String, Object> result = new HashMap<>();
                    result.put("id", value.getId());
                    result.put("value", value.getValueAsString());
                    result.put("timestamp", value.getCreatedAt());
                    return result;
                })
                .collect(Collectors.toList());
    }

    @Override
    public Map<String, Object> getValueDistribution(Long featureId) {
        Map<String, Long> distribution = repository.getValueDistribution(featureId);
        Map<String, Object> result = new HashMap<>();
        distribution.forEach((key, value) -> result.put(key, value));
        return result;
    }

    @Override
    public List<Map<String, Object>> getValueHistory(Long featureId, Long valueId) {
        List<ProductFeatureValue> history = repository.getValueHistory(featureId, valueId);
        if (history == null || history.isEmpty()) {
            return List.of();
        }
        return history.stream()
                .map(value -> {
                    Map<String, Object> result = new HashMap<>();
                    result.put("id", value.getId());
                    result.put("value", value.getValueAsString());
                    result.put("timestamp", value.getCreatedAt());
                    result.put("status", value.getValidationStatus());
                    return result;
                })
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public Map<String, Object> bulkUpdateValues(Long featureId, Map<String, Object> updates) {
        // Implement bulk update logic
        return updates;
    }

    @Override
    public List<ProductFeatureValueDTO> normalizeNumericValues(Long featureId) {
        // Implement normalization logic
        return List.of();
    }

    @Override
    public List<ProductFeatureValueDTO> searchByValuePattern(Long featureId, String pattern) {
        return repository.searchByValuePattern(featureId, pattern).stream()
                .map(mapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<ProductFeatureValueDTO> findSimilarValues(Long featureId, String value, double threshold) {
        return repository.findSimilarValues(featureId, value, threshold).stream()
                .map(mapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public byte[] exportFeatureValuesToJson(Long featureId) {
        List<ProductFeatureValueDTO> values = findByFeature(featureId);
        try {
            return objectMapper.writeValueAsBytes(values);
        } catch (Exception e) {
            throw new RuntimeException("Error exporting feature values to JSON", e);
        }
    }

    @Override
    public List<ProductFeatureValueDTO> importFeatureValuesFromJson(Long featureId, byte[] jsonData) {
        try {
            List<ProductFeatureValueDTO> dtos = objectMapper.readValue(jsonData,
                    objectMapper.getTypeFactory().constructCollectionType(List.class, ProductFeatureValueDTO.class));
            return saveAll(dtos);
        } catch (Exception e) {
            throw new RuntimeException("Error importing feature values from JSON", e);
        }
    }

    @Override
    public void addValidationRule(Long featureId, String ruleExpression) {
        // Implement validation rule addition
    }

    @Override
    public void removeValidationRule(Long featureId, String ruleId) {
        // Implement validation rule removal
    }

    @Override
    public List<String> getValidationRules(Long featureId) {
        return repository.getValidationRules(featureId);
    }

    @Override
    public void refreshCache(Long featureId) {
        // Implement cache refresh logic
    }

    @Override
    public void invalidateCache(Long featureId) {
        // Implement cache invalidation logic
    }

    @Override
    public JsonNode convertToJsonNode(Object value, String type) {
        try {
            if (value == null) return null;
            
            switch (type.toUpperCase()) {
                case "STRING":
                    return objectMapper.valueToTree(value.toString());
                case "NUMBER":
                    return objectMapper.valueToTree(Double.parseDouble(value.toString()));
                case "BOOLEAN":
                    return objectMapper.valueToTree(Boolean.parseBoolean(value.toString()));
                case "JSON":
                    return objectMapper.readTree(value.toString());
                default:
                    throw new IllegalArgumentException("Unsupported type: " + type);
            }
        } catch (Exception e) {
            throw new RuntimeException("Error converting value to JsonNode", e);
        }
    }

    @Override
    @Transactional
    public ProductFeatureValueDTO convertUnit(ProductFeatureValueDTO dto, String targetUnitCode) {
        // Implement unit conversion logic
        return dto;
    }

    @Override
    public void addAuditEntry(Long valueId, String action, String details) {
        // Implement audit entry logic
    }

    @Override
    public List<ProductFeatureValueDTO> findByStatus(String status) {
        return repository.findByValidationStatus(status).stream()
                .map(mapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<ProductFeatureValueDTO> findByValidationMessage(String message) {
        return repository.findByValidationMessageContaining(message).stream()
                .map(mapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<ProductFeatureValueDTO> findByType(String type) {
        return repository.findByFeatureType(type).stream()
                .map(mapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<ProductFeatureValueDTO> findByUnit(String unit) {
        return repository.findByUnitOfMeasureCode(unit).stream()
                .map(mapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public void validateAll() {
        repository.findAll().forEach(entity -> validateFeatureValue(mapper.toDTO(entity)));
    }

    @Override
    public void transformAll() {
        repository.findAll().forEach(entity -> validateAndTransform(mapper.toDTO(entity)));
    }
}
