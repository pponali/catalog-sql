package com.nosql.poc.vendor.model;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Data
public class QualityMetrics {
    private String metricsPeriod; // DAILY, WEEKLY, MONTHLY, QUARTERLY
    
    private LocalDateTime lastUpdated;
    
    private BigDecimal overallScore;
    
    private List<MetricScore> scores;
    
    private Map<String, ChannelMetrics> channelMetrics;
    
    private List<QualityIncident> incidents;
    
    private Map<String, Object> additionalMetrics;
}

@Data
public class MetricScore {
    private String metricName;
    private String metricType;
    private BigDecimal score;
    private String unit;
    private BigDecimal weight;
    private String status;
    private Map<String, Object> metricDetails;
}

@Data
public class ChannelMetrics {
    private String channelId;
    private BigDecimal channelScore;
    private List<MetricScore> channelScores;
    private Map<String, Object> channelSpecificMetrics;
}

@Data
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
