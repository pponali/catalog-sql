package com.nosql.poc.vendor.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ServiceLevelAgreement {
    private String slaId;
    
    private String slaVersion;
    
    private LocalDateTime effectiveFrom;
    
    private LocalDateTime effectiveTo;
    
    private List<SlaMetric> metrics;
    
    private List<SlaViolation> violations;
    
    private PenaltyStructure penaltyStructure;
    
    private Map<String, Object> additionalTerms;
}
