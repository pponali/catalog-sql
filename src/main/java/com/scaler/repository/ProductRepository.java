package com.scaler.repository;

import com.scaler.entity.Product;
import com.scaler.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ProductRepository extends JpaRepository<Product, UUID>, JpaSpecificationExecutor<Product> {
    Optional<Product> findBySku(String sku);
    
    // Basic queries
    @Query("SELECT DISTINCT p FROM Product p " +
           "WHERE p.id = :productId " +
           "AND p.merchant.id = :merchantId")
    Optional<Product> findByIdAndMerchantId(
            @Param("productId") UUID productId,
            @Param("merchantId") UUID merchantId);
    
    @Query("SELECT DISTINCT p FROM Product p " +
           "JOIN p.channels ch " +
           "WHERE p.id = :productId " +
           "AND ch.id = :channelId")
    Optional<Product> findByIdAndChannelId(
            @Param("productId") UUID productId,
            @Param("channelId") UUID channelId);

    @Query("SELECT DISTINCT p FROM Product p " +
           "JOIN p.sellers s " +
           "WHERE p.id = :productId " +
           "AND s.id = :sellerId")
    Optional<Product> findByIdAndSellerId(
            @Param("productId") UUID productId,
            @Param("sellerId") UUID sellerId);
    
    @Query("SELECT DISTINCT p FROM Product p " +
           "JOIN p.categories c " +
           "WHERE p.id = :productId " +
           "AND c.id = :categoryId")
    Optional<Product> findByIdAndCategoryId(
            @Param("productId") UUID productId,
            @Param("categoryId") UUID categoryId);
    
    // Store (Merchant) based queries
    @Query("SELECT DISTINCT p FROM Product p " +
           "JOIN p.sellers s " +
           "WHERE p.merchant.id = :merchantId " +
           "AND s.id = :sellerId " +
           "AND p.id = :productId")
    Optional<Product> findByMerchantIdAndSellerIdAndProductId(
            @Param("merchantId") UUID merchantId,
            @Param("sellerId") UUID sellerId,
            @Param("productId") UUID productId);

    @Query("SELECT DISTINCT p FROM Product p " +
           "JOIN p.sellers s " +
           "JOIN p.catalog c " +
           "WHERE p.merchant.id = :merchantId " +
           "AND s.id = :sellerId " +
           "AND p.id = :productId " +
           "AND c.id = :catalogId")
    Optional<Product> findByMerchantIdAndSellerIdAndProductIdAndCatalogId(
            @Param("merchantId") UUID merchantId,
            @Param("sellerId") UUID sellerId,
            @Param("productId") UUID productId,
            @Param("catalogId") UUID catalogId);

    // Store and Channel based queries
    @Query("SELECT DISTINCT p FROM Product p " +
           "JOIN p.channels ch " +
           "WHERE p.id = :productId " +
           "AND p.merchant.id = :merchantId " +
           "AND ch.id = :channelId")
    Optional<Product> findByIdAndMerchantIdAndChannelId(
            @Param("productId") UUID productId,
            @Param("merchantId") UUID merchantId,
            @Param("channelId") UUID channelId);
    
    // Store and Category based queries
    @Query("SELECT DISTINCT p FROM Product p " +
           "JOIN p.categories c " +
           "WHERE p.id = :productId " +
           "AND p.merchant.id = :merchantId " +
           "AND c.id = :categoryId")
    Optional<Product> findByIdAndMerchantIdAndCategoryId(
            @Param("productId") UUID productId,
            @Param("merchantId") UUID merchantId,
            @Param("categoryId") UUID categoryId);
    
    // Seller and Category based queries
    @Query("SELECT DISTINCT p FROM Product p " +
           "JOIN p.sellers s " +
           "JOIN p.categories c " +
           "WHERE p.id = :productId " +
           "AND s.id = :sellerId " +
           "AND c.id = :categoryId")
    Optional<Product> findByIdAndSellerIdAndCategoryId(
            @Param("productId") UUID productId,
            @Param("sellerId") UUID sellerId,
            @Param("categoryId") UUID categoryId);
    
    // Channel and Category based queries
    @Query("SELECT DISTINCT p FROM Product p " +
           "JOIN p.channels ch " +
           "JOIN p.categories c " +
           "WHERE p.id = :productId " +
           "AND ch.id = :channelId " +
           "AND c.id = :categoryId")
    Optional<Product> findByIdAndChannelIdAndCategoryId(
            @Param("productId") UUID productId,
            @Param("channelId") UUID channelId,
            @Param("categoryId") UUID categoryId);

    // Three-way combinations
    @Query("SELECT DISTINCT p FROM Product p " +
           "JOIN p.channels ch " +
           "JOIN p.sellers s " +
           "WHERE p.id = :productId " +
           "AND p.merchant.id = :merchantId " +
           "AND ch.id = :channelId " +
           "AND s.id = :sellerId")
    Optional<Product> findByIdAndMerchantIdAndChannelIdAndSellerId(
            @Param("productId") UUID productId,
            @Param("merchantId") UUID merchantId,
            @Param("channelId") UUID channelId,
            @Param("sellerId") UUID sellerId);

    @Query("SELECT DISTINCT p FROM Product p " +
           "JOIN p.channels ch " +
           "JOIN p.categories c " +
           "WHERE p.id = :productId " +
           "AND p.merchant.id = :merchantId " +
           "AND ch.id = :channelId " +
           "AND c.id = :categoryId")
    Optional<Product> findByIdAndMerchantIdAndChannelIdAndCategoryId(
            @Param("productId") UUID productId,
            @Param("merchantId") UUID merchantId,
            @Param("channelId") UUID channelId,
            @Param("categoryId") UUID categoryId);

    @Query("SELECT DISTINCT p FROM Product p " +
           "JOIN p.sellers s " +
           "JOIN p.categories c " +
           "WHERE p.id = :productId " +
           "AND p.merchant.id = :merchantId " +
           "AND s.id = :sellerId " +
           "AND c.id = :categoryId")
    Optional<Product> findByIdAndMerchantIdAndSellerIdAndCategoryId(
            @Param("productId") UUID productId,
            @Param("merchantId") UUID merchantId,
            @Param("sellerId") UUID sellerId,
            @Param("categoryId") UUID categoryId);

    @Query("SELECT DISTINCT p FROM Product p " +
           "JOIN p.channels ch " +
           "JOIN p.sellers s " +
           "JOIN p.categories c " +
           "WHERE p.id = :productId " +
           "AND ch.id = :channelId " +
           "AND s.id = :sellerId " +
           "AND c.id = :categoryId")
    Optional<Product> findByIdAndChannelIdAndSellerIdAndCategoryId(
            @Param("productId") UUID productId,
            @Param("channelId") UUID channelId,
            @Param("sellerId") UUID sellerId,
            @Param("categoryId") UUID categoryId);

    // Four-way combination
    @Query("SELECT DISTINCT p FROM Product p " +
           "JOIN p.channels ch " +
           "JOIN p.sellers s " +
           "JOIN p.categories c " +
           "WHERE p.id = :productId " +
           "AND p.merchant.id = :merchantId " +
           "AND ch.id = :channelId " +
           "AND s.id = :sellerId " +
           "AND c.id = :categoryId")
    Optional<Product> findByIdAndMerchantIdAndChannelIdAndSellerIdAndCategoryId(
            @Param("productId") UUID productId,
            @Param("merchantId") UUID merchantId,
            @Param("channelId") UUID channelId,
            @Param("sellerId") UUID sellerId,
            @Param("categoryId") UUID categoryId);
}

