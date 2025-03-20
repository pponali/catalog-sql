package com.nosql.poc.vendor.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
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