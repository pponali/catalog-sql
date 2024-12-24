package com.scaler.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CacheStats {
    private long hits;
    private long misses;
    private long evictions;
    private long size;
    private LocalDateTime lastEvictionTime;
    private LocalDateTime lastUpdateTime;
    private double hitRate;
    private double missRate;
    private LocalDateTime lastRefreshed;
}
