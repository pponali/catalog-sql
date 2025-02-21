package com.scaler.repository;

import com.scaler.entity.Product;
import com.scaler.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ProductRepository extends JpaRepository<Product, UUID> {
    Optional<Product> findBySku(String sku);
    
    @Query("SELECT p FROM Product p JOIN p.categories c WHERE c = :category")
    List<Product> findByCategory(@Param("category") Category category);

    List<Product> findByMerchantId(UUID merchantId);
    List<Product> findByMerchantIdAndCategories(UUID merchantId, Category category);
    Optional<Product> findByMerchantIdAndId(UUID merchantId, UUID id);
    List<Product> findByCatalogId(UUID catalogId);

    @Query("SELECT DISTINCT p FROM Product p " +
           "JOIN p.channels c " +
           "WHERE p.merchant.id = :merchantId " +
           "AND c.id = :channelId")
    List<Product> findByMerchantAndChannel(
            @Param("merchantId") Long merchantId,
            @Param("channelId") Long channelId);
            
    @Query("SELECT DISTINCT p FROM Product p " +
           "JOIN p.channels c " +
           "WHERE p.merchant.id = :merchantId " +
           "AND c.id = :channelId " +
           "AND p.lineOfBusiness.id = :lineOfBusinessId")
    List<Product> findByMerchantChannelAndLob(
            @Param("merchantId") Long merchantId,
            @Param("channelId") Long channelId,
            @Param("lineOfBusinessId") Long lineOfBusinessId);
}
