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

@Repository
public interface ProductFeatureValueRepository extends JpaRepository<ProductFeatureValue, Long> {
    
    // Basic feature queries
    List<ProductFeatureValue> findByFeature(ProductFeature feature);
    Optional<ProductFeatureValue> findByFeatureAndId(ProductFeature feature, Long id);
    
    // JSON value queries
    @Query(value = "SELECT pfv FROM ProductFeatureValue pfv WHERE pfv.feature = :feature AND pfv.attributeValue IS NOT NULL")
    List<ProductFeatureValue> findByFeatureWithValues(@Param("feature") ProductFeature feature);
    
    @Query(value = "SELECT pfv FROM ProductFeatureValue pfv WHERE pfv.feature = :feature AND pfv.attributeValue->>'type' = :type")
    List<ProductFeatureValue> findByFeatureAndValueType(@Param("feature") ProductFeature feature, @Param("type") String type);
    
    @Query(value = "SELECT pfv FROM ProductFeatureValue pfv WHERE pfv.feature = :feature AND pfv.attributeValue->>'value' = :value")
    List<ProductFeatureValue> findByFeatureAndValue(@Param("feature") ProductFeature feature, @Param("value") String value);
    
    // Numeric value comparisons
    @Query(value = "SELECT pfv FROM ProductFeatureValue pfv WHERE pfv.feature = :feature " +
           "AND (pfv.attributeValue->>'type' = 'number') " +
           "AND CAST(pfv.attributeValue->>'value' AS DECIMAL) > :value")
    List<ProductFeatureValue> findByFeatureAndNumericValueGreaterThan(
            @Param("feature") ProductFeature feature, 
            @Param("value") Double value);
    
    @Query(value = "SELECT pfv FROM ProductFeatureValue pfv WHERE pfv.feature = :feature " +
           "AND (pfv.attributeValue->>'type' = 'number') " +
           "AND CAST(pfv.attributeValue->>'value' AS DECIMAL) < :value")
    List<ProductFeatureValue> findByFeatureAndNumericValueLessThan(
            @Param("feature") ProductFeature feature, 
            @Param("value") Double value);
    
    @Query(value = "SELECT pfv FROM ProductFeatureValue pfv WHERE pfv.feature = :feature " +
           "AND (pfv.attributeValue->>'type' = 'number') " +
           "AND CAST(pfv.attributeValue->>'value' AS DECIMAL) BETWEEN :minValue AND :maxValue")
    List<ProductFeatureValue> findByFeatureAndNumericValueBetween(
            @Param("feature") ProductFeature feature, 
            @Param("minValue") Double minValue,
            @Param("maxValue") Double maxValue);
    
    // Array value queries
    @Query(value = "SELECT pfv FROM ProductFeatureValue pfv WHERE pfv.feature = :feature " +
           "AND (pfv.attributeValue->>'type' = 'array') " +
           "AND pfv.attributeValue->'value' ? :arrayElement")
    List<ProductFeatureValue> findByFeatureAndArrayContains(
            @Param("feature") ProductFeature feature,
            @Param("arrayElement") String arrayElement);
    
    // Product and template queries
    @Query("SELECT pfv FROM ProductFeatureValue pfv " +
           "JOIN pfv.feature pf " +
           "JOIN pf.product p " +
           "JOIN pf.template t " +
           "WHERE p.sku = :productSku " +
           "AND t.code = :templateCode")
    List<ProductFeatureValue> findByProductSkuAndTemplateCode(
            @Param("productSku") String productSku,
            @Param("templateCode") String templateCode);
    

    List<ProductFeatureValue> findByFeatureAndUnitOfMeasureCode(ProductFeature feature, String unitCode);
    
    // Advanced JSON queries
    @Query(value = "SELECT pfv FROM ProductFeatureValue pfv WHERE pfv.feature = :feature " +
           "AND pfv.attributeValue @> :jsonPattern")
    List<ProductFeatureValue> findByFeatureAndJsonPattern(
            @Param("feature") ProductFeature feature,
            @Param("jsonPattern") String jsonPattern);
    
    // Text search in JSON values
    @Query(value = "SELECT pfv FROM ProductFeatureValue pfv WHERE pfv.feature = :feature " +
           "AND pfv.attributeValue->>'value' ILIKE %:searchText%")
    List<ProductFeatureValue> findByFeatureAndValueContaining(
            @Param("feature") ProductFeature feature,
            @Param("searchText") String searchText);
    
    // Aggregation queries
    @Query(value = "SELECT COUNT(pfv) FROM ProductFeatureValue pfv WHERE pfv.feature = :feature " +
           "AND (pfv.attributeValue->>'type' = :type)")
    Long countByFeatureAndValueType(
            @Param("feature") ProductFeature feature,
            @Param("type") String type);
    
    @Query(value = "SELECT AVG(CAST(pfv.attributeValue->>'value' AS DECIMAL)) FROM ProductFeatureValue pfv " +
           "WHERE pfv.feature = :feature AND (pfv.attributeValue->>'type' = 'number')")
    Double averageNumericValueByFeature(@Param("feature") ProductFeature feature);
    
