package com.nosql.poc.vendor.repository;

import com.nosql.poc.vendor.model.SimpleVendor;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository for vendor data.
 */
@Repository
public interface VendorRepository extends MongoRepository<SimpleVendor, String> {
    
    /**
     * Find a vendor by its vendor ID.
     * 
     * @param vendorId the vendor ID
     * @return the vendor if found
     */
    Optional<SimpleVendor> findByVendorId(String vendorId);
    
    /**
     * Find vendors by business name containing the given text.
     * 
     * @param name the business name to search
     * @return list of matching vendors
     */
    List<SimpleVendor> findByBusinessNameContaining(String name);
    
    /**
     * Find active vendors.
     * 
     * @return list of active vendors
     */
    List<SimpleVendor> findByActiveTrue();
    
    /**
     * Find vendors by seller type.
     * 
     * @param sellerType the seller type
     * @return list of matching vendors
     */
    List<SimpleVendor> findBySellerType(String sellerType);
}