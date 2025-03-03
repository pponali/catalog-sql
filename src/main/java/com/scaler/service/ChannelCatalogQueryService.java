package com.scaler.service;

import com.scaler.entity.Catalog;
import com.scaler.entity.Channel;
import com.scaler.entity.ChannelCatalog;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ChannelCatalogQueryService {
    
    List<ChannelCatalog> getAllChannelCatalogs();
    
    Optional<ChannelCatalog> getChannelCatalogById(UUID id);
    
    List<ChannelCatalog> getChannelCatalogsByChannelId(UUID channelId);
    
    List<ChannelCatalog> getChannelCatalogsByCatalogId(UUID catalogId);
    
    Optional<ChannelCatalog> getChannelCatalogByChannelAndCatalog(UUID channelId, UUID catalogId);
    
    List<Catalog> getCatalogsByChannelId(UUID channelId);
    
    List<Channel> getChannelsByCatalogId(UUID catalogId);
    
    List<ChannelCatalog> getActiveChannelCatalogs();
    
    List<ChannelCatalog> getActiveChannelCatalogsByChannelId(UUID channelId);
    
    List<ChannelCatalog> getActiveChannelCatalogsByCatalogId(UUID catalogId);
}