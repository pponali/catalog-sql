package com.scaler.config;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cache.CacheManager;
import org.springframework.cache.caffeine.CaffeineCacheManager;

import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class CacheConfigTest {

    @Autowired
    private CacheManager cacheManager;

    @Test
    void testCacheManagerConfiguration() {
        // Verify that we get a CaffeineCacheManager
        assertTrue(cacheManager instanceof CaffeineCacheManager,
                "Cache manager should be an instance of CaffeineCacheManager");
    }

    @Test
    void testCacheCreation() {
        // Create a test cache
        org.springframework.cache.Cache testCache = cacheManager.getCache("testCache");
        
        assertNotNull(testCache, "Cache should be created successfully");
        
        // Test cache operations
        testCache.put("testKey", "testValue");
        assertEquals("testValue", testCache.get("testKey", String.class),
                "Cache should store and retrieve values correctly");
    }

    @Test
    void testCacheEviction() throws InterruptedException {
        // Create a test cache with same configuration as in CacheConfig
        Cache<String, String> cache = Caffeine.newBuilder()
                .maximumSize(500)
                .expireAfterWrite(1, TimeUnit.SECONDS) // Using 1 second for testing
                .build();

        // Put a test value
        cache.put("testKey", "testValue");
        
        // Verify value is present
        assertNotNull(cache.getIfPresent("testKey"),
                "Value should be present immediately after putting");
        
        // Wait for expiration
        Thread.sleep(1100); // Wait slightly more than 1 second
        
        // Verify value has been evicted
        assertNull(cache.getIfPresent("testKey"),
                "Value should be evicted after expiration time");
    }

    @Test
    void testCacheMaximumSize() {
        // Create a test cache with same max size as in CacheConfig
        Cache<String, String> cache = Caffeine.newBuilder()
                .maximumSize(500)
                .build();

        // Add 501 entries
        for (int i = 0; i < 501; i++) {
            cache.put("key" + i, "value" + i);
        }

        // Wait for eviction
        cache.cleanUp();

        // Count entries
        long size = cache.estimatedSize();
        assertTrue(size <= 500,
                "Cache size should not exceed maximum size of 500");
    }
}
