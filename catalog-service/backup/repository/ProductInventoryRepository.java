package com.scaler.repository;

import com.scaler.entity.ProductInventory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

public interface ProductInventoryRepository extends JpaRepository<ProductInventory, UUID> {
    
    @Query("SELECT pi FROM ProductInventory pi " +
           "WHERE pi.product.id = :productId " +
           "AND (:merchantId IS NULL OR pi.merchant.id = :merchantId) " +
           "AND (:channelId IS NULL OR pi.channel.id = :channelId) " +
           "AND (:sellerId IS NULL OR pi.seller.id = :sellerId)")
    Optional<ProductInventory> findByProductIdAndMerchantIdAndChannelId(
            @Param("productId") UUID productId,
            @Param("merchantId") UUID merchantId,
            @Param("channelId") UUID channelId,
            @Param("sellerId") UUID sellerId);
}
