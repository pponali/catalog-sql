# Discovery Service

## Overview
The Discovery Service is a centralized service registry built using Netflix Eureka. It enables service-to-service communication in the microservices architecture by allowing services to register themselves and discover other services without hardcoded URLs.

## Features
- Service registration and discovery
- Health monitoring of registered services
- Dynamic service availability updates
- Self-preservation mode to handle network partitions

## Configuration
The Discovery Service runs on port 8761 (standard Eureka port) and is configured with the following options:
- Self-registration disabled (since it's the registry itself)
- Registry fetching disabled (same reason)
- Self-preservation mode disabled for development (enable in production)

## Usage
To start the Discovery Service:
```bash
./start-discovery-service.sh
```

To stop the Discovery Service:
```bash
./stop-discovery-service.sh
```

The Eureka dashboard is accessible at: http://localhost:8761

## Service Integration
Each microservice that needs to be discovered should include:

1. The Eureka client dependency:
```xml
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-netflix-eureka-client</artifactId>
</dependency>
```

2. Enable discovery client in the main application class:
```java
@SpringBootApplication
@EnableDiscoveryClient
public class YourServiceApplication {
    // ...
}
```

3. Configure Eureka client in application.yaml/properties:
```yaml
eureka:
  client:
    serviceUrl:
      defaultZone: http://localhost:8761/eureka/
    register-with-eureka: true
    fetch-registry: true
  instance:
    preferIpAddress: true
```

## Microservices Architecture
The Discovery Service is part of a larger microservices ecosystem with the following services:

1. Discovery Service (this service)
2. Validation Service
3. Rules Service
4. Vendor Service 
5. Channel Service
6. Partner Service
7. Catalog Service

These services should be started in the order listed above to ensure proper dependency resolution.