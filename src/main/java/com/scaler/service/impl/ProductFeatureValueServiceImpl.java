package com.scaler.service.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.scaler.dto.ProductFeatureValueDTO;
import com.scaler.entity.ProductFeature;
import com.scaler.entity.ProductFeatureValue;
import com.scaler.entity.UnitOfMeasure;
import com.scaler.exception.ResourceNotFoundException;
import com.scaler.exception.ValidationException;
import com.scaler.mapper.ProductFeatureValueMapper;
import com.scaler.repository.ProductFeatureRepository;
import com.scaler.repository.ProductFeatureValueRepository;
import com.scaler.repository.UnitOfMeasureRepository;
import com.scaler.service.ProductFeatureValueService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductFeatureValueServiceImpl implements ProductFeatureValueService {

    private final ProductFeatureValueRepository repository;
    private final ProductFeatureRepository featureRepository;
    private final UnitOfMeasureRepository unitRepository;
    private final ProductFeatureValueMapper mapper;
    private final ObjectMapper objectMapper = new ObjectMapper();

    private List<ProductFeatureValueDTO> convertToDTOList(List<ProductFeatureValue> entities) {
        return entities.stream()
                .map(mapper::toDTO)
                .collect(Collectors.toList());
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
                .map(mapper::toEntity)
                .collect(Collectors.toList());
        entities = repository.saveAll(entities);
        return convertToDTOList(entities);
    }

    @Override
    @Transactional
    public void delete(UUID id) {
        repository.deleteById(id);
    }

    @Override
    @Transactional
    public void delete(ProductFeatureValueDTO dto) {
        repository.delete(mapper.toEntity(dto));
    }

    @Override
    public Optional<ProductFeatureValueDTO> findById(UUID id) {
        return repository.findById(id).map(mapper::toDTO);
    }

    @Override
    public List<ProductFeatureValueDTO> findAllById(List<UUID> ids) {
        return convertToDTOList(repository.findAllById(ids));
    }

    @Override
    @Transactional
    public void deleteByFeature(UUID featureId) {
        repository.deleteByFeatureId(featureId);
    }

    @Override
    public void validateFeatureValue(ProductFeatureValueDTO dto) {
        ProductFeature feature = featureRepository.findById(dto.getFeatureId())
                .orElseThrow(() -> new ResourceNotFoundException("Feature not found with id: " + dto.getFeatureId()));

        // Validate unit if specified
        if (dto.getUnitOfMeasure() != null) {
            unitRepository.findByCode(dto.getUnitOfMeasure())
                    .orElseThrow(() -> new ValidationException("Invalid unit of measure: " + dto.getUnitOfMeasure()));
        }

        // Validate value format
        if (feature.getValidationPattern() != null && !feature.getValidationPattern().isEmpty()) {
            Pattern pattern = Pattern.compile(feature.getValidationPattern());
            if (dto.getAttributeValue() != null && !pattern.matcher(dto.getAttributeValue().toString()).matches()) {
                throw new ValidationException("Value does not match validation pattern");
            }
        }

        // Validate value range
        if (feature.getMinValue() != null || feature.getMaxValue() != null) {
            try {
                double value = Double.parseDouble(dto.getAttributeValue().toString());
                if (feature.getMinValue() != null && value < Double.parseDouble(feature.getMinValue())) {
                    throw new ValidationException("Value is below minimum allowed value");
                }
                if (feature.getMaxValue() != null && value > Double.parseDouble(feature.getMaxValue())) {
                    throw new ValidationException("Value is above maximum allowed value");
                }
            } catch (NumberFormatException e) {
                throw new ValidationException("Value must be a number for range validation");
            }
        }

        // Validate allowed values
        if (feature.getAllowedValues() != null && !feature.getAllowedValues().isEmpty()) {
            List<String> allowedValues = Arrays.asList(feature.getAllowedValues().split(","));
            if (!allowedValues.contains(dto.getAttributeValue().toString())) {
                throw new ValidationException("Value is not in the list of allowed values");
            }
        }
    }

    @Override
    public List<ProductFeatureValueDTO> validateAndTransformBulk(List<ProductFeatureValueDTO> dtos) {
        return dtos.stream()
            .map(dto -> {
                validateFeatureValue(dto);
                return dto;
            })
            .collect(Collectors.toList());
    }

    @Override
    public void validateBulkUpdate(Map<String, Object> updates) {
        updates.forEach((id, value) -> {
            try {
                Optional<ProductFeatureValue> optionalValue = repository.findById(UUID.fromString(id));
                if (optionalValue.isPresent()) {
                    ProductFeatureValue featureValue = optionalValue.get();
                    featureValue.setAttributeValues(objectMapper.valueToTree(value));
                    validateFeatureValue(mapper.toDTO(featureValue));
                }
            } catch (Exception e) {
                throw new ValidationException("Error validating update for " + id + ": " + e.getMessage());
            }
        });
    }

    @Override
    public List<ProductFeatureValueDTO> findByFeature(UUID featureId) {
        return convertToDTOList(repository.findByFeatureIdOrderByCreatedDateDesc(featureId));
    }

    @Override
    public Page<ProductFeatureValueDTO> findByFeature(UUID featureId, Pageable pageable) {
        return repository.findByFeatureId(featureId, pageable)
                .map(mapper::toDTO);
    }

    @Override
    public Optional<ProductFeatureValueDTO> findByFeatureAndId(UUID featureId, UUID valueId) {
        return repository.findByFeatureIdAndId(featureId, valueId).map(mapper::toDTO);
    }

    @Override
    public List<ProductFeatureValueDTO> findByFeatureWithValues(UUID featureId) {
        return convertToDTOList(repository.findByFeatureIdAndAttributeValuesIsNotNull(featureId));
    }

    @Override
    public List<ProductFeatureValueDTO> findByFeatureAndValueType(UUID featureId, String type) {
        return List.of();
    }

    @Override
    public List<ProductFeatureValueDTO> findByFeatureAndValue(UUID featureId, String value) {
        return List.of();
    }

    @Override
    public List<ProductFeatureValueDTO> findByFeatureAndNumericValueGreaterThan(UUID featureId, Double value) {
        return List.of();
    }

    @Override
    public List<ProductFeatureValueDTO> findByFeatureAndNumericValueLessThan(UUID featureId, Double value) {
        return List.of();
    }

    @Override
    public List<ProductFeatureValueDTO> findByFeatureAndNumericValueBetween(UUID featureId, Double minValue, Double maxValue) {
        return List.of();
    }

    @Override
    public List<ProductFeatureValueDTO> findByFeatureAndArrayContains(UUID featureId, String arrayElement) {
        return List.of();
    }

    @Override
    public List<ProductFeatureValueDTO> findByFeatureAndJsonPattern(UUID featureId, String jsonPattern) {
        return List.of();
    }

    @Override
    public List<ProductFeatureValueDTO> findByFeatureAndValueContaining(UUID featureId, String searchText) {
        return List.of();
    }

    @Override
    public List<ProductFeatureValueDTO> findByFeatureAndNestedKeyValue(UUID featureId, String key, String value) {
        return List.of();
    }

    @Override
    public Page<ProductFeatureValueDTO> findByFeatureAndValueTypePaged(UUID featureId, String type, Pageable pageable) {
        return null;
    }

    @Override
    public List<ProductFeatureValueDTO> findByProductSkuAndTemplateCode(String productSku, String templateCode) {
        return List.of();
    }

    @Override
    public List<ProductFeatureValueDTO> findByUnitOfMeasureCode(String unitCode) {
        return List.of();
    }

    @Override
    public List<ProductFeatureValueDTO> findByFeatureAndUnitOfMeasureCode(UUID featureId, String unitCode) {
        return List.of();
    }

    @Override
    public List<ProductFeatureValueDTO> findByTemplate(UUID templateId) {
        return List.of();
    }

    @Override
    public List<ProductFeatureValueDTO> findByProduct(UUID productId) {
        return List.of();
    }

    @Override
    public Long countByFeatureAndValueType(UUID featureId, String type) {
        return 0L;
    }

    @Override
    public Double averageNumericValueByFeature(UUID featureId) {
        return 0.0;
    }

    @Override
    public Map<String, Long> countValuesByType(UUID featureId) {
        return Map.of();
    }

    @Override
    public Map<String, Double> getNumericValueStatistics(UUID featureId) {
        return Map.of();
    }

    @Override
    public List<Map<String, Object>> getValueTrends(UUID featureId, String interval) {
        ProductFeature feature = featureRepository.findById(featureId)
                .orElseThrow(() -> new ResourceNotFoundException("Feature not found with id: " + featureId));

        List<ProductFeatureValue> values = repository.findByFeatureIdOrderByCreatedDateDesc(featureId);
        
        // Group values by interval
        Map<String, List<ProductFeatureValue>> groupedValues = values.stream()
                .collect(Collectors.groupingBy(value -> getIntervalKey(value.getCreatedDate(), interval)));

        // Calculate statistics for each interval
        return groupedValues.entrySet().stream()
                .map(entry -> {
                    Map<String, Object> stats = new HashMap<>();
                    stats.put("interval", entry.getKey());
                    stats.put("count", entry.getValue().size());
                    stats.put("values", entry.getValue().stream()
                            .map(ProductFeatureValue::getAttributeValues)
                            .collect(Collectors.toList()));
                    return stats;
                })
                .collect(Collectors.toList());
    }

    @Override
    public Map<String, Object> getValueDistribution(UUID featureId) {
        return Map.of();
    }

    @Override
    public List<Map<String, Object>> getValueHistory(UUID featureId, UUID valueId) {
        return List.of();
    }

    private String getIntervalKey(LocalDateTime dateTime, String interval) {
        switch (interval.toLowerCase()) {
            case "hourly":
                return String.format("%d-%02d-%02d %02d:00", 
                    dateTime.getYear(), dateTime.getMonthValue(), dateTime.getDayOfMonth(), dateTime.getHour());
            case "daily":
                return String.format("%d-%02d-%02d", 
                    dateTime.getYear(), dateTime.getMonthValue(), dateTime.getDayOfMonth());
            case "weekly":
                return String.format("%d-W%d", 
                    dateTime.getYear(), dateTime.get(java.time.temporal.WeekFields.ISO.weekOfWeekBasedYear()));
            case "monthly":
                return String.format("%d-%02d", 
                    dateTime.getYear(), dateTime.getMonthValue());
            default:
                throw new IllegalArgumentException("Invalid interval: " + interval);
        }
    }

    @Override
    public Map<String, Object> bulkUpdateValues(UUID featureId, Map<String, Object> updates) {
        ProductFeature feature = featureRepository.findById(featureId)
                .orElseThrow(() -> new ResourceNotFoundException("Feature not found with id: " + featureId));

        Map<String, Object> results = new HashMap<>();
        List<ProductFeatureValue> updatedValues = new ArrayList<>();
        List<String> errors = new ArrayList<>();

        updates.forEach((id, value) -> {
            try {
                Optional<ProductFeatureValue> optionalValue = repository.findById(UUID.fromString(id));
                if (optionalValue.isPresent()) {
                    ProductFeatureValue featureValue = optionalValue.get();
                    featureValue.setAttributeValues(objectMapper.valueToTree(value));
                    validateFeatureValue(mapper.toDTO(featureValue));
                    updatedValues.add(repository.save(featureValue));
                } else {
                    errors.add("Value not found with id: " + id);
                }
            } catch (Exception e) {
                errors.add(String.format("Error updating value %s: %s", id, e.getMessage()));
            }
        });

        results.put("updated", updatedValues.size());
        results.put("errors", errors);
        return results;
    }

    @Override
    public void validateAndTransform(ProductFeatureValueDTO dto) {
        validateFeatureValue(dto);
        transformValue(dto);
    }

    private void transformValue(ProductFeatureValueDTO dto) {
        ProductFeature feature = featureRepository.findById(dto.getFeatureId())
                .orElseThrow(() -> new ResourceNotFoundException("Feature not found: " + dto.getFeatureId()));

        // Apply default value if value is null
        if ((dto.getAttributeValue() == null || dto.getAttributeValue().isEmpty()) && feature.getDefaultValue() != null) {
            dto.setAttributeValue(objectMapper.valueToTree(feature.getDefaultValue()));
        }

        // Set audit fields
        LocalDateTime now = LocalDateTime.now();
        if (dto.getCreatedAt() == null) {
            dto.setCreatedAt(now);
            dto.setCreatedBy("system");
        }
        dto.setLastModifiedAt(now);
        dto.setLastModifiedBy("system");
    }

    @Override
    public void addAuditEntry(UUID valueId, String action, String details) {
        ProductFeatureValue value = repository.findById(valueId)
                .orElseThrow(() -> new ResourceNotFoundException("Value not found: " + valueId));

        Map<String, Object> auditEntry = new HashMap<>();
        auditEntry.put("timestamp", LocalDateTime.now());
        auditEntry.put("action", action);
        auditEntry.put("details", details);
        auditEntry.put("user", "system");

        // Add audit entry to metadata
        try {
            JsonNode currentMetadata = value.getMetadata() != null ?
                    value.getMetadata() :
                    objectMapper.createObjectNode();

            ((com.fasterxml.jackson.databind.node.ObjectNode) currentMetadata)
                    .set("audit_" + System.currentTimeMillis(),
                            objectMapper.valueToTree(auditEntry));

            value.setMetadata(currentMetadata);
            repository.save(value);
        } catch (Exception e) {
            throw new RuntimeException("Error adding audit entry", e);
        }
    }

    @Override
    public List<String> getValidationRules(UUID featureId) {
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
    public List<ProductFeatureValueDTO> normalizeNumericValues(UUID featureId) {
        log.debug("Normalizing numeric values for featureId: {}", featureId);
        return List.of();
    }

    @Override
    public List<ProductFeatureValueDTO> searchByValuePattern(UUID featureId, String pattern) {
        List<ProductFeatureValue> values = repository.searchByValuePattern(featureId, pattern);
        return convertToDTOList(values);
    }

    @Override
    public List<ProductFeatureValueDTO> findSimilarValues(UUID featureId, String value, double threshold) {
        List<ProductFeatureValue> values = repository.findSimilarValues(featureId, value, threshold);
        return convertToDTOList(values);
    }

    @Override
    public byte[] exportFeatureValuesToJson(UUID featureId) {
        List<ProductFeatureValueDTO> values = findByFeature(featureId);
        try {
            return objectMapper.writeValueAsBytes(values);
        } catch (Exception e) {
            throw new RuntimeException("Error exporting feature values to JSON", e);
        }
    }

    @Override
    public List<ProductFeatureValueDTO> importFeatureValuesFromJson(UUID featureId, byte[] jsonData) {
        try {
            List<ProductFeatureValueDTO> dtos = objectMapper.readValue(jsonData,
                    objectMapper.getTypeFactory().constructCollectionType(List.class, ProductFeatureValueDTO.class));
            return saveAll(dtos);
        } catch (Exception e) {
            throw new RuntimeException("Error importing feature values from JSON", e);
        }
    }

    @Override
    public void addValidationRule(UUID featureId, String ruleExpression) {
        // Implement validation rule addition
    }

    @Override
    public void removeValidationRule(UUID featureId, String ruleId) {
        // Implement validation rule removal
    }

    @Override
    public void refreshCache(UUID featureId) {
        // Implement cache refresh logic
    }

    @Override
    public void invalidateCache(UUID featureId) {
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
    public ProductFeatureValueDTO convertUnit(ProductFeatureValueDTO dto, String targetUnitCode) {
        if (dto.getUnitOfMeasure() == null || dto.getUnitOfMeasure().equals(targetUnitCode)) {
            return dto;
        }

        UnitOfMeasure sourceUnit = unitRepository.findByCode(dto.getUnitOfMeasure())
                .orElseThrow(() -> new ValidationException("Source unit not found: " + dto.getUnitOfMeasure()));
        UnitOfMeasure targetUnit = unitRepository.findByCode(targetUnitCode)
                .orElseThrow(() -> new ValidationException("Target unit not found: " + targetUnitCode));

        if (!sourceUnit.getBaseUnit().equals(targetUnit.getBaseUnit())) {
            throw new ValidationException("Cannot convert between different base units");
        }

        try {
            double value = Double.parseDouble(dto.getAttributeValue().toString());
            double convertedValue = value * (sourceUnit.getConversionFactor() / targetUnit.getConversionFactor());
            dto.setAttributeValue(objectMapper.valueToTree(convertedValue));
            dto.setUnitOfMeasure(targetUnitCode);
        } catch (NumberFormatException e) {
            throw new ValidationException("Value must be numeric for unit conversion");
        }

        return dto;
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
