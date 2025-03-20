package com.nosql.poc.channel.client;

import com.nosql.poc.channel.model.Vendor;
import com.nosql.poc.channel.model.VendorChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Fallback implementation for the VendorServiceClient.
 * This provides degraded functionality when the vendor service is unavailable.
 */
@Component
public class VendorServiceFallback implements VendorServiceClient {
    
    private static final Logger log = LoggerFactory.getLogger(VendorServiceFallback.class);
    
    @Override
    public Vendor getVendor(String vendorId) {
        log.warn("Fallback: Unable to get vendor with ID: {}", vendorId);
        return null;
    }
    
    @Override
    public List<VendorChannel> getVendorChannels(String vendorId) {
        log.warn("Fallback: Unable to get channels for vendor with ID: {}", vendorId);
        return new ArrayList<>();
    }
    
    @Override
    public VendorChannel addVendorChannel(String vendorId, VendorChannel channel) {
        log.warn("Fallback: Unable to add channel for vendor with ID: {}", vendorId);
        return null;
    }
    
    @Override
    public VendorChannel updateVendorChannel(String vendorId, String channelId, VendorChannel channel) {
        log.warn("Fallback: Unable to update channel {} for vendor with ID: {}", channelId, vendorId);
        return null;
    }
}