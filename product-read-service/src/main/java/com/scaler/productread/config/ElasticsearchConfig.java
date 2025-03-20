package com.scaler.productread.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.client.ClientConfiguration;
import org.springframework.data.elasticsearch.client.elc.ElasticsearchConfiguration;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;
import org.springframework.core.env.Environment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;

import java.time.Duration;

/**
 * Configuration for Elasticsearch client.
 */
@Configuration
@EnableElasticsearchRepositories(basePackages = "com.scaler.productread.repository")
public class ElasticsearchConfig extends ElasticsearchConfiguration {
    
    @Value("${spring.data.elasticsearch.client.reactive.endpoints:localhost:9200}")
    private String elasticsearchEndpoints;
    
    @Value("${spring.data.elasticsearch.client.reactive.connection-timeout:5s}")
    private String connectionTimeout;
    
    @Value("${spring.data.elasticsearch.client.reactive.socket-timeout:30s}")
    private String socketTimeout;
    
    @Autowired
    private Environment environment;
    
    @Override
    public ClientConfiguration clientConfiguration() {
        return ClientConfiguration.builder()
                .connectedTo(elasticsearchEndpoints)
                .withConnectTimeout(Duration.parse("PT" + connectionTimeout))
                .withSocketTimeout(Duration.parse("PT" + socketTimeout))
                .build();
    }
    
    /**
     * Provides custom name resolver for SpEL expressions in document index names.
     */
    @Bean
    public IndexNameProvider indexNameProvider() {
        return new IndexNameProvider(environment);
    }
}