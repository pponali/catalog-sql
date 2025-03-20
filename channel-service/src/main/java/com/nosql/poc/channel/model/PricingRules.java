package com.nosql.poc.channel.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;
import java.util.Map;

/**
 * Pricing rules for products in a channel.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PricingRules {
    private String pricingModel;
    private String currency;
    private Double minPrice;
    private Double maxPrice;
    private Integer decimalPlaces;
    private List<String> allowedPricePoints;
    private Map<String, Object> priceCalculationRules;
}