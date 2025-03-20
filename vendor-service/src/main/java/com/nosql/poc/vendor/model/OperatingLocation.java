package com.nosql.poc.vendor.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OperatingLocation {
    private String locationId;
    
    private String locationType; // WAREHOUSE, STORE, OFFICE
    
    private Address address;
    
    private ContactPerson locationManager;
    
    private List<OperatingHours> operatingHours;
    
    private List<String> serviceableAreas;
    
    private DeliveryCapabilities deliveryCapabilities;
    
    private StorageCapabilities storageCapabilities;
    
    private Boolean active;
    
    private Map<String, Object> locationAttributes;
}
