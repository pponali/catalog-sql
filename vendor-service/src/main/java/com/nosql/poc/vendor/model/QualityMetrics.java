package com.nosql.poc.vendor.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class QualityMetrics {
    private String metricsPeriod; // DAILY, WEEKLY, MONTHLY, QUARTERLY
    
    private LocalDateTime lastUpdated;
    
    private BigDecimal overallScore;
    
    private List<MetricScore> scores;
    
    private Map<String, ChannelMetrics> channelMetrics;
    
    private List<QualityIncident> incidents;
    
    private Map<String, Object> additionalMetrics;
}
