package com.nosql.poc.vendor.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class QualityIncident {
    private String incidentId;
    private String incidentType;
    private LocalDateTime incidentTime;
    private String severity;
    private String status;
    private String resolution;
    private BigDecimal impactScore;
    private Map<String, Object> incidentDetails;
}