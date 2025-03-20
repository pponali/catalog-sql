package com.nosql.poc.vendor.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;
import java.util.List;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StorageCapabilities {
    private Double totalArea;
    private String areaUnit;
    private Integer totalRacks;
    private Integer totalBins;
    private List<StorageType> storageTypes;
    private Map<String, Object> storageConstraints;
}