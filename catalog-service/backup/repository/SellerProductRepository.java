package com.scaler.repository;

import com.scaler.entity.SellerProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface SellerProductRepository extends JpaRepository<SellerProduct, UUID> {
    
    // Find all sellers for a product in a specific merchant
    @Query("SELECT sp FROM SellerProduct sp WHERE sp.product.id = :productId AND sp.merchant.id = :merchantId")
    List<SellerProduct> findByProductAndMerchant(@Param("productId") UUID productId, @Param("merchantId") UUID merchantId);
    
    // Find manufacturer for a product
    @Query("SELECT sp FROM SellerProduct sp WHERE sp.product.id = :productId AND sp.isManufacturer = true")
    Optional<SellerProduct> findManufacturerForProduct(@Param("productId") UUID productId);
    
    // Find all products by seller across all merchants
    @Query("SELECT sp FROM SellerProduct sp WHERE sp.seller.id = :sellerId ORDER BY sp.createdDate DESC")
    List<SellerProduct> findBySellerIdOrderByCreatedDateDesc(@Param("sellerId") UUID sellerId);
    
    // Find all products by seller for a specific merchant
    @Query("SELECT sp FROM SellerProduct sp WHERE sp.seller.id = :sellerId AND sp.merchant.id = :merchantId ORDER BY sp.createdDate DESC")
    List<SellerProduct> findBySellerIdAndMerchantIdOrderByCreatedDateDesc(@Param("sellerId") UUID sellerId, @Param("merchantId") UUID merchantId);
    
    // Find products by price range for a merchant
    @Query("SELECT sp FROM SellerProduct sp WHERE sp.merchant.id = :merchantId AND sp.price BETWEEN :minPrice AND :maxPrice")
    List<SellerProduct> findByPriceRange(
        @Param("merchantId") UUID merchantId,
        @Param("minPrice") BigDecimal minPrice,
        @Param("maxPrice") BigDecimal maxPrice
    );
    
    // Find products with low stock for a merchant
    @Query("SELECT sp FROM SellerProduct sp WHERE sp.merchant.id = :merchantId AND sp.stock <= :threshold")
    List<SellerProduct> findLowStockProducts(
        @Param("merchantId") UUID merchantId,
        @Param("threshold") Integer threshold
    );
    
    // Find unique product by seller and merchant
    Optional<SellerProduct> findByProductIdAndSellerIdAndMerchantId(
        UUID productId,
        UUID sellerId,
        UUID merchantId
    );
    
    // Find all active products for a merchant
    @Query("SELECT sp FROM SellerProduct sp WHERE sp.merchant.id = :merchantId AND sp.status = 'ACTIVE'")
    List<SellerProduct> findActiveProductsForMerchant(@Param("merchantId") UUID merchantId);
    
    // Count products by seller and merchant
    Long countBySellerIdAndMerchantId(UUID sellerId, UUID merchantId);
    
    // Check if seller is manufacturer for product
    Boolean existsByProductIdAndSellerIdAndIsManufacturerTrue(UUID productId, UUID sellerId);
}
