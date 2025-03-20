package com.scaler.config;

import io.grpc.Server;
import io.grpc.ServerBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;

@Slf4j
@Configuration
public class GrpcServerConfig {

    @Value("${grpc.server.port}")
    private int grpcServerPort;

    @Bean
    public Server grpcServer(com.scaler.grpc.service.ProductGrpcService productGrpcService) {
        return ServerBuilder.forPort(grpcServerPort)
                .addService(productGrpcService)
                .build();
    }

    @Bean
    public Server startGrpcServer(Server grpcServer) throws IOException {
        log.info("Starting gRPC server on port {}", grpcServerPort);
        grpcServer.start();
        
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            log.info("Shutting down gRPC server");
            grpcServer.shutdown();
        }));
        
        return grpcServer;
    }
}