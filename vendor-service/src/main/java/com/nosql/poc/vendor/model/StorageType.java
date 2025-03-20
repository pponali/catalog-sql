package com.nosql.poc.vendor.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StorageType {
    private String type; // AMBIENT, COLD_STORAGE, FROZEN
    private Double capacity;
    private String capacityUnit;
    private Boolean temperatureControlled;
    private Double minTemperature;
    private Double maxTemperature;
}