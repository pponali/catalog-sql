package com.nosql.poc.vendor.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChannelMetrics {
    private String channelId;
    private BigDecimal channelScore;
    private List<MetricScore> channelScores;
    private Map<String, Object> channelSpecificMetrics;
}