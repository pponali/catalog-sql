package com.scaler.productread.config;

import org.springframework.core.env.Environment;

/**
 * Provider class for resolving Elasticsearch index names from properties.
 * This class helps resolve SpEL expressions in Elasticsearch @Document annotations.
 */
public class IndexNameProvider {
    
    private final Environment environment;
    
    public IndexNameProvider(Environment environment) {
        this.environment = environment;
    }
    
    /**
     * Get the index name for the given key.
     *
     * @param key The property key for the index
     * @return The resolved index name
     */
    public String getIndexName(String key) {
        return environment.getProperty(key);
    }
}
