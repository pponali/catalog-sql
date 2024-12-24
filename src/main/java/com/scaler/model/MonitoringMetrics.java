package com.scaler.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MonitoringMetrics {
    @Builder.Default
    private Map<String, Double> operationTimes = Map.of();
    @Builder.Default
    private Map<String, Long> errorCounts = Map.of();
    @Builder.Default
    private Map<String, Long> requestCounts = Map.of();
    @Builder.Default
    private Map<String, CacheStats> cacheStats = Map.of();
    @Builder.Default
    private List<FeatureValueEvent> eventHistory = List.of();
    @Builder.Default
    private Map<Long, LocalDateTime> lastUpdateTimes = Map.of();
    private LocalDateTime lastUpdated;
}
