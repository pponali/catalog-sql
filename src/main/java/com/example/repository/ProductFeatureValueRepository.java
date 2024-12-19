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
    List<ProductFeatureValue> findByProductFeature(ProductFeature productFeature);
    
    // Find all string values for a specific product feature
    List<ProductFeatureValue> findByProductFeatureAndStringValueIsNotNull(ProductFeature productFeature);
    
    // Find all numeric values for a specific product feature
    List<ProductFeatureValue> findByProductFeatureAndNumericValueIsNotNull(ProductFeature productFeature);
    
    // Find all boolean values for a specific product feature
    List<ProductFeatureValue> findByProductFeatureAndBooleanValueIsNotNull(ProductFeature productFeature);
    
    // Find by specific string value
    List<ProductFeatureValue> findByStringValue(String value);
    
    // Find by numeric value range
    List<ProductFeatureValue> findByNumericValueBetween(Double minValue, Double maxValue);
    
    // Find by boolean value
    List<ProductFeatureValue> findByBooleanValue(Boolean value);
    
    // Find by product feature and string value
    Optional<ProductFeatureValue> findByProductFeatureAndStringValue(ProductFeature productFeature, String value);
    
    // Find by product feature and numeric value
    Optional<ProductFeatureValue> findByProductFeatureAndNumericValue(ProductFeature productFeature, Double value);
    
    // Find by product feature and boolean value
    Optional<ProductFeatureValue> findByProductFeatureAndBooleanValue(ProductFeature productFeature, Boolean value);
    
    // Custom query to find values by product code and attribute code
    @Query("SELECT pfv FROM ProductFeatureValue pfv " +
           "JOIN pfv.productFeature pf " +
           "JOIN pf.product p " +
           "JOIN pf.classificationAttribute ca " +
           "WHERE p.code = :productCode " +
           "AND ca.code = :attributeCode")
    List<ProductFeatureValue> findByProductCodeAndAttributeCode(
        @Param("productCode") String productCode,
        @Param("attributeCode") String attributeCode
    );
    
    // Count values by product feature
    long countByProductFeature(ProductFeature productFeature);
    
    // Delete all values for a specific product feature
    void deleteByProductFeature(ProductFeature productFeature);
    
    // Find all values with pagination support
    @Query("SELECT pfv FROM ProductFeatureValue pfv " +
           "JOIN pfv.productFeature pf " +
           "JOIN pf.product p " +
           "JOIN pf.classificationAttribute ca " +
           "WHERE p.code = :productCode " +
           "ORDER BY ca.code")
    List<ProductFeatureValue> findAllByProductCodeOrdered(@Param("productCode") String productCode);
    
    // Search values by string pattern
    @Query("SELECT pfv FROM ProductFeatureValue pfv " +
           "WHERE pfv.stringValue LIKE %:pattern%")
    List<ProductFeatureValue> searchByStringValuePattern(@Param("pattern") String pattern);
    
    // Find distinct string values for a specific attribute across all products
    @Query("SELECT DISTINCT pfv.stringValue FROM ProductFeatureValue pfv " +
           "JOIN pfv.productFeature pf " +
           "JOIN pf.classificationAttribute ca " +
           "WHERE ca.code = :attributeCode " +
           "AND pfv.stringValue IS NOT NULL")
    List<String> findDistinctStringValuesByAttributeCode(@Param("attributeCode") String attributeCode);
    
    // Find average numeric value for a specific attribute
    @Query("SELECT AVG(pfv.numericValue) FROM ProductFeatureValue pfv " +
           "JOIN pfv.productFeature pf " +
           "JOIN pf.classificationAttribute ca " +
           "WHERE ca.code = :attributeCode " +
           "AND pfv.numericValue IS NOT NULL")
    Double findAverageNumericValueByAttributeCode(@Param("attributeCode") String attributeCode);
    
    // Find min and max numeric values for a specific attribute
    @Query("SELECT MIN(pfv.numericValue), MAX(pfv.numericValue) FROM ProductFeatureValue pfv " +
           "JOIN pfv.productFeature pf " +
           "JOIN pf.classificationAttribute ca " +
           "WHERE ca.code = :attributeCode " +
           "AND pfv.numericValue IS NOT NULL")
    Object[] findMinMaxNumericValuesByAttributeCode(@Param("attributeCode") String attributeCode);
}
