package com.nosql.poc.partner.repository;

import com.nosql.poc.partner.model.SimplePartnerContract;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface PartnerContractRepository extends MongoRepository<SimplePartnerContract, String> {
    
    Optional<SimplePartnerContract> findByContractId(String contractId);
    
    List<SimplePartnerContract> findByPartnerId(String partnerId);
    
    List<SimplePartnerContract> findByContractType(String contractType);
    
    List<SimplePartnerContract> findByStatus(String status);
    
    List<SimplePartnerContract> findByPartnerIdAndStatus(String partnerId, String status);
    
    @Query("{'startDate': {$lte: ?0}, 'endDate': {$gte: ?0}, 'status': 'ACTIVE'}")
    List<SimplePartnerContract> findActiveContractsForDate(LocalDate date);
    
    @Query("{'endDate': {$lte: ?0}, 'status': 'ACTIVE'}")
    List<SimplePartnerContract> findContractsExpiringBefore(LocalDate date);
    
    @Query("{'autoRenewal': true, 'endDate': {$lte: ?0, $gte: ?1}, 'status': 'ACTIVE'}")
    List<SimplePartnerContract> findContractsForAutoRenewal(LocalDate upperBound, LocalDate lowerBound);
}