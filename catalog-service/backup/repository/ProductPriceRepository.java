package com.scaler.repository;

import com.scaler.entity.ProductPrice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ProductPriceRepository extends JpaRepository<ProductPrice, UUID> {
    
    @Query("SELECT pp FROM ProductPrice pp " +
           "WHERE pp.product.id = :productId " +
           "AND (:channelId IS NULL OR pp.channel.id = :channelId) " +
           "AND (:sellerId IS NULL OR pp.seller.id = :sellerId) " +
           "AND pp.isActive = true " +
           "ORDER BY pp.channel.id NULLS LAST")
    List<ProductPrice> findActiveProductPrices(
            @Param("productId") UUID productId,
            @Param("channelId") UUID channelId,
            @Param("sellerId") UUID sellerId);
}
