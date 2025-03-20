package com.scaler.rules.repository;

import com.scaler.rules.model.SellerProductRule;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SellerProductRuleRepository extends MongoRepository<SellerProductRule, String> {
    
    List<SellerProductRule> findBySellerIdAndProductId(String sellerId, String productId);
    
    List<SellerProductRule> findBySellerIdAndRuleTypeAndActive(String sellerId, String ruleType, Boolean active);
    
    List<SellerProductRule> findByProductIdAndRuleTypeAndActive(String productId, String ruleType, Boolean active);
}