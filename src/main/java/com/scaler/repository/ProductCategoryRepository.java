package com.scaler.repository;

import com.scaler.entity.ProductCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ProductCategoryRepository extends JpaRepository<ProductCategory, UUID> {
    
    @Query("SELECT pc FROM ProductCategory pc " +
           "WHERE pc.product.id = :productId " +
           "AND pc.merchant.id = :merchantId " +
           "AND pc.effectiveFrom <= :now " +
           "AND (pc.effectiveTo IS NULL OR pc.effectiveTo > :now)")
    List<ProductCategory> findActiveByProductAndMerchant(
            @Param("productId") UUID productId,
            @Param("merchantId") UUID merchantId,
            @Param("now") LocalDateTime now);

    @Query("SELECT pc FROM ProductCategory pc " +
           "WHERE pc.category.id = :categoryId " +
           "AND pc.merchant.id = :merchantId " +
           "AND pc.effectiveFrom <= :now " +
           "AND (pc.effectiveTo IS NULL OR pc.effectiveTo > :now) " +
           "ORDER BY pc.displayOrder")
    List<ProductCategory> findActiveProductsByCategoryAndMerchant(
            @Param("categoryId") UUID categoryId,
            @Param("merchantId") UUID merchantId,
            @Param("now") LocalDateTime now);

    Optional<ProductCategory> findByProductIdAndCategoryIdAndMerchantId(
            UUID productId, UUID categoryId, UUID merchantId);

    List<ProductCategory> findByProductIdAndMerchantIdAndIsPrimaryTrue(
            UUID productId, UUID merchantId);

    @Query("SELECT pc FROM ProductCategory pc " +
           "WHERE pc.product.id = :productId " +
           "AND pc.merchant.id = :merchantId " +
           "AND pc.isPrimary = true " +
           "AND pc.effectiveFrom <= :now " +
           "AND (pc.effectiveTo IS NULL OR pc.effectiveTo > :now)")
    Optional<ProductCategory> findActivePrimaryCategory(
            @Param("productId") UUID productId,
            @Param("merchantId") UUID merchantId,
            @Param("now") LocalDateTime now);
}
