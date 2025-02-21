package com.nosql.poc.channel.repository;

import com.nosql.poc.channel.model.Channel;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import java.util.List;

public interface ChannelRepository extends MongoRepository<Channel, String> {
    
    List<Channel> findByType(String type);
    
    List<Channel> findByActive(boolean active);
    
    @Query("{'configuration.entityId': ?0}")
    List<Channel> findByEntityId(String entityId);
    
    @Query("{'metrics.averageQualityScore': {$gte: ?0}}")
    List<Channel> findByMinimumQualityScore(double minScore);
}
