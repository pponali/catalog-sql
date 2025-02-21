package com.scaler.repository;

import com.scaler.entity.ProductInventory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ProductInventoryRepository extends JpaRepository<ProductInventory, Long> {
    
    @Query("SELECT pi FROM ProductInventory pi " +
           "WHERE pi.product.id = :productId " +
           "AND pi.merchant.id = :merchantId " +
           "AND pi.channel.id = :channelId")
    Optional<ProductInventory> findByProductMerchantAndChannel(
            @Param("productId") Long productId,
            @Param("merchantId") Long merchantId,
            @Param("channelId") Long channelId);
}
