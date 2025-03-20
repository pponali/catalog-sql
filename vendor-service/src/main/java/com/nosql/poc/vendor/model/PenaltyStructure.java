package com.nosql.poc.vendor.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PenaltyStructure {
    private String penaltyType;
    private String calculationBasis;
    private Map<String, PenaltyTier> penaltyTiers;
    private Double maxPenaltyPercentage;
    private String penaltyApplication; // PER_INCIDENT, MONTHLY, QUARTERLY
}