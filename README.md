# Catalog System Microservices Architecture

This project implements a microservices-based catalog system architecture with proper domain boundaries and independent services.

## Architecture Overview

The system is divided into several microservices, each responsible for a specific domain:

1. **catalog-service**: Core product and category management
   - Products, Categories, Features, UnitOfMeasure
   - Uses PostgreSQL database

2. **vendor-service**: Merchant and seller management
   - Merchants, Sellers
   - Uses MongoDB database

3. **channel-service**: Channel, platform, and store management
   - Channels, Platforms, Stores
   - Uses MongoDB database

4. **validation-service**: Data validation rules (not fully implemented)
5. **rules-service**: Business rules and workflows (not fully implemented)
6. **business-service**: Partner/business integration (not fully implemented)

## Data Distribution

Each service manages its own domain data and provides endpoints for data import:

### catalog-service
- Categories
- Feature Templates
- Product Features
- Units of Measure
- Products
- Product-Category Mappings

### vendor-service
- Merchants
- Sellers

### channel-service
- Channels
- Platforms
- Stores

## Running the Services

### Prerequisites
- Java 17+
- Maven
- PostgreSQL (for catalog-service)
- MongoDB (for vendor-service and channel-service)

### Setup and Configuration

1. **Configure the service properties**:
   
   Each service has its own `application.yaml` file in `src/main/resources/` with database connection details.

2. **Start the services**:

   ```bash
   # Check the service endpoints
   ./start-microservices.sh
   
   # If you want to start all services (requires PostgreSQL setup)
   ./start-services.sh
   ```

3. **Import data**:

   Use the following endpoints to import data:

   **catalog-service (port 8080)**:
   - POST http://localhost:8080/import/categories
   - POST http://localhost:8080/import/feature-templates
   - POST http://localhost:8080/import/units-of-measure
   - POST http://localhost:8080/import/products
   - POST http://localhost:8080/import/product-features
   - POST http://localhost:8080/import/all

   **vendor-service (port 8081)**:
   - POST http://localhost:8081/import/merchants
   - POST http://localhost:8081/import/sellers
   - POST http://localhost:8081/import/all

   **channel-service (port 8082)**:
   - POST http://localhost:8082/import/channels
   - POST http://localhost:8082/import/platforms
   - POST http://localhost:8082/import/stores
   - POST http://localhost:8082/import/all

## Service Implementation Details

### catalog-service

Core service for managing products, categories, and their features.

- **Data Model**: JPA entities for products, categories, features, etc.
- **Database**: PostgreSQL
- **API**: REST endpoints for CRUD operations and data import

### vendor-service

Service for managing merchants and sellers.

- **Data Model**: MongoDB documents for merchants and sellers
- **Database**: MongoDB
- **API**: REST endpoints for CRUD operations and data import

### channel-service

Service for managing channels, platforms, and stores.

- **Data Model**: MongoDB documents for channels, platforms, and stores
- **Database**: MongoDB
- **API**: REST endpoints for CRUD operations and data import

## Inter-service Communication

The services communicate via gRPC for efficient, strongly-typed communication. Each service exposes a gRPC interface defined in proto files.

## Data Import

Each service provides data import functionality tailored to its domain:

1. The CSV files are stored in each service's resources directory
2. Each service has its own `LoadDataFromCsvService` to handle importing data
3. Import endpoints are exposed via REST controllers

## Development and Testing

To add a new feature:

1. Identify which service the feature belongs to
2. Implement the feature within that service
3. If cross-service communication is needed, use gRPC interfaces

To test a service:

1. Start the service using the provided scripts
2. Use Postman or curl to interact with the service's REST endpoints

## Next Steps

Future enhancements planned for the system:

1. Complete implementation of validation-service, rules-service, and business-service
2. Add comprehensive monitoring and observability
3. Implement service discovery using Consul or Eureka
4. Add API gateway for external access
5. Implement data synchronization between services
