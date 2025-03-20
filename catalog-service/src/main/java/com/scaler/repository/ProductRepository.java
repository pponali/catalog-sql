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
    Optional<Product> findByCode(String code);
    
    // Basic queries
    @Query("SELECT DISTINCT p FROM Product p " +
           "WHERE p.id = :productId " +
           "AND p.merchant.id = :merchantId")
    Optional<Product> findByIdAndMerchantId(
            @Param("productId") UUID productId,
            @Param("merchantId") UUID merchantId);
    
    // Modified to not use productChannels which was moved to channel-service
    @Query("SELECT DISTINCT p FROM Product p " +
           "WHERE p.id = :productId")
    Optional<Product> findByIdAndChannelId(
            @Param("productId") UUID productId,
            @Param("channelId") UUID channelId); // Parameter kept for API compatibility

    @Query("SELECT DISTINCT p FROM Product p " +
           "WHERE p.id = :productId")
    Optional<Product> findByIdAndSellerId(
            @Param("productId") UUID productId,
            @Param("sellerId") UUID sellerId); // Parameter kept for API compatibility
    
    @Query("SELECT DISTINCT p FROM Product p " +
           "JOIN p.productCategories pc JOIN pc.category c " +
           "WHERE p.id = :productId " +
           "AND c.id = :categoryId")
    Optional<Product> findByIdAndCategoryId(
            @Param("productId") UUID productId,
            @Param("categoryId") UUID categoryId);
    
    // Store (Merchant) based queries - simplified to not use sellerProducts
    @Query("SELECT DISTINCT p FROM Product p " +
           "WHERE p.merchant.id = :merchantId " +
           "AND p.id = :productId")
    Optional<Product> findByMerchantIdAndSellerIdAndProductId(
            @Param("merchantId") UUID merchantId,
            @Param("sellerId") UUID sellerId, // Parameter kept for API compatibility
            @Param("productId") UUID productId);

    @Query("SELECT DISTINCT p FROM Product p " +
           "JOIN p.catalog c " +
           "WHERE p.merchant.id = :merchantId " +
           "AND p.id = :productId " +
           "AND c.id = :catalogId")
    Optional<Product> findByMerchantIdAndSellerIdAndProductIdAndCatalogId(
            @Param("merchantId") UUID merchantId,
            @Param("sellerId") UUID sellerId, // Parameter kept for API compatibility
            @Param("productId") UUID productId,
            @Param("catalogId") UUID catalogId);

    // Store and Channel based queries - Modified to not use productChannels which was moved to channel-service
    @Query("SELECT DISTINCT p FROM Product p " +
           "WHERE p.id = :productId " +
           "AND p.merchant.id = :merchantId")
    Optional<Product> findByIdAndMerchantIdAndChannelId(
            @Param("productId") UUID productId,
            @Param("merchantId") UUID merchantId,
            @Param("channelId") UUID channelId); // Parameter kept for API compatibility
    
    // Store and Category based queries
    @Query("SELECT DISTINCT p FROM Product p " +
           "JOIN p.productCategories pc JOIN pc.category c " +
           "WHERE p.id = :productId " +
           "AND p.merchant.id = :merchantId " +
           "AND c.id = :categoryId")
    Optional<Product> findByIdAndMerchantIdAndCategoryId(
            @Param("productId") UUID productId,
            @Param("merchantId") UUID merchantId,
            @Param("categoryId") UUID categoryId);
    
    // Seller and Category based queries - simplified to not use sellerProducts
    @Query("SELECT DISTINCT p FROM Product p " +
           "JOIN p.productCategories pc JOIN pc.category c " +
           "WHERE p.id = :productId " +
           "AND c.id = :categoryId")
    Optional<Product> findByIdAndSellerIdAndCategoryId(
            @Param("productId") UUID productId,
            @Param("sellerId") UUID sellerId, // Parameter kept for API compatibility
            @Param("categoryId") UUID categoryId);
    
    // Channel and Category based queries
    // Modified to not use productChannels which was moved to channel-service
    @Query("SELECT DISTINCT p FROM Product p " +
           "JOIN p.productCategories pcat JOIN pcat.category c " +
           "WHERE p.id = :productId " +
           "AND c.id = :categoryId")
    Optional<Product> findByIdAndChannelIdAndCategoryId(
            @Param("productId") UUID productId,
            @Param("channelId") UUID channelId, // Parameter kept for API compatibility
            @Param("categoryId") UUID categoryId);


    // Modified to not use productChannels which was moved to channel-service
    @Query("SELECT DISTINCT p FROM Product p " +
           "JOIN p.productCategories pcat JOIN pcat.category c " +
           "WHERE p.id = :productId " +
           "AND p.merchant.id = :merchantId " +
           "AND c.id = :categoryId")
    Optional<Product> findByIdAndMerchantIdAndChannelIdAndCategoryId(
            @Param("productId") UUID productId,
            @Param("merchantId") UUID merchantId,
            @Param("channelId") UUID channelId, // Parameter kept for API compatibility
            @Param("categoryId") UUID categoryId);

    @Query("SELECT DISTINCT p FROM Product p " +
           "JOIN p.productCategories pc JOIN pc.category c " +
           "WHERE p.id = :productId " +
           "AND p.merchant.id = :merchantId " +
           "AND c.id = :categoryId")
    Optional<Product> findByIdAndMerchantIdAndSellerIdAndCategoryId(
            @Param("productId") UUID productId,
            @Param("merchantId") UUID merchantId,
            @Param("sellerId") UUID sellerId, // Parameter kept for API compatibility
            @Param("categoryId") UUID categoryId);


    // Four-way combination - Modified to not use productChannels or sellerProducts
    @Query("SELECT DISTINCT p FROM Product p " +
           "JOIN p.productCategories pcat JOIN pcat.category c " +
           "WHERE p.id = :productId " +
           "AND p.merchant.id = :merchantId " +
           "AND c.id = :categoryId")
    Optional<Product> findByIdAndMerchantIdAndChannelIdAndSellerIdAndCategoryId(
            @Param("productId") UUID productId,
            @Param("merchantId") UUID merchantId,
            @Param("channelId") UUID channelId, // Parameter kept for API compatibility
            @Param("sellerId") UUID sellerId, // Parameter kept for API compatibility
            @Param("categoryId") UUID categoryId);

    List<Product> findByMerchantId(UUID id);
    
    /**
     * Find products that belong to a specific category
     * @param categoryId The ID of the category
     * @return List of products in the category
     */
    @Query("SELECT DISTINCT p FROM Product p " +
           "JOIN p.productCategories pc JOIN pc.category c " +
           "WHERE c.id = :categoryId")
    List<Product> findByCategoriesContaining(@Param("categoryId") UUID categoryId);



    // Modified to not use productChannels or sellerProducts
    @Query("SELECT DISTINCT p FROM Product p " +
           "WHERE p.id = :productId " +
           "AND (:channelId IS NOT NULL OR :channelId IS NULL) " +
           "AND (:sellerId IS NOT NULL OR :sellerId IS NULL)")
    Optional<Product> findByIdAndChannelIdAndSellerId(
            @Param("productId") UUID productId,
            @Param("channelId") UUID channelId, // Parameter kept for API compatibility
            @Param("sellerId") UUID sellerId); // Parameter kept for API compatibility
            
    // Updated query to not use sellerProducts which doesn't exist in the Product entity
    // We've simplified this query to just use direct product properties
    @Query("SELECT DISTINCT p FROM Product p " +
           "WHERE p.id = :productId " +
           "AND p.merchant.id = :merchantId " +
           "AND (:channelId IS NOT NULL OR :channelId IS NULL) " +
           "AND (:sellerId IS NOT NULL OR :sellerId IS NULL)")
    Optional<Product> findByIdAndMerchantIdAndChannelIdAndSellerId(
            @Param("productId") UUID productId,
            @Param("merchantId") UUID merchantId,
            @Param("channelId") UUID channelId, // Parameter kept for API compatibility
            @Param("sellerId") UUID sellerId); // Parameter kept for API compatibility

}
