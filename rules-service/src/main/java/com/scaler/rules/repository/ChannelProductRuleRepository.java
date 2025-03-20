package com.scaler.rules.repository;

import com.scaler.rules.model.ChannelProductRule;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChannelProductRuleRepository extends MongoRepository<ChannelProductRule, String> {
    
    List<ChannelProductRule> findByChannelIdAndProductId(String channelId, String productId);
    
    List<ChannelProductRule> findByChannelIdAndRuleTypeAndActive(String channelId, String ruleType, Boolean active);
    
    List<ChannelProductRule> findByProductIdAndRuleTypeAndActive(String productId, String ruleType, Boolean active);
}