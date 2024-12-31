package com.scaler.service.cache;

import com.scaler.dto.ProductFeatureValueDTO;
import com.scaler.model.CacheStats;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Service
@RequiredArgsConstructor
public class FeatureValueCacheService {

    private final CacheManager cacheManager;
    private final Map<String, AtomicLong> cacheHits = new ConcurrentHashMap<>();
    private final Map<String, AtomicLong> cacheMisses = new ConcurrentHashMap<>();
    private final Map<String, LocalDateTime> lastEvictionTime = new ConcurrentHashMap<>();

    @Cacheable(value = "featureValues", key = "#featureId + '_' + #valueId")
    public ProductFeatureValueDTO getCachedValue(Long featureId, Long valueId) {
        incrementMisses("featureValues");
        return null; // actual value will be loaded by the cache
    }

    @CacheEvict(value = "featureValues", key = "#featureId + '_' + #valueId")
    public void evictCachedValue(Long featureId, Long valueId) {
        lastEvictionTime.put("featureValues", LocalDateTime.now());
    }

    @CacheEvict(value = "featureValues", allEntries = true)
    public void evictAllCachedValues() {
        lastEvictionTime.put("featureValues", LocalDateTime.now());
    }

    @Scheduled(fixedRate = 3600000) // Every hour
    public void evictExpiredEntries() {
        cacheManager.getCacheNames().forEach(cacheName -> {
            cacheManager.getCache(cacheName).clear();
            lastEvictionTime.put(cacheName, LocalDateTime.now());
        });
    }

    public void recordCacheHit(String cacheName) {
        cacheHits.computeIfAbsent(cacheName, k -> new AtomicLong()).incrementAndGet();
    }

    private void incrementMisses(String cacheName) {
        cacheMisses.computeIfAbsent(cacheName, k -> new AtomicLong()).incrementAndGet();
    }

    public CacheStats getCacheStats(String cacheName) {
        return CacheStats.builder()
                .hits(cacheHits.getOrDefault(cacheName, new AtomicLong()).get())
                .misses(cacheMisses.getOrDefault(cacheName, new AtomicLong()).get())
                .lastEvictionTime(lastEvictionTime.get(cacheName))
                .build();
    }

    public Map<String, CacheStats> getAllCacheStats() {
        Map<String, CacheStats> stats = new HashMap<>();
        cacheManager.getCacheNames().forEach(cacheName -> 
            stats.put(cacheName, getCacheStats(cacheName))
        );
        return stats;
    }
}
