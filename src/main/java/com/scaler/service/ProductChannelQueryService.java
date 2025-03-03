package com.scaler.service;

import com.scaler.entity.Channel;
import com.scaler.entity.Product;
import com.scaler.entity.ProductChannel;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ProductChannelQueryService {
    
    List<ProductChannel> getAllProductChannels();
    
    Optional<ProductChannel> getProductChannelById(UUID id);
    
    List<ProductChannel> getProductChannelsByProductId(UUID productId);
    
    List<ProductChannel> getProductChannelsByChannelId(UUID channelId);
    
    Optional<ProductChannel> getProductChannelByProductAndChannel(UUID productId, UUID channelId);
    
    List<Channel> getChannelsByProductId(UUID productId);
    
    List<Product> getProductsByChannelId(UUID channelId);
    
    List<Product> getProductsByChannelIdAndActive(UUID channelId, boolean isActive);
}