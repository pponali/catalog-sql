package com.scaler.productread.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import graphql.scalars.ExtendedScalars;
import graphql.schema.GraphQLScalarType;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.graphql.execution.RuntimeWiringConfigurer;

/**
 * Configuration for GraphQL.
 * Adds custom scalar types and other GraphQL-specific configurations.
 */
@Configuration
public class GraphQLConfig {
    
    private final ObjectMapper objectMapper;
    
    public GraphQLConfig(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }
    
    /**
     * Register custom scalar types with GraphQL.
     */
    @Bean
    public RuntimeWiringConfigurer runtimeWiringConfigurer() {
        return wiringBuilder -> wiringBuilder
                .scalar(jsonScalar());
    }
    
    /**
     * Create a JSON scalar type for GraphQL.
     */
    @Bean
    public GraphQLScalarType jsonScalar() {
        return ExtendedScalars.Json;
    }
}