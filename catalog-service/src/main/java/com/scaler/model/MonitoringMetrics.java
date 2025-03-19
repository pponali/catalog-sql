package com.scaler.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MonitoringMetrics {
    private UUID featureId;
    private long totalValues;
    private long validValues;
    
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
    private Map<UUID, LocalDateTime> lastUpdateTimes = Map.of();
    private LocalDateTime lastUpdated;
    private LocalDateTime lastUpdateTime;
}
