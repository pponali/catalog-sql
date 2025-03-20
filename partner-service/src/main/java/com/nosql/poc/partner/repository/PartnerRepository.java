package com.nosql.poc.partner.repository;

import com.nosql.poc.partner.model.SimplePartner;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;
import java.util.Optional;

public interface PartnerRepository extends MongoRepository<SimplePartner, String> {
    
    Optional<SimplePartner> findByPartnerId(String partnerId);
    
    List<SimplePartner> findByType(String type);
    
    List<SimplePartner> findByCategory(String category);
    
    @Query("{'status': 'ACTIVE'}")
    List<SimplePartner> findActivePartners();
    
    @Query("{'type': ?0, 'status': 'ACTIVE'}")
    List<SimplePartner> findActivePartnersByType(String type);
    
    @Query("{'category': ?0, 'status': 'ACTIVE'}")
    List<SimplePartner> findActivePartnersByCategory(String category);
}