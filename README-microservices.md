# Microservices Architecture - Data Distribution

This document outlines how data is distributed across the microservices in the catalog system.

## Services Overview

The system is divided into the following microservices:

1. **catalog-service**: Core product and category management
2. **vendor-service**: Merchant and seller management
3. **channel-service**: Channel, platform, and store management
4. **validation-service**: Data validation rules
5. **rules-service**: Business rules and workflows
6. **business-service**: Partner/business integration

## Data Distribution

Each service is responsible for managing its own data domain:

### catalog-service (PostgreSQL)
- Products
- Categories
- Feature Templates
- Product Features
- Product-Category Mappings
- Units of Measure

### vendor-service (MongoDB)
- Merchants
- Sellers

### channel-service (MongoDB)
- Channels
- Platforms
- Stores

### validation-service (PostgreSQL)
- Validation Rules
- Validation Rule Mappings

### rules-service (PostgreSQL)
- Business Rules
- Workflow Definitions

## Data Import Endpoints

Each service provides its own data import endpoints to load data from CSV files:

### catalog-service
- POST /import/categories
- POST /import/feature-templates
- POST /import/units-of-measure
- POST /import/products
- POST /import/product-features
- POST /import/category-feature-templates
- POST /import/product-category-mappings
- POST /import/all (imports all catalog-related data)

### vendor-service
- POST /import/merchants
- POST /import/sellers
- POST /import/all (imports all vendor-related data)

### channel-service
- POST /import/channels
- POST /import/platforms
- POST /import/stores
- POST /import/all (imports all channel-related data)

## Service Dependencies

The services should be started in the following order:
1. validation-service
2. vendor-service
3. rules-service
4. channel-service
5. business-service
6. catalog-service

This ensures that dependent services are available when needed.

## Inter-service Communication

Services communicate via gRPC for efficient, strongly-typed communication. Each service exposes a gRPC interface defined in proto files.

- catalog-service depends on: validation-service, vendor-service, channel-service
- channel-service depends on: validation-service, vendor-service
- vendor-service depends on: validation-service

## Running the System

Use the `start-services.sh` script to build and start all services in the correct dependency order:

```bash
./start-services.sh
```

Before starting the services, make sure to set up the databases using:

```bash
./setup-databases.sh
```