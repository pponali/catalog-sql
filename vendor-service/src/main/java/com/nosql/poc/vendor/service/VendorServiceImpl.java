package com.nosql.poc.vendor.service;

import com.nosql.poc.vendor.event.ChannelUpdateEvent;
import com.nosql.poc.vendor.event.ProductUpdateEvent;
import com.nosql.poc.vendor.event.VendorEventPublisher;
import com.nosql.poc.vendor.model.AuditInfo;
import com.nosql.poc.vendor.model.ContactInformation;
import com.nosql.poc.vendor.model.SimpleVendor;
import com.nosql.poc.vendor.model.Vendor;
import com.nosql.poc.vendor.repository.VendorRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Implementation of the VendorService interface.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class VendorServiceImpl implements VendorService {

    private final VendorRepository vendorRepository;
    private final VendorEventPublisher eventPublisher;

    /**
     * Get all vendors.
     * 
     * @return list of all vendors
     */
    @Override
    public List<Vendor> getAllVendors() {
        log.debug("Getting all vendors");
        List<SimpleVendor> simpleVendors = vendorRepository.findAll();
        
        // Convert SimpleVendor to Vendor
        return simpleVendors.stream()
                .map(this::convertToVendor)
                .collect(Collectors.toList());
    }

    /**
     * Get a vendor by ID.
     * 
     * @param id the vendor ID
     * @return the vendor if found
     */
    @Override
    public Optional<Vendor> getVendorById(String id) {
        log.debug("Getting vendor with ID: {}", id);
        return vendorRepository.findById(id)
                .map(this::convertToVendor);
    }

    /**
     * Create a new vendor.
     * 
     * @param vendor the vendor to create
     * @return the created vendor
     */
    @Override
    public Vendor createVendor(Vendor vendor) {
        log.debug("Creating new vendor: {}", vendor.getBusinessName());
        
        // Convert Vendor to SimpleVendor
        SimpleVendor simpleVendor = new SimpleVendor();
        simpleVendor.setId(UUID.randomUUID().toString());
        simpleVendor.setVendorId(vendor.getVendorId());
        simpleVendor.setBusinessName(vendor.getBusinessName());
        simpleVendor.setEmail(vendor.getContactInformation() != null ? vendor.getContactInformation().getEmail() : null);
        simpleVendor.setPhone(vendor.getContactInformation() != null ? vendor.getContactInformation().getPhone() : null);
        simpleVendor.setLegalName(vendor.getBusinessName());
        simpleVendor.setActive(true);
        simpleVendor.setCreatedAt(LocalDateTime.now());
        simpleVendor.setUpdatedAt(LocalDateTime.now());
        
        // Save the SimpleVendor
        SimpleVendor savedVendor = vendorRepository.save(simpleVendor);
        
        // Convert back to Vendor
        return convertToVendor(savedVendor);
    }

    /**
     * Update an existing vendor.
     * 
     * @param id the vendor ID
     * @param vendor the updated vendor
     * @return the updated vendor
     */
    @Override
    public Vendor updateVendor(String id, Vendor vendor) {
        log.debug("Updating vendor with ID: {}", id);
        
        SimpleVendor simpleVendor = vendorRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Vendor not found: " + id));
        
        // Update SimpleVendor fields
        simpleVendor.setBusinessName(vendor.getBusinessName());
        simpleVendor.setEmail(vendor.getContactInformation() != null ? vendor.getContactInformation().getEmail() : null);
        simpleVendor.setPhone(vendor.getContactInformation() != null ? vendor.getContactInformation().getPhone() : null);
        simpleVendor.setLegalName(vendor.getBusinessName());
        simpleVendor.setUpdatedAt(LocalDateTime.now());
        
        // Save the updated SimpleVendor
        SimpleVendor updatedVendor = vendorRepository.save(simpleVendor);
        
        // Convert back to Vendor
        return convertToVendor(updatedVendor);
    }

    /**
     * Delete a vendor.
     * 
     * @param id the vendor ID
     */
    @Override
    public void deleteVendor(String id) {
        log.debug("Deleting vendor with ID: {}", id);
        vendorRepository.deleteById(id);
    }

    /**
     * Handle channel update events.
     * 
     * @param channelId the channel ID
     */
    @Override
    public void handleChannelUpdate(String channelId) {
        log.debug("Handling channel update for channel: {}", channelId);
        // This is a simplified placeholder implementation
        // In a real service, we would update vendor-channel relationships
        
        // For now, we'll just log the event
        log.info("Channel {} updated. Vendor synchronization would occur here.", channelId);
    }

    /**
     * Handle product update events.
     * 
     * @param productId the product ID
     * @param vendorId the vendor ID
     */
    @Override
    public void handleProductUpdate(String productId, String vendorId) {
        log.debug("Handling product update for product: {} and vendor: {}", productId, vendorId);
        
        // Find the vendor
        Optional<SimpleVendor> vendorOpt = vendorRepository.findById(vendorId);
        if (vendorOpt.isPresent()) {
            // In a real service, we would update vendor-product relationships
            log.info("Product {} updated for vendor {}. Processing update.", productId, vendorId);
            
            // Publish an event for interested services
            eventPublisher.publishProductUpdate(new ProductUpdateEvent(productId, vendorId));
        } else {
            log.warn("Product update received for unknown vendor: {}", vendorId);
        }
    }

    /**
     * Sync a product to a channel.
     * 
     * @param productId the product ID
     * @param vendorId the vendor ID
     * @param channelId the channel ID
     */
    @Override
    public void syncProductToChannel(String productId, String vendorId, String channelId) {
        log.debug("Syncing product {} from vendor {} to channel {}", productId, vendorId, channelId);
        
        // In a real service, we would sync the product to the channel
        log.info("Product {} from vendor {} synced to channel {}", productId, vendorId, channelId);
        
        // Publish an event to notify the channel service
        eventPublisher.publishChannelUpdate(new ChannelUpdateEvent(channelId, vendorId, productId));
    }
    
    /**
     * Convert a SimpleVendor to a Vendor.
     * This is a simplistic conversion that only maps basic fields.
     * In a real service, a dedicated mapper would be used for this.
     * 
     * @param simpleVendor the SimpleVendor to convert
     * @return the converted Vendor
     */
    private Vendor convertToVendor(SimpleVendor simpleVendor) {
        Vendor vendor = new Vendor();
        vendor.setId(simpleVendor.getId());
        vendor.setVendorId(simpleVendor.getVendorId());
        vendor.setBusinessName(simpleVendor.getBusinessName());
        vendor.setActive(simpleVendor.isActive());
        
        // Create contact information if email or phone exists
        if (simpleVendor.getEmail() != null || simpleVendor.getPhone() != null) {
            ContactInformation contactInfo = new ContactInformation();
            contactInfo.setEmail(simpleVendor.getEmail());
            contactInfo.setPhone(simpleVendor.getPhone());
            vendor.setContactInformation(contactInfo);
        }
        
        // Set seller type
        vendor.setSellerType(simpleVendor.getSellerType());
        
        // Create audit info
        AuditInfo auditInfo = new AuditInfo();
        auditInfo.setCreatedAt(simpleVendor.getCreatedAt());
        auditInfo.setUpdatedAt(simpleVendor.getUpdatedAt());
        auditInfo.setCreatedBy("system");
        auditInfo.setUpdatedBy("system");
        vendor.setAuditInfo(auditInfo);
        
        return vendor;
    }
}