    // Pagination support
    @Query(value = "SELECT pfv FROM ProductFeatureValue pfv WHERE pfv.feature = :feature " +
           "AND pfv.attributeValue->>'type' = :type",
           countQuery = "SELECT COUNT(pfv) FROM ProductFeatureValue pfv WHERE pfv.feature = :feature " +
           "AND pfv.attributeValue->>'type' = :type")
    Page<ProductFeatureValue> findByFeatureAndValueTypePaged(
            @Param("feature") ProductFeature feature,
            @Param("type") String type,
            Pageable pageable);
    
    // Complex JSON structure queries
    @Query(value = "SELECT pfv FROM ProductFeatureValue pfv WHERE pfv.feature = :feature " +
           "AND pfv.attributeValue->'value'->>'key' = :key " +
           "AND pfv.attributeValue->'value'->>'value' = :value")
    List<ProductFeatureValue> findByFeatureAndNestedKeyValue(
            @Param("feature") ProductFeature feature,
            @Param("key") String key,
            @Param("value") String value);

    Page<ProductFeatureValue> findByFeatureId(Long featureId, Pageable pageable);
    void deleteByFeatureId(Long featureId);
    
    @Query("SELECT v FROM ProductFeatureValue v WHERE v.feature.template.id = :templateId")
    List<ProductFeatureValue> findByFeatureTemplateId(Long templateId);
    
    @Query("SELECT v FROM ProductFeatureValue v WHERE v.feature.product.id = :productId")
    List<ProductFeatureValue> findByProductId(Long productId);
    
    @Query("SELECT v FROM ProductFeatureValue v WHERE v.attributeValue->>'status' = :status")
    List<ProductFeatureValue> findByStatus(String status);

    
    @Query("SELECT v FROM ProductFeatureValue v WHERE v.attributeValue->>'type' = :type")
    List<ProductFeatureValue> findByType(String type);

    // ID-based queries
    @Query("SELECT pfv FROM ProductFeatureValue pfv WHERE pfv.feature.id = :featureId AND pfv.id = :id")
    Optional<ProductFeatureValue> findByFeatureIdAndId(@Param("featureId") Long featureId, @Param("id") Long id);

    @Query("SELECT pfv FROM ProductFeatureValue pfv WHERE pfv.feature.id = :featureId AND pfv.attributeValue IS NOT NULL")
    List<ProductFeatureValue> findByFeatureIdAndValueIsNotNull(@Param("featureId") Long featureId);

    @Query("SELECT pfv FROM ProductFeatureValue pfv WHERE pfv.feature.id = :featureId AND pfv.attributeValue->>'type' = :type")
    List<ProductFeatureValue> findByFeatureIdAndValueType(@Param("featureId") Long featureId, @Param("type") String type);

    @Query("SELECT pfv FROM ProductFeatureValue pfv WHERE pfv.feature.id = :featureId AND pfv.attributeValue->>'type' = :type")
    Page<ProductFeatureValue> findByFeatureIdAndValueType(@Param("featureId") Long featureId, @Param("type") String type, Pageable pageable);

    @Query("SELECT pfv FROM ProductFeatureValue pfv WHERE pfv.feature.id = :featureId AND pfv.attributeValue->>'value' = :value")
    List<ProductFeatureValue> findByFeatureIdAndValue(@Param("featureId") Long featureId, @Param("value") String value);

    @Query("SELECT pfv FROM ProductFeatureValue pfv WHERE pfv.feature.id = :featureId " +
           "AND (pfv.attributeValue->>'type' = 'number') " +
           "AND CAST(pfv.attributeValue->>'value' AS DECIMAL) > :value")
    List<ProductFeatureValue> findByFeatureIdAndNumericValueGreaterThan(
            @Param("featureId") Long featureId, 
            @Param("value") Double value);

    @Query("SELECT pfv FROM ProductFeatureValue pfv WHERE pfv.feature.id = :featureId " +
           "AND (pfv.attributeValue->>'type' = 'number') " +
           "AND CAST(pfv.attributeValue->>'value' AS DECIMAL) < :value")
    List<ProductFeatureValue> findByFeatureIdAndNumericValueLessThan(
            @Param("featureId") Long featureId, 
            @Param("value") Double value);

    @Query("SELECT pfv FROM ProductFeatureValue pfv WHERE pfv.feature.id = :featureId " +
           "AND (pfv.attributeValue->>'type' = 'number') " +
           "AND CAST(pfv.attributeValue->>'value' AS DECIMAL) BETWEEN :minValue AND :maxValue")
    List<ProductFeatureValue> findByFeatureIdAndNumericValueBetween(
            @Param("featureId") Long featureId, 
            @Param("minValue") Double minValue,
            @Param("maxValue") Double maxValue);

    @Query("SELECT pfv FROM ProductFeatureValue pfv WHERE pfv.feature.id = :featureId " +
           "AND (pfv.attributeValue->>'type' = 'array') " +
           "AND pfv.attributeValue->'value' ? :arrayElement")
    List<ProductFeatureValue> findByFeatureIdAndArrayContains(
            @Param("featureId") Long featureId,
            @Param("arrayElement") String arrayElement);

