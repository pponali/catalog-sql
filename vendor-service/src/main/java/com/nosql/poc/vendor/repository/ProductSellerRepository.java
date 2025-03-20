package com.nosql.poc.vendor.repository;

import com.nosql.poc.vendor.model.SimpleProductSeller;
import com.nosql.poc.vendor.model.SellerType;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ProductSellerRepository extends MongoRepository<SimpleProductSeller, String> {
    
    List<SimpleProductSeller> findByProductId(String productId);
    
    List<SimpleProductSeller> findByVendorId(String vendorId);
    
    Optional<SimpleProductSeller> findByProductIdAndVendorId(String productId, String vendorId);
    
    List<SimpleProductSeller> findByProductIdAndSellerType(String productId, SellerType sellerType);
    
    @Query("{'productId': ?0, 'isActive': true}")
    List<SimpleProductSeller> findActiveSellersByProductId(String productId);
    
    @Query("{'productId': ?0, 'sellerType': ?1, 'isActive': true}")
    List<SimpleProductSeller> findActiveSellersByProductIdAndType(String productId, SellerType sellerType);
}
