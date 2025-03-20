package com.nosql.poc.vendor.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
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