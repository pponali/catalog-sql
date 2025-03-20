package com.nosql.poc.vendor.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;
import java.math.BigDecimal;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SettlementDeduction {
    private String deductionType; // TDS, COMMISSION, PENALTY
    private BigDecimal value;
    private String calculationBasis;
    private Boolean isAutoDeducted;
    private Map<String, Object> deductionRules;
}