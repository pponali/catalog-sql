package com.nosql.poc.vendor.model;

import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Data
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

@Data
public class SlaMetric {
    private String metricName;
    private String metricType;
    private String unit;
    private Double threshold;
    private String measurementPeriod;
    private String calculationMethod;
    private Integer gracePeriod;
    private String priority;
}

@Data
public class SlaViolation {
    private String violationId;
    private String metricName;
    private LocalDateTime violationTime;
    private String severity;
    private String description;
    private String status;
    private Double penaltyAmount;
    private String resolution;
}

@Data
public class PenaltyStructure {
    private String penaltyType;
    private String calculationBasis;
    private Map<String, PenaltyTier> penaltyTiers;
    private Double maxPenaltyPercentage;
    private String penaltyApplication; // PER_INCIDENT, MONTHLY, QUARTERLY
}
