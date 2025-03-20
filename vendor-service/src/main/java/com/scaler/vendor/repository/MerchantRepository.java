package com.scaler.vendor.repository;

import com.scaler.vendor.model.Merchant;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MerchantRepository extends MongoRepository<Merchant, String> {
    Optional<Merchant> findByCode(String code);
}