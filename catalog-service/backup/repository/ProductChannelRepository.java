package com.scaler.repository;

import com.scaler.entity.ProductChannel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface ProductChannelRepository extends JpaRepository<ProductChannel, UUID> {
    List<ProductChannel> findByProductId(UUID productId);
    List<ProductChannel> findByChannelId(UUID channelId);
    
    @Query("SELECT pc FROM ProductChannel pc " +
           "WHERE pc.product.id = :productId " +
           "AND pc.isEnabled = true " +
           "AND pc.isVisible = true " +
           "AND (pc.effectiveTo IS NULL OR pc.effectiveTo > :now)")
    List<ProductChannel> findActiveChannelsForProduct(UUID productId, LocalDateTime now);

    @Query("SELECT pc FROM ProductChannel pc " +
           "WHERE pc.channel.id = :channelId " +
           "AND pc.isEnabled = true " +
           "AND pc.isVisible = true " +
           "AND (pc.effectiveTo IS NULL OR pc.effectiveTo > :now)")
    List<ProductChannel> findActiveProductsForChannel(UUID channelId, LocalDateTime now);
}
