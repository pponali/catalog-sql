package com.scaler.repository;

import com.scaler.entity.Product;
import com.scaler.entity.SellerProduct;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.nio.channels.FileChannel;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface SellerProductRepository extends JpaRepository<SellerProduct, UUID> {
    
    List<SellerProduct> findByProductId(UUID productId);
    
    List<SellerProduct> findBySellerId(UUID sellerId);
    
    List<SellerProduct> findByMerchantId(UUID merchantId);
    
    Optional<SellerProduct> findByProductIdAndSellerId(UUID productId, UUID sellerId);
    
    Optional<SellerProduct> findByProductIdAndSellerIdAndMerchantId(UUID productId, UUID sellerId, UUID merchantId);
    
    List<SellerProduct> findBySellerIdAndMerchantId(UUID sellerId, UUID merchantId);


    boolean existsByProductIdAndSellerIdAndIsManufacturerTrue(@NotNull(message = "Product ID is required") UUID productId, @NotNull(message = "Seller ID is required") UUID sellerId);

    @Query("SELECT sp FROM SellerProduct sp WHERE sp.product.id = :productId AND sp.merchant.id = :merchantId")
    Collection<SellerProduct> findByProductAndMerchant(UUID productId, UUID merchantId);

    @Query("SELECT sp FROM SellerProduct sp WHERE sp.seller.id = :sellerId AND sp.merchant.id = :merchantId ORDER BY sp.createdDate DESC")
    Collection<SellerProduct> findBySellerIdAndMerchantIdOrderByCreatedDateDesc(UUID sellerId, UUID merchantId);

    @Query("SELECT sp FROM SellerProduct sp WHERE sp.merchant.id = :merchantId AND sp.price BETWEEN :minPrice AND :maxPrice")
    Collection<SellerProduct> findByPriceRange(UUID merchantId, BigDecimal minPrice, BigDecimal maxPrice);

    @Query("SELECT sp FROM SellerProduct sp WHERE sp.merchant.id = :merchantId AND sp.stock < :threshold")
    Collection<SellerProduct> findLowStockProducts(UUID merchantId, Integer threshold);

    @Query("SELECT sp FROM SellerProduct sp WHERE sp.product.id = :productId")
    Collection<SellerProduct> findManufacturerForProduct(UUID productId);
}
