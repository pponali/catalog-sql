package com.nosql.poc.vendor.model;

import lombok.Data;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;

@Data
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

@Data
public class OperatingHours {
    private String dayOfWeek;
    private LocalTime openTime;
    private LocalTime closeTime;
    private Boolean isHoliday;
    private String shiftType;
}

@Data
public class DeliveryCapabilities {
    private List<String> deliveryModes;
    private Double maxDeliveryRadius;
    private List<String> serviceablePincodes;
    private Integer maxOrdersPerDay;
    private Map<String, Object> deliveryConstraints;
}

@Data
public class StorageCapabilities {
    private Double totalArea;
    private String areaUnit;
    private Integer totalRacks;
    private Integer totalBins;
    private List<StorageType> storageTypes;
    private Map<String, Object> storageConstraints;
}

@Data
public class StorageType {
    private String type; // AMBIENT, COLD_STORAGE, FROZEN
    private Double capacity;
    private String capacityUnit;
    private Boolean temperatureControlled;
    private Double minTemperature;
    private Double maxTemperature;
}
