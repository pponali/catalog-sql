package com.nosql.poc.vendor.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommissionTier {
    private BigDecimal fromAmount;
    private BigDecimal toAmount;
    private BigDecimal commissionPercentage;
    private String applicability; // PER_ORDER, MONTHLY_GMV
    private Map<String, Object> conditions;
}