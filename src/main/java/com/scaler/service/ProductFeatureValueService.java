package com.scaler.service;

import com.scaler.dto.ProductFeatureValueDTO;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface ProductFeatureValueService {
    ProductFeatureValueDTO save(ProductFeatureValueDTO dto);
    ProductFeatureValueDTO update(ProductFeatureValueDTO dto);
    List<ProductFeatureValueDTO> saveAll(List<ProductFeatureValueDTO> dtos);
    void delete(Long id);
    void delete(ProductFeatureValueDTO dto);
    Optional<ProductFeatureValueDTO> findById(Long id);
    List<ProductFeatureValueDTO> findAllById(List<Long> ids);
    void deleteByFeature(Long featureId);
    void validateFeatureValue(ProductFeatureValueDTO dto);
    void validateAndTransform(ProductFeatureValueDTO dto);
    List<ProductFeatureValueDTO> validateAndTransformBulk(List<ProductFeatureValueDTO> dtos);
    void validateBulkUpdate(Map<String, Object> updates);
    
    // Feature-related queries
    List<ProductFeatureValueDTO> findByFeature(Long featureId);
    Page<ProductFeatureValueDTO> findByFeature(Long featureId, Pageable pageable);
    Optional<ProductFeatureValueDTO> findByFeatureAndId(Long featureId, Long valueId);
    List<ProductFeatureValueDTO> findByFeatureWithValues(Long featureId);
    List<ProductFeatureValueDTO> findByFeatureAndValueType(Long featureId, String type);
    List<ProductFeatureValueDTO> findByFeatureAndValue(Long featureId, String value);
    List<ProductFeatureValueDTO> findByFeatureAndNumericValueGreaterThan(Long featureId, Double value);
    List<ProductFeatureValueDTO> findByFeatureAndNumericValueLessThan(Long featureId, Double value);
    List<ProductFeatureValueDTO> findByFeatureAndNumericValueBetween(Long featureId, Double minValue, Double maxValue);
    List<ProductFeatureValueDTO> findByFeatureAndArrayContains(Long featureId, String arrayElement);
    List<ProductFeatureValueDTO> findByFeatureAndJsonPattern(Long featureId, String jsonPattern);
    List<ProductFeatureValueDTO> findByFeatureAndValueContaining(Long featureId, String searchText);
    List<ProductFeatureValueDTO> findByFeatureAndNestedKeyValue(Long featureId, String key, String value);
    Page<ProductFeatureValueDTO> findByFeatureAndValueTypePaged(Long featureId, String type, Pageable pageable);
    
    // Product and template related queries
    List<ProductFeatureValueDTO> findByProductSkuAndTemplateCode(String productSku, String templateCode);
    List<ProductFeatureValueDTO> findByUnitOfMeasureCode(String unitCode);
    List<ProductFeatureValueDTO> findByFeatureAndUnitOfMeasureCode(Long featureId, String unitCode);
    List<ProductFeatureValueDTO> findByTemplate(Long templateId);
    List<ProductFeatureValueDTO> findByProduct(Long productId);
    
    // Analytics and statistics
    Long countByFeatureAndValueType(Long featureId, String type);
    Double averageNumericValueByFeature(Long featureId);
    Map<String, Long> countValuesByType(Long featureId);
    Map<String, Double> getNumericValueStatistics(Long featureId);
    List<Map<String, Object>> getValueTrends(Long featureId, String interval);
    Map<String, Object> getValueDistribution(Long featureId);
    List<Map<String, Object>> getValueHistory(Long featureId, Long valueId);
    
    // Bulk operations and transformations
    Map<String, Object> bulkUpdateValues(Long featureId, Map<String, Object> updates);
    List<ProductFeatureValueDTO> normalizeNumericValues(Long featureId);
    List<ProductFeatureValueDTO> searchByValuePattern(Long featureId, String pattern);
    List<ProductFeatureValueDTO> findSimilarValues(Long featureId, String value, double threshold);
    
    // Export and import
    byte[] exportFeatureValuesToJson(Long featureId);
    List<ProductFeatureValueDTO> importFeatureValuesFromJson(Long featureId, byte[] jsonData);
    
    // Validation rules
    void addValidationRule(Long featureId, String ruleExpression);
    void removeValidationRule(Long featureId, String ruleId);
    List<String> getValidationRules(Long featureId);
    
    // Cache management
    void refreshCache(Long featureId);
    void invalidateCache(Long featureId);
    
    // Utility methods
    JsonNode convertToJsonNode(Object value, String type);
    ProductFeatureValueDTO convertUnit(ProductFeatureValueDTO dto, String targetUnitCode);
    void addAuditEntry(Long valueId, String action, String details);
    
    // Additional methods
    List<ProductFeatureValueDTO> findByStatus(String status);
    List<ProductFeatureValueDTO> findByValidationMessage(String message);
    List<ProductFeatureValueDTO> findByType(String type);
    List<ProductFeatureValueDTO> findByUnit(String unit);
    void validateAll();
    void transformAll();
}
