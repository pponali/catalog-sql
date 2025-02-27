package com.scaler.config;

import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.beans.factory.annotation.Value;
import com.github.benmanes.caffeine.cache.Caffeine;
import java.util.concurrent.TimeUnit;

@Configuration
@EnableCaching
public class CacheConfig {

    @Value("${app.cache.feature-values.ttl:3600}")
    private long featureValuesCacheTtl;

    @Value("${app.cache.feature-values.max-size:1000}")
    private long featureValuesMaxSize;

    @Bean
    public CacheManager cacheManager() {
        CaffeineCacheManager cacheManager = new CaffeineCacheManager();
        
        // Configure different caches with their own settings
        cacheManager.setCacheNames(java.util.Arrays.asList("productFeatureValues"));
        cacheManager.setCaffeine(Caffeine.newBuilder()
                .maximumSize(featureValuesMaxSize)
                .expireAfterWrite(featureValuesCacheTtl, TimeUnit.SECONDS)
                .recordStats());

        return cacheManager;
    }
}
