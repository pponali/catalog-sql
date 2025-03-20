package com.scaler.repository;

import com.scaler.entity.Seller;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface SellerRepository extends JpaRepository<Seller, UUID> {
    List<Seller> findByMerchantId(UUID merchantId);
    boolean existsByCode(String code);
}