    @Query("SELECT pfv FROM ProductFeatureValue pfv WHERE pfv.feature.id = :featureId " +
           "AND pfv.attributeValue @> :jsonPattern")
    List<ProductFeatureValue> findByFeatureIdAndJsonPattern(
            @Param("featureId") Long featureId,
            @Param("jsonPattern") String jsonPattern);

    @Query("SELECT pfv FROM ProductFeatureValue pfv WHERE pfv.feature.id = :featureId " +
           "AND pfv.attributeValue->>'value' ILIKE %:searchText%")
    List<ProductFeatureValue> findByFeatureIdAndValueContaining(
            @Param("featureId") Long featureId,
            @Param("searchText") String searchText);

    @Query("SELECT pfv FROM ProductFeatureValue pfv WHERE pfv.feature.id = :featureId " +
           "AND pfv.attributeValue->>:key = :value")
    List<ProductFeatureValue> findByFeatureIdAndNestedKeyValue(
            @Param("featureId") Long featureId,
            @Param("key") String key,
            @Param("value") String value);

    @Query("SELECT v FROM ProductFeatureValue v WHERE v.feature.featureType = :type")
    List<ProductFeatureValue> findByFeatureType(@Param("type") String type);
    
    @Query("SELECT v FROM ProductFeatureValue v WHERE v.validationStatus = :status")
    List<ProductFeatureValue> findByValidationStatus(@Param("status") String status);
    
    @Query("SELECT v FROM ProductFeatureValue v WHERE v.validationMessage LIKE %:message%")
    List<ProductFeatureValue> findByValidationMessageContaining(@Param("message") String message);
    
    @Query("SELECT v FROM ProductFeatureValue v WHERE v.feature.unit.code = :unitCode")
    List<ProductFeatureValue> findByUnitOfMeasureCode(@Param("unitCode") String unitCode);
    
    @Query("SELECT v FROM ProductFeatureValue v WHERE v.feature.id = :featureId")
    List<ProductFeatureValue> findByFeatureId(@Param("featureId") Long featureId);

    List<ProductFeatureValue> findByFeatureIdAndUnitOfMeasureCode(Long featureId, String unitOfMeasureCode);

    List<ProductFeatureValue> findByTemplateId(Long templateId);

    @Query("SELECT COUNT(pf) FROM ProductFeatureValue pf WHERE pf.feature.id = :featureId AND pf.type = :valueType")
    Long countByFeatureIdAndValueType(@Param("featureId") Long featureId, @Param("valueType") String valueType);

    @Query("SELECT AVG(pf.numericValue) FROM ProductFeatureValue pf WHERE pf.feature.id = :featureId")
    Double averageNumericValueByFeatureId(@Param("featureId") Long featureId);

    @Query("SELECT pf.type, COUNT(pf) FROM ProductFeatureValue pf WHERE pf.feature.id = :featureId GROUP BY pf.type")
    Map<String, Long> countValuesByType(@Param("featureId") Long featureId);

    @Query("SELECT MIN(pf.numericValue) as min, MAX(pf.numericValue) as max, AVG(pf.numericValue) as avg, " +
           "COUNT(pf.numericValue) as count FROM ProductFeatureValue pf WHERE pf.feature.id = :featureId")
    Map<String, Double> getNumericValueStatistics(@Param("featureId") Long featureId);

    @Query("SELECT pf FROM ProductFeatureValue pf WHERE pf.feature.id = :featureId " +
           "AND pf.type = :valueType ORDER BY pf.createdAt")
    List<ProductFeatureValue> getValueTrends(@Param("featureId") Long featureId, @Param("valueType") String valueType);

    @Query("SELECT pf.stringValue, COUNT(pf) FROM ProductFeatureValue pf " +
           "WHERE pf.feature.id = :featureId GROUP BY pf.stringValue")
    Map<String, Long> getValueDistribution(@Param("featureId") Long featureId);

    @Query("SELECT pf FROM ProductFeatureValue pf WHERE pf.feature.id = :featureId " +
           "AND pf.product.id = :productId ORDER BY pf.createdAt")
    List<ProductFeatureValue> getValueHistory(@Param("featureId") Long featureId, @Param("productId") Long productId);

    @Query("SELECT pf FROM ProductFeatureValue pf WHERE pf.feature.id = :featureId " +
           "AND pf.stringValue LIKE %:pattern%")
    List<ProductFeatureValue> searchByValuePattern(@Param("featureId") Long featureId, @Param("pattern") String pattern);

    @Query(value = "SELECT * FROM product_feature_value pf WHERE pf.feature_id = :featureId " +
                   "AND similarity(pf.string_value, :value) > :threshold", nativeQuery = true)
    List<ProductFeatureValue> findSimilarValues(@Param("featureId") Long featureId, 
                                              @Param("value") String value,
                                              @Param("threshold") double threshold);

    @Query("SELECT DISTINCT pf.validationPattern FROM ProductFeatureValue pf " +
           "WHERE pf.feature.id = :featureId AND pf.validationPattern IS NOT NULL")
    List<String> getValidationRules(@Param("featureId") Long featureId);
}
