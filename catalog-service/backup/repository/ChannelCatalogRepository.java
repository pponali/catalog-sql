package com.scaler.repository;

import com.scaler.entity.ChannelCatalog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ChannelCatalogRepository extends JpaRepository<ChannelCatalog, UUID> {
    List<ChannelCatalog> findByChannelId(UUID channelId);
    List<ChannelCatalog> findByCatalogId(UUID catalogId);
    List<ChannelCatalog> findByChannelIdAndIsEnabled(UUID channelId, Boolean isEnabled);
}
