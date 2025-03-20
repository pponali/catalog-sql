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
public class MetricScore {
    private String metricName;
    private String metricType;
    private BigDecimal score;
    private String unit;
    private BigDecimal weight;
    private String status;
    private Map<String, Object> metricDetails;
}