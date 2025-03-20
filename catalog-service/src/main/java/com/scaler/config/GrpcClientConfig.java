package com.scaler.config;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.client.inject.GrpcClient;
import net.devh.boot.grpc.client.inject.GrpcClientBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PreDestroy;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Slf4j
@Configuration
@GrpcClientBean(
    clazz = GrpcClientConfig.class,
    beanName = "validationServiceChannel",
    client = @GrpcClient("validation-service")
)
public class GrpcClientConfig {

    @Value("${grpc.client.validation-service.address:validation-service}")
    private String validationServiceAddress;
    
    @Value("${grpc.client.vendor-service.address:vendor-service}")
    private String vendorServiceAddress;
    
    @Value("${grpc.client.channel-service.address:channel-service}")
    private String channelServiceAddress;
    
    @Value("${grpc.client.partner-service.address:partner-service}")
    private String partnerServiceAddress;
    
    @Value("${grpc.client.rules-service.address:rules-service}")
    private String rulesServiceAddress;
    
    private final DiscoveryClient discoveryClient;
    
    private ManagedChannel validationServiceChannel;
    private ManagedChannel vendorServiceChannel;
    private ManagedChannel channelServiceChannel;
    private ManagedChannel partnerServiceChannel;
    private ManagedChannel rulesServiceChannel;
    
    public GrpcClientConfig(DiscoveryClient discoveryClient) {
        this.discoveryClient = discoveryClient;
    }

    @Bean
    public ManagedChannel validationServiceChannel() {
        // Use discovery client to find service using Eureka
        log.info("Creating gRPC channel to validation service using service discovery");
        
        // For local development fallback if service discovery doesn't find the service
        String host = "localhost";
        int port = 9090;  // Default gRPC port for validation service
        
        try {
            List<ServiceInstance> instances = discoveryClient.getInstances("validation-service");
            if (!instances.isEmpty()) {
                ServiceInstance instance = instances.get(0);
                host = instance.getHost();
                port = instance.getPort();
                log.info("Found validation-service instance at {}:{}", host, port);
            } else {
                log.warn("No validation-service instances found in service registry, using default: {}:{}", host, port);
            }
        } catch (Exception e) {
            log.error("Error discovering validation-service: {}", e.getMessage());
        }
        
        validationServiceChannel = ManagedChannelBuilder.forAddress(host, port)
                .usePlaintext()
                .enableRetry()
                .maxRetryAttempts(3)
                .build();
        return validationServiceChannel;
    }
    
    @Bean
    public ManagedChannel vendorServiceChannel() {
        // Use discovery client to find service
        log.info("Creating gRPC channel to vendor service using service discovery");
        
        // For local development fallback
        String host = "localhost";
        int port = 9091;  // Default gRPC port for vendor service
        
        try {
            List<ServiceInstance> instances = discoveryClient.getInstances("vendor-service");
            if (!instances.isEmpty()) {
                ServiceInstance instance = instances.get(0);
                host = instance.getHost();
                port = instance.getPort();
                log.info("Found vendor-service instance at {}:{}", host, port);
            } else {
                log.warn("No vendor-service instances found in service registry, using default: {}:{}", host, port);
            }
        } catch (Exception e) {
            log.error("Error discovering vendor-service: {}", e.getMessage());
        }
        
        vendorServiceChannel = ManagedChannelBuilder.forAddress(host, port)
                .usePlaintext()
                .enableRetry()
                .maxRetryAttempts(3)
                .build();
        return vendorServiceChannel;
    }
    
    @Bean
    public ManagedChannel channelServiceChannel() {
        // Use discovery client to find service
        log.info("Creating gRPC channel to channel service using service discovery");
        
        // For local development fallback
        String host = "localhost";
        int port = 9092;  // Default gRPC port for channel service
        
        try {
            List<ServiceInstance> instances = discoveryClient.getInstances("channel-service");
            if (!instances.isEmpty()) {
                ServiceInstance instance = instances.get(0);
                host = instance.getHost();
                port = instance.getPort();
                log.info("Found channel-service instance at {}:{}", host, port);
            } else {
                log.warn("No channel-service instances found in service registry, using default: {}:{}", host, port);
            }
        } catch (Exception e) {
            log.error("Error discovering channel-service: {}", e.getMessage());
        }
        
        channelServiceChannel = ManagedChannelBuilder.forAddress(host, port)
                .usePlaintext()
                .enableRetry()
                .maxRetryAttempts(3)
                .build();
        return channelServiceChannel;
    }
    
    @Bean
    public ManagedChannel partnerServiceChannel() {
        // Use discovery client to find service
        log.info("Creating gRPC channel to partner service using service discovery");
        
        // For local development fallback
        String host = "localhost";
        int port = 9093;  // Default gRPC port for partner service
        
        try {
            List<ServiceInstance> instances = discoveryClient.getInstances("partner-service");
            if (!instances.isEmpty()) {
                ServiceInstance instance = instances.get(0);
                host = instance.getHost();
                port = instance.getPort();
                log.info("Found partner-service instance at {}:{}", host, port);
            } else {
                log.warn("No partner-service instances found in service registry, using default: {}:{}", host, port);
            }
        } catch (Exception e) {
            log.error("Error discovering partner-service: {}", e.getMessage());
        }
        
        partnerServiceChannel = ManagedChannelBuilder.forAddress(host, port)
                .usePlaintext()
                .enableRetry()
                .maxRetryAttempts(3)
                .build();
        return partnerServiceChannel;
    }
    
    @Bean
    public ManagedChannel rulesServiceChannel() {
        // Use discovery client to find service
        log.info("Creating gRPC channel to rules service using service discovery");
        
        // For local development fallback
        String host = "localhost";
        int port = 9094;  // Default gRPC port for rules service
        
        try {
            List<ServiceInstance> instances = discoveryClient.getInstances("rules-service");
            if (!instances.isEmpty()) {
                ServiceInstance instance = instances.get(0);
                host = instance.getHost();
                port = instance.getPort();
                log.info("Found rules-service instance at {}:{}", host, port);
            } else {
                log.warn("No rules-service instances found in service registry, using default: {}:{}", host, port);
            }
        } catch (Exception e) {
            log.error("Error discovering rules-service: {}", e.getMessage());
        }
        
        rulesServiceChannel = ManagedChannelBuilder.forAddress(host, port)
                .usePlaintext()
                .enableRetry()
                .maxRetryAttempts(3)
                .build();
        return rulesServiceChannel;
    }
    
    @PreDestroy
    public void cleanUp() {
        log.info("Shutting down gRPC channels");
        
        try {
            if (validationServiceChannel != null) {
                validationServiceChannel.shutdown().awaitTermination(5, TimeUnit.SECONDS);
            }
            if (vendorServiceChannel != null) {
                vendorServiceChannel.shutdown().awaitTermination(5, TimeUnit.SECONDS);
            }
            if (channelServiceChannel != null) {
                channelServiceChannel.shutdown().awaitTermination(5, TimeUnit.SECONDS);
            }
            if (partnerServiceChannel != null) {
                partnerServiceChannel.shutdown().awaitTermination(5, TimeUnit.SECONDS);
            }
            if (rulesServiceChannel != null) {
                rulesServiceChannel.shutdown().awaitTermination(5, TimeUnit.SECONDS);
            }
        } catch (InterruptedException e) {
            log.error("Error shutting down gRPC channels: {}", e.getMessage());
            Thread.currentThread().interrupt();
        }
    }
}