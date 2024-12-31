package com.scaler.service;

import com.scaler.dto.ProductFeatureValueDTO;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public interface ProductFeatureValueService {
    ProductFeatureValueDTO save(ProductFeatureValueDTO dto);
    ProductFeatureValueDTO update(ProductFeatureValueDTO dto);
    List<ProductFeatureValueDTO> saveAll(List<ProductFeatureValueDTO> dtos);
    void delete(UUID id);
    void delete(ProductFeatureValueDTO dto);
    Optional<ProductFeatureValueDTO> findById(UUID id);
    List<ProductFeatureValueDTO> findAllById(List<UUID> ids);
    void deleteByFeature(UUID featureId);
    void validateFeatureValue(ProductFeatureValueDTO dto);
    void validateAndTransform(ProductFeatureValueDTO dto);
    List<ProductFeatureValueDTO> validateAndTransformBulk(List<ProductFeatureValueDTO> dtos);
    void validateBulkUpdate(Map<String, Object> updates);
    
    // Feature-related queries
    List<ProductFeatureValueDTO> findByFeature(UUID featureId);
    Page<ProductFeatureValueDTO> findByFeature(UUID featureId, Pageable pageable);
    Optional<ProductFeatureValueDTO> findByFeatureAndId(UUID featureId, UUID valueId);
    List<ProductFeatureValueDTO> findByFeatureWithValues(UUID featureId);
    List<ProductFeatureValueDTO> findByFeatureAndValueType(UUID featureId, String type);
    List<ProductFeatureValueDTO> findByFeatureAndValue(UUID featureId, String value);
    List<ProductFeatureValueDTO> findByFeatureAndNumericValueGreaterThan(UUID featureId, Double value);
    List<ProductFeatureValueDTO> findByFeatureAndNumericValueLessThan(UUID featureId, Double value);
    List<ProductFeatureValueDTO> findByFeatureAndNumericValueBetween(UUID featureId, Double minValue, Double maxValue);
    List<ProductFeatureValueDTO> findByFeatureAndArrayContains(UUID featureId, String arrayElement);
    List<ProductFeatureValueDTO> findByFeatureAndJsonPattern(UUID featureId, String jsonPattern);
    List<ProductFeatureValueDTO> findByFeatureAndValueContaining(UUID featureId, String searchText);
    List<ProductFeatureValueDTO> findByFeatureAndNestedKeyValue(UUID featureId, String key, String value);
    Page<ProductFeatureValueDTO> findByFeatureAndValueTypePaged(UUID featureId, String type, Pageable pageable);
    
    // Product and template related queries
    List<ProductFeatureValueDTO> findByProductSkuAndTemplateCode(String productSku, String templateCode);
    List<ProductFeatureValueDTO> findByUnitOfMeasureCode(String unitCode);
    List<ProductFeatureValueDTO> findByFeatureAndUnitOfMeasureCode(UUID featureId, String unitCode);
    List<ProductFeatureValueDTO> findByTemplate(UUID templateId);
    List<ProductFeatureValueDTO> findByProduct(UUID productId);
    
    // Analytics and statistics
    Long countByFeatureAndValueType(UUID featureId, String type);
    Double averageNumericValueByFeature(UUID featureId);
    Map<String, Long> countValuesByType(UUID featureId);
    Map<String, Double> getNumericValueStatistics(UUID featureId);
    List<Map<String, Object>> getValueTrends(UUID featureId, String interval);
    Map<String, Object> getValueDistribution(UUID featureId);
    List<Map<String, Object>> getValueHistory(UUID featureId, UUID valueId);
    
    // Bulk operations and transformations
    Map<String, Object> bulkUpdateValues(UUID featureId, Map<String, Object> updates);
    List<ProductFeatureValueDTO> normalizeNumericValues(UUID featureId);
    List<ProductFeatureValueDTO> searchByValuePattern(UUID featureId, String pattern);
    List<ProductFeatureValueDTO> findSimilarValues(UUID featureId, String value, double threshold);
    
    // Export and import
    byte[] exportFeatureValuesToJson(UUID featureId);
    List<ProductFeatureValueDTO> importFeatureValuesFromJson(UUID featureId, byte[] jsonData);
    
    // Validation rules
    void addValidationRule(UUID featureId, String ruleExpression);
    void removeValidationRule(UUID featureId, String ruleId);
    List<String> getValidationRules(UUID featureId);
    
    // Cache management
    void refreshCache(UUID featureId);
    void invalidateCache(UUID featureId);
    
    // Utility methods
    JsonNode convertToJsonNode(Object value, String type);
    ProductFeatureValueDTO convertUnit(ProductFeatureValueDTO dto, String targetUnitCode);
    void addAuditEntry(UUID valueId, String action, String details);
    
    // Additional methods
    List<ProductFeatureValueDTO> findByStatus(String status);
    List<ProductFeatureValueDTO> findByValidationMessage(String message);
    List<ProductFeatureValueDTO> findByType(String type);
    List<ProductFeatureValueDTO> findByUnit(String unit);
    void validateAll();
    void transformAll();
}
