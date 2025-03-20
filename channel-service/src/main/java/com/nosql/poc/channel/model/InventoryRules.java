package com.nosql.poc.channel.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Map;

/**
 * Inventory rules for products in a channel.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InventoryRules {
    private String inventoryModel;
    private Boolean allowBackorder;
    private Integer minInventoryLevel;
    private Integer maxInventoryLevel;
    private Integer bufferQuantity;
    private String updateFrequency;
    private Map<String, Object> inventoryCalculationRules;
}