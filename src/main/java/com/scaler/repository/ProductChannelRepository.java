package com.scaler.repository;

import com.scaler.entity.ProductChannel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ProductChannelRepository extends JpaRepository<ProductChannel, UUID> {
    
    List<ProductChannel> findByProductId(UUID productId);
    
    List<ProductChannel> findByChannelId(UUID channelId);
    
    Optional<ProductChannel> findByProductIdAndChannelId(UUID productId, UUID channelId);
    
    List<ProductChannel> findByChannelIdAndIsEnabled(UUID channelId, boolean isEnabled);
}
