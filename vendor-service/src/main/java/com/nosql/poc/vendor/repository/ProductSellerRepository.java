package com.nosql.poc.vendor.repository;

import com.nosql.poc.vendor.model.ProductSeller;
import com.nosql.poc.vendor.model.SellerType;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ProductSellerRepository extends MongoRepository<ProductSeller, String> {
    
    List<ProductSeller> findByProductId(String productId);
    
    List<ProductSeller> findByVendorId(String vendorId);
    
    Optional<ProductSeller> findByProductIdAndVendorId(String productId, String vendorId);
    
    List<ProductSeller> findByProductIdAndSellerType(String productId, SellerType sellerType);
    
    @Query("{'productId': ?0, 'isActive': true}")
    List<ProductSeller> findActiveSellersByProductId(String productId);
    
    @Query("{'productId': ?0, 'sellerType': ?1, 'isActive': true}")
    List<ProductSeller> findActiveSellersByProductIdAndType(String productId, SellerType sellerType);
}
