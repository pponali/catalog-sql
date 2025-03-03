package com.scaler.repository;

import com.scaler.entity.ChannelCatalog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ChannelCatalogRepository extends JpaRepository<ChannelCatalog, UUID> {
    
    List<ChannelCatalog> findByChannelId(UUID channelId);
    
    List<ChannelCatalog> findByCatalogId(UUID catalogId);
    
    Optional<ChannelCatalog> findByChannelIdAndCatalogId(UUID channelId, UUID catalogId);
    
    List<ChannelCatalog> findByIsEnabledTrue();
    
    List<ChannelCatalog> findByChannelIdAndIsEnabledTrue(UUID channelId);
    
    List<ChannelCatalog> findByCatalogIdAndIsEnabledTrue(UUID catalogId);
}
