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

import java.util.ArrayList;
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

    private List<ProductFeatureValueDTO> convertToDTOList(List<ProductFeatureValue> entities) {
        List<ProductFeatureValueDTO> dtos = new ArrayList<>();
        for (ProductFeatureValue entity : entities) {
            dtos.add(mapper.toDTO(entity));
        }
        return dtos;
    }

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
                .map(dto -> mapper.toEntity(dto))
                .collect(Collectors.toList());
        entities = repository.saveAll(entities);
        return convertToDTOList(entities);
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
        return repository.findById(id).map(dto -> mapper.toDTO(dto));
    }

    @Override
    public List<ProductFeatureValueDTO> findAllById(List<Long> ids) {
        return convertToDTOList(repository.findAllById(ids));
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
        List<ProductFeatureValue> values = repository.findByFeatureId(featureId);
        return convertToDTOList(values);
    }

    @Override
    public Page<ProductFeatureValueDTO> findByFeature(Long featureId, Pageable pageable) {
        return null;//repository.findByFeatureId(featureId, pageable)
                //.map(entity -> mapper.toDTO(entity));
    }

    @Override
    public Optional<ProductFeatureValueDTO> findByFeatureAndId(Long featureId, Long valueId) {
        return repository.findByFeatureIdAndId(featureId, valueId)
                .map(entity -> mapper.toDTO(entity));
    }

    @Override
    public List<ProductFeatureValueDTO> findByFeatureWithValues(Long featureId) {
        List<ProductFeatureValue> values = repository.findByFeatureIdAndValueIsNotNull(featureId);
        return convertToDTOList(values);
    }

    @Override
    public List<ProductFeatureValueDTO> findByFeatureAndValueType(Long featureId, String type) {
        List<ProductFeatureValue> values = repository.findByFeatureIdAndValueType(featureId, type);
        return convertToDTOList(values);
    }

    @Override
    public List<ProductFeatureValueDTO> findByFeatureAndValue(Long featureId, String value) {
        List<ProductFeatureValue> values = repository.findByFeatureIdAndValue(featureId, value);
        return convertToDTOList(values);
    }

    @Override
    public List<ProductFeatureValueDTO> findByFeatureAndNumericValueGreaterThan(Long featureId, Double value) {
        List<ProductFeatureValue> values = repository.findByFeatureIdAndNumericValueGreaterThan(featureId, value);
        return convertToDTOList(values);
    }

    @Override
    public List<ProductFeatureValueDTO> findByFeatureAndNumericValueLessThan(Long featureId, Double value) {
        List<ProductFeatureValue> values = repository.findByFeatureIdAndNumericValueLessThan(featureId, value);
        return convertToDTOList(values);
    }

    @Override
    public List<ProductFeatureValueDTO> findByFeatureAndNumericValueBetween(Long featureId, Double minValue, Double maxValue) {
        List<ProductFeatureValue> values = repository.findByFeatureIdAndNumericValueBetween(featureId, minValue, maxValue);
        return convertToDTOList(values);
    }

    @Override
    public List<ProductFeatureValueDTO> findByFeatureAndArrayContains(Long featureId, String arrayElement) {
        List<ProductFeatureValue> values = repository.findByFeatureIdAndArrayContains(featureId, arrayElement);
        return convertToDTOList(values);
    }

    @Override
    public List<ProductFeatureValueDTO> findByFeatureAndJsonPattern(Long featureId, String jsonPattern) {
        List<ProductFeatureValue> values = repository.findByFeatureIdAndJsonPattern(featureId, jsonPattern);
        return convertToDTOList(values);
    }

    @Override
    public List<ProductFeatureValueDTO> findByFeatureAndValueContaining(Long featureId, String searchText) {
        List<ProductFeatureValue> values = repository.findByFeatureIdAndValueContaining(featureId, searchText);
        return convertToDTOList(values);
    }

    @Override
    public List<ProductFeatureValueDTO> findByFeatureAndNestedKeyValue(Long featureId, String key, String value) {
        List<ProductFeatureValue> values = repository.findByFeatureIdAndNestedKeyValue(featureId, key, value);
        return convertToDTOList(values);
    }

    @Override
    public Page<ProductFeatureValueDTO> findByFeatureAndValueTypePaged(Long featureId, String type, Pageable pageable) {
        return repository.findByFeatureIdAndValueType(featureId, type, pageable)
                .map(entity -> mapper.toDTO(entity));
    }

    @Override
    public List<ProductFeatureValueDTO> findByProductSkuAndTemplateCode(String productSku, String templateCode) {
        List<ProductFeatureValue> values = repository.findByProductSkuAndTemplateCode(productSku, templateCode);
        return convertToDTOList(values);
    }

    @Override
    public List<ProductFeatureValueDTO> findByUnitOfMeasureCode(String unitCode) {
        List<ProductFeatureValue> values = repository.findByUnitOfMeasure(unitCode);
        return convertToDTOList(values);
    }

    @Override
    public List<ProductFeatureValueDTO> findByFeatureAndUnitOfMeasureCode(Long featureId, String unitCode) {
        List<ProductFeatureValue> values = repository.findByFeatureIdAndUnitOfMeasure(featureId, unitCode);
        return convertToDTOList(values);
    }

    @Override
    public List<ProductFeatureValueDTO> findByTemplate(Long templateId) {
        List<ProductFeatureValue> values = repository.findByTemplateId(templateId);
        return convertToDTOList(values);
    }

    @Override
    public List<ProductFeatureValueDTO> findByProduct(Long productId) {
        List<ProductFeatureValue> values = repository.findByProductId(productId);
        return convertToDTOList(values);
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
        List<Map<String, Object>> results = repository.countValuesByType(featureId);
        Map<String, Long> converted = new HashMap<>();
        results.forEach(map -> {
            String type = (String) map.get("type");
            Long count = ((Number) map.get("count")).longValue();
            converted.put(type, count);
        });
        return converted;
    }

    @Override
    public Map<String, Double> getNumericValueStatistics(Long featureId) {
        Map<String, Object> result = repository.getNumericValueStatistics(featureId);
        Map<String, Double> converted = new HashMap<>();
        if (result != null) {
            converted.put("min_value", ((Number) result.get("min_value")).doubleValue());
            converted.put("max_value", ((Number) result.get("max_value")).doubleValue());
            converted.put("avg_value", ((Number) result.get("avg_value")).doubleValue());
        }
        return converted;
    }

    @Override
    public List<Map<String, Object>> getValueTrends(Long featureId, String interval) {
        return repository.getValueTrends(featureId, interval);
    }

    @Override
    public Map<String, Object> getValueDistribution(Long featureId) {
        List<Map<String, Object>> distribution = repository.getValueDistribution(featureId);
        Map<String, Object> converted = new HashMap<>();
        distribution.forEach(map -> {
            String value = (String) map.get("value");
            Number count = (Number) map.get("count");
            converted.put(value, count);
        });
        return converted;
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
                    result.put("timestamp", value.getCreatedDate());
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
    public List<String> getValidationRules(Long featureId) {
        Map<String, Object> rules = repository.getValidationRules(featureId);
        if (rules != null && rules.containsKey("validationRules")) {
            Object rulesObj = rules.get("validationRules");
            if (rulesObj instanceof List) {
                @SuppressWarnings("unchecked")
                List<String> rulesList = (List<String>) rulesObj;
                return rulesList;
            }
        }
        return List.of();
    }


    @Override
    public List<ProductFeatureValueDTO> normalizeNumericValues(Long featureId) {
        // Implement normalization logic
        return List.of();
    }

    @Override
    public List<ProductFeatureValueDTO> searchByValuePattern(Long featureId, String pattern) {
        List<ProductFeatureValue> values = repository.searchByValuePattern(featureId, pattern);
        return convertToDTOList(values);
    }

    @Override
    public List<ProductFeatureValueDTO> findSimilarValues(Long featureId, String value, double threshold) {
        List<ProductFeatureValue> values = repository.findSimilarValues(featureId, value, threshold);
        return convertToDTOList(values);
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
        List<ProductFeatureValue> values = repository.findByValidationStatus(status);
        return convertToDTOList(values);
    }

    @Override
    public List<ProductFeatureValueDTO> findByValidationMessage(String message) {
        List<ProductFeatureValue> values = repository.findByValidationMessageContaining(message);
        return convertToDTOList(values);
    }

    @Override
    public List<ProductFeatureValueDTO> findByType(String type) {
        List<ProductFeatureValue> values = repository.findByFeatureType(type);
        return convertToDTOList(values);
    }

    @Override
    public List<ProductFeatureValueDTO> findByUnit(String unit) {
        List<ProductFeatureValue> values = repository.findByUnitOfMeasure(unit);
        return convertToDTOList(values);
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
