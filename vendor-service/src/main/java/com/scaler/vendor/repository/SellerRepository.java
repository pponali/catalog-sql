package com.scaler.vendor.repository;

import com.scaler.vendor.model.Seller;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SellerRepository extends MongoRepository<Seller, String> {
    Optional<Seller> findByCode(String code);
    List<Seller> findByMerchantId(String merchantId);
}