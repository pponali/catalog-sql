package com.scaler.repository;

import com.scaler.entity.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface QueryRepository extends JpaRepository<Product, String> {

    @Query("SELECT DISTINCT p FROM Product p JOIN productCategories c WHERE c.category.id = :categoryId")
    List<Product> findProductsByCategory(@Param("categoryId") UUID categoryId);

    @Query("SELECT DISTINCT p FROM Product p JOIN p.productChannels pc JOIN pc.channel c WHERE c.id = :channelId")
    List<Product> findProductsByChannel(@Param("channelId") UUID channelId);

    @Query("SELECT DISTINCT p FROM Product p JOIN p.sellerProducts sp WHERE sp.seller.id = :sellerId")
    List<Product> findProductsBySeller(@Param("sellerId") UUID sellerId);

    @Query("SELECT DISTINCT p FROM Product p WHERE p.merchant.id = :merchantId")
    List<Product> findProductsByMerchant(@Param("merchantId") UUID merchantId);

    @Query("SELECT DISTINCT c FROM Category c JOIN c.productCategories pc JOIN pc.product p JOIN p.productChannels pch JOIN pch.channel ch WHERE ch.id = :channelId")
    List<Category> findCategoriesByChannel(@Param("channelId") UUID channelId);

    @Query("SELECT DISTINCT s FROM Seller s JOIN s.sellerProducts sp JOIN sp.product p JOIN p.productChannels pc JOIN pc.channel c WHERE c.id = :channelId")
    List<Seller> findSellersByChannel(@Param("channelId") UUID channelId);

    @Query("SELECT DISTINCT m FROM Merchant m JOIN Seller s JOIN s.sellerProducts sp JOIN sp.product p JOIN p.productChannels pc JOIN pc.channel c WHERE c.id = :channelId")
    List<Merchant> findMerchantsByChannel(@Param("channelId") UUID channelId);

    @Query("SELECT DISTINCT p FROM Product p JOIN p.productPlatforms pp WHERE pp.platform.id = :platformId AND pp.isActive = true")
    List<Product> findProductsByPlatform(@Param("platformId") UUID platformId);
}
