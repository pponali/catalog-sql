package com.nosql.poc.channel.service.impl;

import com.nosql.poc.channel.dto.PartnerDTO;
import com.nosql.poc.channel.service.PartnerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Mock implementation of PartnerService.
 * This is a temporary implementation for testing without dependencies.
 */
@Service
public class PartnerServiceImpl implements PartnerService {
    
    private static final Logger log = LoggerFactory.getLogger(PartnerServiceImpl.class);
    
    // In-memory store for partners
    private final Map<String, PartnerDTO> partners = new ConcurrentHashMap<>();
    
    public PartnerServiceImpl() {
        // Add some sample data
        createSamplePartners();
    }
    
    private void createSamplePartners() {
        PartnerDTO partner1 = PartnerDTO.builder()
                .id(UUID.randomUUID().toString())
                .name("Sample Partner 1")
                .code("PARTNER1")
                .type("RETAIL")
                .status("ACTIVE")
                .contactEmail("partner1@example.com")
                .contactPhone("1234567890")
                .additionalDetails(Map.of("website", "https://partner1.example.com"))
                .build();
        
        PartnerDTO partner2 = PartnerDTO.builder()
                .id(UUID.randomUUID().toString())
                .name("Sample Partner 2")
                .code("PARTNER2")
                .type("MARKETPLACE")
                .status("ACTIVE")
                .contactEmail("partner2@example.com")
                .contactPhone("0987654321")
                .additionalDetails(Map.of("website", "https://partner2.example.com"))
                .build();
        
        partners.put(partner1.getId(), partner1);
        partners.put(partner2.getId(), partner2);
        
        log.info("Created sample partners: {} and {}", partner1.getId(), partner2.getId());
    }
    
    @Override
    public PartnerDTO createPartner(PartnerDTO partnerDTO) {
        String id = UUID.randomUUID().toString();
        partnerDTO.setId(id);
        
        log.info("Creating partner with ID: {}", id);
        partners.put(id, partnerDTO);
        
        return partnerDTO;
    }
    
    @Override
    public PartnerDTO updatePartner(String id, PartnerDTO partnerDTO) {
        log.info("Updating partner with ID: {}", id);
        
        if (!partners.containsKey(id)) {
            log.warn("Partner not found with ID: {}", id);
            return null;
        }
        
        partnerDTO.setId(id);
        partners.put(id, partnerDTO);
        return partnerDTO;
    }
    
    @Override
    public PartnerDTO getPartner(String id) {
        log.info("Getting partner with ID: {}", id);
        return partners.get(id);
    }
    
    @Override
    public List<PartnerDTO> getAllPartners() {
        log.info("Getting all partners");
        return new ArrayList<>(partners.values());
    }
    
    @Override
    public void deletePartner(String id) {
        log.info("Deleting partner with ID: {}", id);
        partners.remove(id);
    }
}