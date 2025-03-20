package com.nosql.poc.channel.service;

import com.nosql.poc.channel.dto.PartnerDTO;
import java.util.List;

/**
 * Service for managing partners.
 * Renamed from BusinessService to align with microservice architecture.
 */
public interface PartnerService {
    /**
     * Create a new partner
     */
    PartnerDTO createPartner(PartnerDTO partnerDTO);
    
    /**
     * Update an existing partner
     */
    PartnerDTO updatePartner(String id, PartnerDTO partnerDTO);
    
    /**
     * Get a partner by ID
     */
    PartnerDTO getPartner(String id);
    
    /**
     * Get all partners
     */
    List<PartnerDTO> getAllPartners();
    
    /**
     * Delete a partner by ID
     */
    void deletePartner(String id);
}
