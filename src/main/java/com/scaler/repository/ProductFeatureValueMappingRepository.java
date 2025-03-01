package com.scaler.repository;

import com.scaler.entity.ProductFeatureValueMapping;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;
import java.util.UUID;

@Repository
public interface ProductFeatureValueMappingRepository extends JpaRepository<ProductFeatureValueMapping, UUID> {
    
    @Query("SELECT pfvm FROM ProductFeatureValueMapping pfvm " +
           "JOIN FETCH pfvm.feature f " +
           "JOIN FETCH pfvm.featureValue fv " +
           "WHERE pfvm.product.id = :productId " +
           "ORDER BY pfvm.displayOrder")
    List<ProductFeatureValueMapping> findByProductIdWithFeatureValues(@Param("productId") UUID productId);

    @Query("SELECT DISTINCT pfvm FROM ProductFeatureValueMapping pfvm " +
           "JOIN FETCH pfvm.feature f " +
           "JOIN FETCH pfvm.featureValue fv " +
           "WHERE pfvm.product.id = :productId " +
           "AND f.code IN :featureCodes " +
           "ORDER BY pfvm.displayOrder")
    List<ProductFeatureValueMapping> findByProductIdAndFeatureCodes(
            @Param("productId") UUID productId,
            @Param("featureCodes") Set<String> featureCodes);

    @Query("SELECT pfvm FROM ProductFeatureValueMapping pfvm " +
           "JOIN FETCH pfvm.feature f " +
           "JOIN FETCH pfvm.featureValue fv " +
           "WHERE pfvm.product.id = :productId " +
           "AND pfvm.isPrimary = true " +
           "ORDER BY pfvm.displayOrder")
    List<ProductFeatureValueMapping> findPrimaryValuesByProductId(@Param("productId") UUID productId);
}
