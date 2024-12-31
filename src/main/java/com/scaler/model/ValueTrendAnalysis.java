package com.scaler.model;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Map;

@Data
@NoArgsConstructor
public class ValueTrendAnalysis {
    
    private Long featureId;
    private String interval;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    
    private Map<String, Double> numericTrends;
    private Map<String, Long> categoryDistribution;
    private Map<String, Double> percentageChanges;
    
    private double minValue;
    private double maxValue;
    private double avgValue;
    private double standardDeviation;
    
    private long totalCount;
    private long uniqueValuesCount;
    
    private Map<String, Object> customMetrics;

    @Builder(builderClassName = "ValueTrendAnalysisBuilder")
    public ValueTrendAnalysis(Long featureId, String interval, LocalDateTime startDate, LocalDateTime endDate,
                            Map<String, Double> numericTrends, Map<String, Long> categoryDistribution,
                            Map<String, Double> percentageChanges, double minValue, double maxValue,
                            double avgValue, double standardDeviation, long totalCount,
                            long uniqueValuesCount, Map<String, Object> customMetrics) {
        this.featureId = featureId;
        this.interval = interval;
        this.startDate = startDate;
        this.endDate = endDate;
        this.numericTrends = numericTrends;
        this.categoryDistribution = categoryDistribution;
        this.percentageChanges = percentageChanges;
        this.minValue = minValue;
        this.maxValue = maxValue;
        this.avgValue = avgValue;
        this.standardDeviation = standardDeviation;
        this.totalCount = totalCount;
        this.uniqueValuesCount = uniqueValuesCount;
        this.customMetrics = customMetrics;
    }
    
    @Data
    @NoArgsConstructor
    public static class TimeSeriesPoint {
        private LocalDateTime timestamp;
        private double value;
        private String category;
        private Map<String, Object> metadata;

        @Builder(builderClassName = "TimeSeriesPointBuilder")
        public TimeSeriesPoint(LocalDateTime timestamp, double value, String category, Map<String, Object> metadata) {
            this.timestamp = timestamp;
            this.value = value;
            this.category = category;
            this.metadata = metadata;
        }
    }
}
