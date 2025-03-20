package com.nosql.poc.channel.repository;

import com.nosql.poc.channel.model.ChannelProduct;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;
import java.util.Optional;

/**
 * Repository for ChannelProduct entities.
 */
public interface ChannelProductRepository extends MongoRepository<ChannelProduct, String> {
    
    /**
     * Find channel products by product ID.
     * 
     * @param productId the product ID
     * @return list of channel products
     */
    List<ChannelProduct> findByProductId(String productId);
    
    /**
     * Find channel products by channel ID.
     * 
     * @param channelId the channel ID
     * @return list of channel products
     */
    List<ChannelProduct> findByChannelId(String channelId);
    
    /**
     * Find a specific product in a specific channel.
     * 
     * @param productId the product ID
     * @param channelId the channel ID
     * @return optional channel product
     */
    Optional<ChannelProduct> findByProductIdAndChannelId(String productId, String channelId);
    
    /**
     * Find active products in a channel.
     * 
     * @param channelId the channel ID
     * @return list of active channel products
     */
    @Query("{'channelId': ?0, 'active': true}")
    List<ChannelProduct> findActiveProductsByChannelId(String channelId);
    
    /**
     * Find active channels for a product.
     * 
     * @param productId the product ID
     * @return list of active channel products
     */
    @Query("{'productId': ?0, 'active': true}")
    List<ChannelProduct> findActiveChannelsByProductId(String productId);
}