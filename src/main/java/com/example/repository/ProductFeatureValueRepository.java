package com.example.repository;

import com.example.entity.ProductFeature;
import com.example.entity.ProductFeatureValue;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductFeatureValueRepository extends JpaRepository<ProductFeatureValue, Long> {
    
    // Find all values for a specific product feature
    List<ProductFeatureValue> findByFeature(ProductFeature feature);
    
    // Find all string values for a specific product feature
    List<ProductFeatureValue> findByFeatureAndStringValueIsNotNull(ProductFeature feature);
    
    // Find all numeric values for a specific product feature
    List<ProductFeatureValue> findByFeatureAndNumericValueIsNotNull(ProductFeature feature);
    
    // Find all boolean values for a specific product feature
    List<ProductFeatureValue> findByFeatureAndBooleanValueIsNotNull(ProductFeature feature);
    
    // Find by specific string value
    List<ProductFeatureValue> findByStringValue(String value);
    
    // Find by numeric value
    List<ProductFeatureValue> findByNumericValue(Double value);
    
    // Find by boolean value
    List<ProductFeatureValue> findByBooleanValue(Boolean value);
    
    // Find by feature and string value
    Optional<ProductFeatureValue> findByFeatureAndStringValue(ProductFeature feature, String value);
    
    // Find by feature and numeric value
    Optional<ProductFeatureValue> findByFeatureAndNumericValue(ProductFeature feature, Double value);
    
    // Find by feature and boolean value
    Optional<ProductFeatureValue> findByFeatureAndBooleanValue(ProductFeature feature, Boolean value);
    
    // Custom query to find values by product SKU and template code
    @Query("SELECT pfv FROM ProductFeatureValue pfv " +
           "JOIN pfv.feature pf " +
           "JOIN pf.product p " +
           "JOIN pf.template t " +
           "WHERE p.sku = :productSku " +
           "AND t.code = :templateCode")
    List<ProductFeatureValue> findByProductSkuAndTemplateCode(
        @Param("productSku") String productSku,
        @Param("templateCode") String templateCode
    );
    
    // Count values by feature
    long countByFeature(ProductFeature feature);
    
    // Delete all values for a specific feature
    void deleteByFeature(ProductFeature feature);
    
    // Find all values with pagination support
    @Query("SELECT pfv FROM ProductFeatureValue pfv " +
           "JOIN pfv.feature pf " +
           "JOIN pf.product p " +
           "JOIN pf.template t " +
           "WHERE p.sku = :productSku " +
           "AND t.code = :templateCode " +
           "ORDER BY pfv.id")
    List<ProductFeatureValue> findByProductSkuAndTemplateCodePaged(
        @Param("productSku") String productSku,
        @Param("templateCode") String templateCode
    );
    
    // Find distinct string values for a specific template across all products
    @Query("SELECT DISTINCT pfv.stringValue FROM ProductFeatureValue pfv " +
           "JOIN pfv.feature pf " +
           "JOIN pf.template t " +
           "WHERE t.code = :templateCode " +
           "AND pfv.stringValue IS NOT NULL")
    List<String> findDistinctStringValuesByTemplateCode(@Param("templateCode") String templateCode);
    
    // Find average numeric value for a specific template
    @Query("SELECT AVG(pfv.numericValue) FROM ProductFeatureValue pfv " +
           "JOIN pfv.feature pf " +
           "JOIN pf.template t " +
           "WHERE t.code = :templateCode " +
           "AND pfv.numericValue IS NOT NULL")
    Double findAverageNumericValueByTemplateCode(@Param("templateCode") String templateCode);
    
    // Find min and max numeric values for a specific template
    @Query("SELECT MIN(pfv.numericValue), MAX(pfv.numericValue) FROM ProductFeatureValue pfv " +
           "JOIN pfv.feature pf " +
           "JOIN pf.template t " +
           "WHERE t.code = :templateCode " +
           "AND pfv.numericValue IS NOT NULL")
    Object[] findMinMaxNumericValuesByTemplateCode(@Param("templateCode") String templateCode);
}
