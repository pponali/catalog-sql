package com.scaler.repository;

import com.scaler.entity.ProductFeature;
import com.scaler.entity.ProductFeatureValue;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public interface ProductFeatureValueRepository extends JpaRepository<ProductFeatureValue, Long> {
    
    @Query(value = "SELECT * FROM product_feature_value pfv " +
           "WHERE pfv.feature_id = ?1", nativeQuery = true)
    List<ProductFeatureValue> findByFeatureId(Long featureId);

    @Query(value = "SELECT * FROM product_feature_value pfv " +
           "WHERE pfv.feature_id = ?1 " +
           "AND pfv.id = ?2", nativeQuery = true)
    Optional<ProductFeatureValue> findByFeatureIdAndId(Long featureId, Long id);

    @Query(value = "SELECT * FROM product_feature_value pfv " +
           "WHERE pfv.feature_id = ?1 " +
           "AND pfv.attribute_values IS NOT NULL", nativeQuery = true)
    List<ProductFeatureValue> findByFeatureIdAndValueIsNotNull(Long featureId);

    @Query(value = "SELECT * FROM product_feature_value pfv " +
           "WHERE pfv.feature_id = ?1 " +
           "AND pfv.type = ?2", nativeQuery = true)
    List<ProductFeatureValue> findByFeatureIdAndValueType(Long featureId, String type);

    @Query(value = "SELECT * FROM product_feature_value pfv " +
           "WHERE pfv.feature_id = ?1 " +
           "AND pfv.attribute_values->>'value' = ?2", nativeQuery = true)
    List<ProductFeatureValue> findByFeatureIdAndValue(Long featureId, String value);

    @Query(value = "SELECT * FROM product_feature_value pfv " +
           "WHERE pfv.product_id = ?1 " +
           "AND pfv.template_id = ?2", nativeQuery = true)
    List<ProductFeatureValue> findByProductSkuAndTemplateCode(String productSku, String templateCode);

    @Query(value = "SELECT * FROM product_feature_value pfv " +
           "WHERE pfv.feature_id = ?1 " +
           "AND pfv.unit_of_measure = ?2", nativeQuery = true)
    List<ProductFeatureValue> findByFeatureIdAndUnitOfMeasureCode(Long featureId, String unitOfMeasureCode);

    @Query(value = "SELECT * FROM product_feature_value pfv " +
           "WHERE pfv.feature_id = ?1 " +
           "AND pfv.type = 'number' " +
           "AND CAST(pfv.attribute_values->>'value' AS DECIMAL) > ?2", nativeQuery = true)
    List<ProductFeatureValue> findByFeatureIdAndNumericValueGreaterThan(Long featureId, Double value);

    @Query(value = "SELECT * FROM product_feature_value pfv " +
           "WHERE pfv.feature_id = ?1 " +
           "AND pfv.type = 'number' " +
           "AND CAST(pfv.attribute_values->>'value' AS DECIMAL) < ?2", nativeQuery = true)
    List<ProductFeatureValue> findByFeatureIdAndNumericValueLessThan(Long featureId, Double value);

    @Query(value = "SELECT * FROM product_feature_value pfv " +
           "WHERE pfv.feature_id = ?1 " +
           "AND pfv.type = 'number' " +
           "AND CAST(pfv.attribute_values->>'value' AS DECIMAL) BETWEEN ?2 AND ?3", nativeQuery = true)
    List<ProductFeatureValue> findByFeatureIdAndNumericValueBetween(Long featureId, Double minValue, Double maxValue);

    @Query(value = "SELECT * FROM product_feature_value pfv " +
           "WHERE pfv.feature_id = ?1 " +
           "AND pfv.type = 'array' " +
           "AND pfv.attribute_values @> CAST(?2 AS jsonb)", nativeQuery = true)
    List<ProductFeatureValue> findByFeatureIdAndArrayContains(Long featureId, String arrayElement);

    @Query(value = "SELECT * FROM product_feature_value pfv " +
           "WHERE pfv.feature_id = ?1 " +
           "AND pfv.attribute_values->>'value' LIKE CONCAT('%', ?2, '%')", nativeQuery = true)
    List<ProductFeatureValue> findByFeatureIdAndValueContaining(Long featureId, String searchText);

    @Query(value = "SELECT * FROM product_feature_value pfv " +
           "WHERE pfv.feature_id = ?1 " +
           "AND pfv.attribute_values->?2 = ?3::jsonb", nativeQuery = true)
    List<ProductFeatureValue> findByFeatureIdAndNestedKeyValue(Long featureId, String key, String value);

    @Query(value = "SELECT COUNT(*) FROM product_feature_value pfv " +
           "WHERE pfv.feature_id = ?1 " +
           "AND pfv.type = ?2", nativeQuery = true)
    Long countByFeatureIdAndValueType(Long featureId, String valueType);

    @Query(value = "SELECT AVG(CAST(pfv.attribute_values->>'value' AS DECIMAL)) " +
           "FROM product_feature_value pfv " +
           "WHERE pfv.feature_id = ?1 " +
           "AND pfv.type = 'number'", nativeQuery = true)
    Double averageNumericValueByFeatureId(Long featureId);

    @Query(value = "SELECT pfv.type as value_type, COUNT(*) as count " +
           "FROM product_feature_value pfv " +
           "WHERE pfv.feature_id = ?1 " +
           "GROUP BY pfv.type", nativeQuery = true)
    List<Map<String, Object>> countValuesByType(Long featureId);

    @Query(value = "SELECT " +
           "MIN(CAST(pfv.attribute_values->>'value' AS DECIMAL)) as min_value, " +
           "MAX(CAST(pfv.attribute_values->>'value' AS DECIMAL)) as max_value, " +
           "AVG(CAST(pfv.attribute_values->>'value' AS DECIMAL)) as avg_value " +
           "FROM product_feature_value pfv " +
           "WHERE pfv.feature_id = ?1 " +
           "AND pfv.type = 'number'", nativeQuery = true)
    Map<String, Object> getNumericValueStatistics(Long featureId);

    @Query(value = "SELECT " +
           "DATE_TRUNC('month', pfv.created_at) as month, " +
           "COUNT(*) as count " +
           "FROM product_feature_value pfv " +
           "WHERE pfv.feature_id = ?1 " +
           "AND pfv.type = ?2 " +
           "GROUP BY DATE_TRUNC('month', pfv.created_at) " +
           "ORDER BY month", nativeQuery = true)
    List<Map<String, Object>> getValueTrends(Long featureId, String valueType);

    @Query(value = "SELECT " +
           "pfv.attribute_values->>'value' as value, " +
           "COUNT(*) as count " +
           "FROM product_feature_value pfv " +
           "WHERE pfv.feature_id = ?1 " +
           "GROUP BY pfv.attribute_values->>'value'", nativeQuery = true)
    List<Map<String, Object>> getValueDistribution(Long featureId);

    @Query(value = "SELECT pfv.created_at, pfv.attribute_values->>'value' as value " +
           "FROM product_feature_value pfv " +
           "WHERE pfv.feature_id = ?1 " +
           "AND pfv.product_id = ?2 " +
           "ORDER BY pfv.created_at DESC", nativeQuery = true)
    List<ProductFeatureValue> getValueHistory(Long featureId, Long productId);

    @Query(value = "SELECT * FROM product_feature_value pfv " +
           "WHERE pfv.feature_id = ?1 " +
           "AND pfv.attribute_values::text SIMILAR TO ?2", nativeQuery = true)
    List<ProductFeatureValue> searchByValuePattern(Long featureId, String pattern);

    @Query(value = "SELECT * FROM product_feature_value pfv " +
           "WHERE pfv.feature_id = ?1 " +
           "AND pfv.attribute_values->>'value' LIKE CONCAT('%', ?2, '%') " +
           "AND similarity(pfv.attribute_values->>'value', ?2) > ?3", nativeQuery = true)
    List<ProductFeatureValue> findSimilarValues(Long featureId, String value, double threshold);

    @Query(value = "SELECT pfv.validation_pattern FROM product_feature_value pfv " +
           "WHERE pfv.feature_id = ?1", nativeQuery = true)
    Map<String, Object> getValidationRules(Long featureId);

    @Query(value = "SELECT * FROM product_feature_value pfv " +
           "WHERE pfv.feature_id = ?1 " +
           "AND pfv.type = ?2", nativeQuery = true)
    Page<ProductFeatureValue> findByFeatureIdAndValueType(Long featureId, String type, Pageable pageable);

    @Query(value = "SELECT * FROM product_feature_value pfv " +
           "WHERE pfv.feature_id = ?1 " +
           "AND pfv.unit_of_measure = ?2", nativeQuery = true)
    List<ProductFeatureValue> findByFeatureIdAndUnitOfMeasure(Long featureId, String unitOfMeasure);

    @Query(value = "SELECT * FROM product_feature_value pfv " +
           "WHERE pfv.template_id = ?1", nativeQuery = true)
    List<ProductFeatureValue> findByTemplateId(Long templateId);

    @Query(value = "SELECT * FROM product_feature_value pfv " +
           "WHERE pfv.type = ?1", nativeQuery = true)
    List<ProductFeatureValue> findByFeatureType(String type);

    @Query(value = "SELECT * FROM product_feature_value pfv " +
           "WHERE pfv.validation_status = ?1", nativeQuery = true)
    List<ProductFeatureValue> findByValidationStatus(String status);

    @Query(value = "SELECT * FROM product_feature_value pfv " +
           "WHERE pfv.validation_message LIKE CONCAT('%', ?1, '%')", nativeQuery = true)
    List<ProductFeatureValue> findByValidationMessageContaining(String message);

    @Query(value = "SELECT * FROM product_feature_value pfv " +
           "WHERE pfv.unit_of_measure = ?1", nativeQuery = true)
    List<ProductFeatureValue> findByUnitOfMeasure(String unitOfMeasure);

    @Query(value = "SELECT * FROM product_feature_value pfv " +
           "WHERE pfv.feature_id = ?1", nativeQuery = true)
    List<ProductFeatureValue> findByFeature(ProductFeature feature);

    @Query(value = "SELECT * FROM product_feature_value pfv " +
           "WHERE pfv.feature_id = ?1 " +
           "AND pfv.attribute_values::text LIKE ?2", nativeQuery = true)
    List<ProductFeatureValue> findByFeatureIdAndJsonPattern(Long featureId, String jsonPattern);

    @Query(value = "SELECT * FROM product_feature_value pfv " +
           "WHERE pfv.product_id = ?1", nativeQuery = true)
    List<ProductFeatureValue> findByProductId(Long productId);

    @Query(value = "SELECT * FROM product_feature_value pfv " +
           "WHERE pfv.feature_id = ?1", nativeQuery = true)
    List<ProductFeatureValue> findByFeatureId(Long featureId, Pageable pageable);

    @Query(value = "SELECT * FROM product_feature_value pfv " +
           "WHERE pfv.status = ?1", nativeQuery = true)
    List<ProductFeatureValue> findByStatus(String status);

    @Query(value = "SELECT * FROM product_feature_value pfv " +
           "WHERE pfv.type = ?1", nativeQuery = true)
    List<ProductFeatureValue> findByType(String type);

    @Query(value = "SELECT * FROM product_feature_value pfv " +
           "WHERE pfv.feature_id = ?1", nativeQuery = true)
    List<ProductFeatureValue> findByFeatureTemplateId(Long templateId);

    void deleteByFeatureId(Long featureId);
}
