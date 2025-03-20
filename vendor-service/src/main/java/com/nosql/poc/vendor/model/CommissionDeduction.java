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
public class CommissionDeduction {
    private String deductionType;
    private String calculationBasis;
    private BigDecimal value;
    private String applicability;
    private Map<String, Object> conditions;
}