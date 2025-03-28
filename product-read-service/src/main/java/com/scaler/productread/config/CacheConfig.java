package com.scaler.productread.config;

import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;

/**
 * Configuration for caching using Caffeine.
 * Sets up two cache regions:
 * 1. Product cache - for individual product entities
 * 2. Query cache - for query results
 */
@Configuration
@EnableCaching
public class CacheConfig {
    
    @Value("${cache.product.ttl:3600}")
    private Integer productCacheTtl;
    
    @Value("${cache.product.maxSize:1000}")
    private Integer productCacheMaxSize;
    
    @Value("${cache.query.ttl:300}")
    private Integer queryCacheTtl;
    
    @Value("${cache.query.maxSize:200}")
    private Integer queryCacheMaxSize;
    
    /**
     * Primary cache manager for product entities.
     *
     * @return CacheManager for product cache
     */
    @Bean
    @Primary
    public CacheManager productCacheManager() {
        CaffeineCacheManager cacheManager = new CaffeineCacheManager("products", "product");
        cacheManager.setCaffeine(Caffeine.newBuilder()
                .maximumSize(productCacheMaxSize)
                .expireAfterWrite(productCacheTtl, TimeUnit.SECONDS)
                .recordStats());
        return cacheManager;
    }
    
    /**
     * Cache manager for query results.
     *
     * @return CacheManager for query cache
     */
    @Bean
    public CacheManager queryCacheManager() {
        CaffeineCacheManager cacheManager = new CaffeineCacheManager(
                "productQueries", 
                "categoryQueries", 
                "brandQueries", 
                "searchQueries", 
                "priceQueries", 
                "sellerQueries", 
                "channelQueries", 
                "featureQueries");
        
        cacheManager.setCaffeine(Caffeine.newBuilder()
                .maximumSize(queryCacheMaxSize)
                .expireAfterWrite(queryCacheTtl, TimeUnit.SECONDS)
                .recordStats());
        return cacheManager;
    }
}