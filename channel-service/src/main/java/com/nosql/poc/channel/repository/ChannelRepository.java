package com.nosql.poc.channel.repository;

import com.nosql.poc.channel.model.Channel;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import java.util.List;
import java.util.Optional;

/**
 * Repository for Channel entities.
 */
public interface ChannelRepository extends MongoRepository<Channel, String> {
    
    /**
     * Find a channel by its ID.
     * 
     * @param channelId the channel ID
     * @return optional channel
     */
    Optional<Channel> findByChannelId(String channelId);
    
    /**
     * Find channels by type.
     * 
     * @param type the channel type
     * @return list of channels
     */
    List<Channel> findByType(String type);
    
    /**
     * Find channels by active status.
     * 
     * @param active active status
     * @return list of channels
     */
    List<Channel> findByActive(boolean active);
    
    /**
     * Find all active channels.
     * 
     * @return list of active channels
     */
    @Query("{'active': true}")
    List<Channel> findActiveChannels();
    
    /**
     * Find active channels by region.
     * 
     * @param region the region
     * @return list of active channels in the region
     */
    @Query("{'attributes.region': ?0, 'active': true}")
    List<Channel> findActiveChannelsByRegion(String region);
}
