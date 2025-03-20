package com.nosql.poc.vendor.service;

import com.nosql.poc.vendor.model.Vendor;
import java.util.List;
import java.util.Optional;

public interface VendorService {
    List<Vendor> getAllVendors();
    
    Optional<Vendor> getVendorById(String id);
    
    Vendor createVendor(Vendor vendor);
    
    Vendor updateVendor(String id, Vendor vendor);
    
    void deleteVendor(String id);
    
    void handleChannelUpdate(String channelId);
    
    void handleProductUpdate(String productId, String vendorId);
    
    void syncProductToChannel(String productId, String vendorId, String channelId);
}