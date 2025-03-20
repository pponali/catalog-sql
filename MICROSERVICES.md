# Microservices Setup Guide

This document provides instructions on how to set up and run the microservices architecture for the catalog application.

## Prerequisites

1. MongoDB (installed and running)
2. Java 17 or higher
3. Maven
4. Bash-compatible shell

## Services

This application consists of the following microservices:

1. **validation-service**: Provides enhanced validation rules with cross-field validation capabilities
2. **vendor-service**: Manages vendor and merchant data
3. **rules-service**: Manages business rules and workflows
4. **channel-service**: Handles multichannel capabilities (web, mobile, physical stores)
5. **business-service**: Manages business partner integration
6. **catalog-service**: Core catalog functionality (depends on all other services)

## Starting the Services

To start all microservices:

```bash
./start-services.sh
```

This script will:
1. Check if MongoDB is running and start it if necessary
2. Set up databases for all services
3. Build all services using Maven
4. Start each service in the appropriate order

Each service will start in the background, and logs will be written to `service-name.log` files in the project root.

## Verifying Service Status

To check if services are running:

```bash
ps aux | grep java
```

Or check individual service logs:

```bash
tail -f validation-service.log
```

## Stopping the Services

To stop all microservices:

```bash
./stop-services.sh
```

This script will gracefully shut down all running services and optionally stop MongoDB as well.

## Enhanced Validation Rules

The validation service now includes a more sophisticated rule engine with:

1. Cross-field validation (relationships between fields)
2. Conditional rules (if-then-else logic) 
3. Calculated field validation
4. Rule dependencies and precedence
5. JavaScript-based custom validations

### Creating a New Validation Rule

To create a new validation rule, you can use the MongoDB client to insert rules into the database:

```javascript
db.enhanced_validation_rules.insertOne({
  ruleId: "PRODUCT_NAME_LENGTH",
  name: "Product Name Length",
  description: "Product name must be between 3 and 100 characters",
  entityType: "PRODUCT",
  primaryAttribute: "name",
  conditionType: "CONDITIONAL",
  conditionExpression: "name != null",
  conditionalExpressions: {
    then: "name.length() >= 3 && name.length() <= 100"
  },
  message: "Product name must be between 3 and 100 characters",
  severity: "ERROR",
  priority: 10,
  active: true
})
```

## Known Issues

- If a service fails to start, check its log file for errors.
- MongoDB connection issues can occur if the MongoDB service is not running properly.

## Troubleshooting

If you experience issues:

1. Check service logs for errors
2. Ensure MongoDB is running properly
3. Verify that application properties are correctly configured
4. Try restarting the services
5. Check for port conflicts if services fail to start

For more detailed diagnostics, use:

```bash
mvn spring-boot:run -Ddebug=true
```

inside the specific service directory to enable debug output.