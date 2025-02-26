package com.scaler.repository;

import com.scaler.entity.ProductFeature;
import com.scaler.entity.ProductFeatureValue;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ProductFeatureValueRepository extends JpaRepository<ProductFeatureValue, UUID> {
    
    @Query(value = "SELECT * FROM product_feature_value pfv " +
           "WHERE pfv.feature_mapping_id IN (SELECT id FROM product_feature_mapping WHERE feature_id = ?1)", nativeQuery = true)
    List<ProductFeatureValue> findByFeatureId(UUID featureId);

    @Query(value = "SELECT * FROM product_feature_value pfv " +
           "WHERE pfv.feature_mapping_id IN (SELECT id FROM product_feature_mapping WHERE feature_id = ?1) " +
           "AND pfv.id = ?2", nativeQuery = true)
    Optional<ProductFeatureValue> findByFeatureIdAndId(UUID featureId, UUID id);

    @Query(value = "SELECT * FROM product_feature_value pfv " +
           "WHERE pfv.feature_mapping_id IN (SELECT id FROM product_feature_mapping WHERE feature_id = ?1) " +
           "AND pfv.attribute_values IS NOT NULL", nativeQuery = true)
    List<ProductFeatureValue> findByFeatureIdAndValueIsNotNull(UUID featureId);

    @Query(value = "SELECT * FROM product_feature_value pfv " +
           "WHERE pfv.feature_mapping_id IN (SELECT id FROM product_feature_mapping WHERE feature_id = ?1) " +
           "AND pfv.type = ?2", nativeQuery = true)
    List<ProductFeatureValue> findByFeatureIdAndValueType(UUID featureId, String type);

    @Query(value = "SELECT * FROM product_feature_value pfv " +
           "WHERE pfv.feature_mapping_id IN (SELECT id FROM product_feature_mapping WHERE feature_id = ?1) " +
           "AND pfv.attribute_values->>'value' = ?2", nativeQuery = true)
    List<ProductFeatureValue> findByFeatureIdAndValue(UUID featureId, String value);

    @Query(value = "SELECT pfv.* FROM product_feature_value pfv " +
           "JOIN product_feature_mapping pfm ON pfv.feature_mapping_id = pfm.id " +
           "WHERE pfm.product_id = ?1 " +
           "AND pfm.feature_id IN (SELECT f.id FROM product_feature f WHERE f.template_id = ?2)", nativeQuery = true)
    List<ProductFeatureValue> findByProductSkuAndTemplateCode(String productSku, String templateCode);

    @Query(value = "SELECT pfv.* FROM product_feature_value pfv " +
           "JOIN product_feature_mapping pfm ON pfv.feature_mapping_id = pfm.id " +
           "WHERE pfm.feature_id = ?1 " +
           "AND pfv.unit_of_measure = ?2", nativeQuery = true)
    List<ProductFeatureValue> findByFeatureIdAndUnitOfMeasureCode(UUID featureId, String unitOfMeasureCode);

    @Query(value = "SELECT pfv.* FROM product_feature_value pfv " +
           "JOIN product_feature_mapping pfm ON pfv.feature_mapping_id = pfm.id " +
           "WHERE pfm.feature_id = ?1 " +
           "AND pfv.type = 'number' " +
           "AND CAST(pfv.attribute_values->>'value' AS DECIMAL) > ?2", nativeQuery = true)
    List<ProductFeatureValue> findByFeatureIdAndNumericValueGreaterThan(UUID featureId, Double value);

    @Query(value = "SELECT pfv.* FROM product_feature_value pfv " +
           "JOIN product_feature_mapping pfm ON pfv.feature_mapping_id = pfm.id " +
           "WHERE pfm.feature_id = ?1 " +
           "AND pfv.type = 'number' " +
           "AND CAST(pfv.attribute_values->>'value' AS DECIMAL) < ?2", nativeQuery = true)
    List<ProductFeatureValue> findByFeatureIdAndNumericValueLessThan(UUID featureId, Double value);

    @Query(value = "SELECT pfv.* FROM product_feature_value pfv " +
           "JOIN product_feature_mapping pfm ON pfv.feature_mapping_id = pfm.id " +
           "WHERE pfm.feature_id = ?1 " +
           "AND pfv.type = 'number' " +
           "AND CAST(pfv.attribute_values->>'value' AS DECIMAL) BETWEEN ?2 AND ?3", nativeQuery = true)
    List<ProductFeatureValue> findByFeatureIdAndNumericValueBetween(UUID featureId, Double minValue, Double maxValue);

    @Query(value = "SELECT pfv.* FROM product_feature_value pfv " +
           "JOIN product_feature_mapping pfm ON pfv.feature_mapping_id = pfm.id " +
           "WHERE pfm.feature_id = ?1 " +
           "AND pfv.type = 'array' " +
           "AND pfv.attribute_values @> CAST(?2 AS jsonb)", nativeQuery = true)
    List<ProductFeatureValue> findByFeatureIdAndArrayContains(UUID featureId, String arrayElement);

    @Query(value = "SELECT pfv.* FROM product_feature_value pfv " +
           "JOIN product_feature_mapping pfm ON pfv.feature_mapping_id = pfm.id " +
           "WHERE pfm.feature_id = ?1 " +
           "AND pfv.attribute_values->>'value' LIKE CONCAT('%', ?2, '%')", nativeQuery = true)
    List<ProductFeatureValue> findByFeatureIdAndValueContaining(UUID featureId, String searchText);

    @Query(value = "SELECT pfv.* FROM product_feature_value pfv " +
           "JOIN product_feature_mapping pfm ON pfv.feature_mapping_id = pfm.id " +
           "WHERE pfm.feature_id = ?1 " +
           "AND pfv.attribute_values->?2 = ?3::jsonb", nativeQuery = true)
    List<ProductFeatureValue> findByFeatureIdAndNestedKeyValue(UUID featureId, String key, String value);

    @Query(value = "SELECT COUNT(*) FROM product_feature_value pfv " +
           "JOIN product_feature_mapping pfm ON pfv.feature_mapping_id = pfm.id " +
           "WHERE pfm.feature_id = ?1 " +
           "AND pfv.type = ?2", nativeQuery = true)
    Long countByFeatureIdAndValueType(UUID featureId, String valueType);

    @Query(value = "SELECT AVG(CAST(pfv.attribute_values->>'value' AS DECIMAL)) " +
           "FROM product_feature_value pfv " +
           "JOIN product_feature_mapping pfm ON pfv.feature_mapping_id = pfm.id " +
           "WHERE pfm.feature_id = ?1 " +
           "AND pfv.type = 'number'", nativeQuery = true)
    Double averageNumericValueByFeatureId(UUID featureId);

    @Query(value = "SELECT pfv.type as value_type, COUNT(*) as count " +
           "FROM product_feature_value pfv " +
           "JOIN product_feature_mapping pfm ON pfv.feature_mapping_id = pfm.id " +
           "WHERE pfm.feature_id = ?1 " +
           "GROUP BY pfv.type", nativeQuery = true)
    List<Map<String, Object>> countValuesByType(UUID featureId);

    @Query(value = "SELECT " +
           "MIN(CAST(pfv.attribute_values->>'value' AS DECIMAL)) as min_value, " +
           "MAX(CAST(pfv.attribute_values->>'value' AS DECIMAL)) as max_value, " +
           "AVG(CAST(pfv.attribute_values->>'value' AS DECIMAL)) as avg_value " +
           "FROM product_feature_value pfv " +
           "JOIN product_feature_mapping pfm ON pfv.feature_mapping_id = pfm.id " +
           "WHERE pfm.feature_id = ?1 " +
           "AND pfv.type = 'number'", nativeQuery = true)
    Map<String, Object> getNumericValueStatistics(UUID featureId);

    @Query(value = "SELECT " +
           "DATE_TRUNC('month', pfv.created_at) as month, " +
           "COUNT(*) as count " +
           "FROM product_feature_value pfv " +
           "JOIN product_feature_mapping pfm ON pfv.feature_mapping_id = pfm.id " +
           "WHERE pfm.feature_id = ?1 " +
           "AND pfv.type = ?2 " +
           "GROUP BY DATE_TRUNC('month', pfv.created_at) " +
           "ORDER BY month", nativeQuery = true)
    List<Map<String, Object>> getValueTrends(UUID featureId, String valueType);

    @Query(value = "SELECT " +
           "pfv.attribute_values->>'value' as value, " +
           "COUNT(*) as count " +
           "FROM product_feature_value pfv " +
           "JOIN product_feature_mapping pfm ON pfv.feature_mapping_id = pfm.id " +
           "WHERE pfm.feature_id = ?1 " +
           "GROUP BY pfv.attribute_values->>'value'", nativeQuery = true)
    List<Map<String, Object>> getValueDistribution(UUID featureId);

    @Query(value = "SELECT pfv.created_at, pfv.attribute_values->>'value' as value " +
           "FROM product_feature_value pfv " +
           "JOIN product_feature_mapping pfm ON pfv.feature_mapping_id = pfm.id " +
           "WHERE pfm.feature_id = ?1 " +
           "AND pfm.product_id = ?2 " +
           "ORDER BY pfv.created_at DESC", nativeQuery = true)
    List<ProductFeatureValue> getValueHistory(UUID featureId, UUID productId);

    @Query(value = "SELECT pfv.* FROM product_feature_value pfv " +
           "JOIN product_feature_mapping pfm ON pfv.feature_mapping_id = pfm.id " +
           "WHERE pfm.feature_id = ?1 " +
           "AND pfv.attribute_values::text SIMILAR TO ?2", nativeQuery = true)
    List<ProductFeatureValue> searchByValuePattern(UUID featureId, String pattern);

    @Query(value = "SELECT pfv.* FROM product_feature_value pfv " +
           "JOIN product_feature_mapping pfm ON pfv.feature_mapping_id = pfm.id " +
           "WHERE pfm.feature_id = ?1 " +
           "AND pfv.attribute_values->>'value' LIKE CONCAT('%', ?2, '%') " +
           "AND similarity(pfv.attribute_values->>'value', ?2) > ?3", nativeQuery = true)
    List<ProductFeatureValue> findSimilarValues(UUID featureId, String value, double threshold);

    @Query(value = "SELECT pfv.validation_pattern FROM product_feature_value pfv " +
           "JOIN product_feature_mapping pfm ON pfv.feature_mapping_id = pfm.id " +
           "WHERE pfm.feature_id = ?1", nativeQuery = true)
    Map<String, Object> getValidationRules(UUID featureId);

    @Query(value = "SELECT pfv.* FROM product_feature_value pfv " +
           "JOIN product_feature_mapping pfm ON pfv.feature_mapping_id = pfm.id " +
           "WHERE pfm.feature_id = ?1 " +
           "AND pfv.type = ?2", nativeQuery = true)
    Page<ProductFeatureValue> findByFeatureIdAndValueType(UUID featureId, String type, Pageable pageable);

    @Query(value = "SELECT pfv.* FROM product_feature_value pfv " +
           "JOIN product_feature_mapping pfm ON pfv.feature_mapping_id = pfm.id " +
           "WHERE pfm.feature_id = ?1 " +
           "AND pfv.unit_of_measure = ?2", nativeQuery = true)
    List<ProductFeatureValue> findByFeatureIdAndUnitOfMeasure(UUID featureId, String unitOfMeasure);

    @Query(value = "SELECT pfv.* FROM product_feature_value pfv " +
           "JOIN product_feature_mapping pfm ON pfv.feature_mapping_id = pfm.id " +
           "JOIN product_feature pf ON pfm.feature_id = pf.id " +
           "WHERE pf.template_id = ?1", nativeQuery = true)
    List<ProductFeatureValue> findByTemplateId(UUID templateId);

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

    @Query(value = "SELECT pfv.* FROM product_feature_value pfv " +
           "JOIN product_feature_mapping pfm ON pfv.feature_mapping_id = pfm.id " +
           "WHERE pfm.feature_id = ?1", nativeQuery = true)
    List<ProductFeatureValue> findByFeature(ProductFeature feature);

    @Query(value = "SELECT pfv.* FROM product_feature_value pfv " +
           "JOIN product_feature_mapping pfm ON pfv.feature_mapping_id = pfm.id " +
           "WHERE pfm.feature_id = ?1 " +
           "AND pfv.attribute_values::text LIKE ?2", nativeQuery = true)
    List<ProductFeatureValue> findByFeatureIdAndJsonPattern(UUID featureId, String jsonPattern);

    @Query(value = "SELECT * FROM product_feature_value pfv " +
           "WHERE pfv.product_id = ?1", nativeQuery = true)
    List<ProductFeatureValue> findByProductId(UUID productId);

    @Query(value = "SELECT * FROM product_feature_value pfv " +
           "WHERE pfv.feature_id = ?1", nativeQuery = true)
    Page<ProductFeatureValue> findByFeatureId(UUID featureId, Pageable pageable);

    @Query(value = "SELECT * FROM product_feature_value pfv " +
           "WHERE pfv.status = ?1", nativeQuery = true)
    List<ProductFeatureValue> findByStatus(String status);

    @Query(value = "SELECT * FROM product_feature_value pfv " +
           "WHERE pfv.type = ?1", nativeQuery = true)
    List<ProductFeatureValue> findByType(String type);

    @Query(value = "SELECT pfv.* FROM product_feature_value pfv " +
           "JOIN product_feature_mapping pfm ON pfv.feature_mapping_id = pfm.id " +
           "WHERE pfm.feature_id = ?1", nativeQuery = true)
    List<ProductFeatureValue> findByFeatureTemplateId(UUID templateId);

    @Query("DELETE FROM ProductFeatureValue pfv WHERE pfv.featureMapping.feature.id = :featureId")
    void deleteByFeatureMappingFeatureId(@Param("featureId") UUID featureId);

    @Query("SELECT pfv FROM ProductFeatureValue pfv JOIN pfv.featureMapping fm WHERE fm.feature.id = :featureId AND pfv.attributeValues IS NOT NULL")
    List<ProductFeatureValue> findByFeatureMappingFeatureIdAndAttributeValuesIsNotNull(@Param("featureId") UUID featureId);

    @Query(value = "SELECT pfv.* FROM product_feature_value pfv " +
           "JOIN product_feature_mapping pfm ON pfv.feature_mapping_id = pfm.id " +
           "WHERE pfm.feature_id = ?1 " +
           "ORDER BY pfv.created_date DESC", nativeQuery = true)
    List<ProductFeatureValue> findByFeatureIdOrderByCreatedDateDesc(UUID featureId);

}
