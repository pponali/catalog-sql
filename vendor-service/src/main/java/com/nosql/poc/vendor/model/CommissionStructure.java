package com.nosql.poc.vendor.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommissionStructure {
    private String structureId;
    private String structureType; // FIXED, TIERED, CATEGORY_BASED
    private LocalDateTime effectiveFrom;
    private LocalDateTime effectiveTo;
    private List<CommissionTier> commissionTiers;
    private Map<String, CategoryCommission> categoryCommissions;
    private List<CommissionDeduction> deductions;
    private Map<String, Object> additionalTerms;
}
