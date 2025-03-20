# Enhanced Validation Service

This is a microservice for validating product data in the e-commerce platform.

The Enhanced Validation Service provides a sophisticated rule engine that supports complex validation scenarios like:

- Cross-field validation
- Conditional validation
- Calculated field validation
- Rule dependencies and precedence
- JavaScript expression evaluation

## Current Status

✅ The service is now successfully running and ready for testing!

## Getting Started

### Prerequisites

- JDK 17 or later
- Maven 3.6+
- MongoDB

### Running the Service

Use the provided scripts to start and stop the service:

```bash
# Start the service
./start-validation-service.sh

# Stop the service
./stop-validation-service.sh
```

### Testing the Service

The service includes a Python test script that demonstrates various validation scenarios:

```bash
# Install required Python package
pip install requests

# Run the test script
python test-validation.py
```

This will test the following scenarios:
1. Getting all enhanced validation rules
2. Validating a valid product
3. Validating a product with a name that's too short
4. Testing cross-field validation with an invalid discount (discount price > regular price)
5. Testing calculated field validation with an unusually high discount percentage

### Environment Variables

You can configure the service using the following environment variables:

- `SPRING_DATA_MONGODB_HOST`: MongoDB host (default: localhost)
- `SPRING_DATA_MONGODB_PORT`: MongoDB port (default: 27017)
- `SPRING_DATA_MONGODB_DATABASE`: MongoDB database name (default: validation_service)
- `SERVER_PORT`: HTTP port for the service (default: 8085)

## Validation Rules

The service supports two types of validation rules:

1. **Simple Validation Rules**: Basic rules for field validation
2. **Enhanced Validation Rules**: Complex rules with support for dependencies, conditions, and cross-field validations

### Rule Condition Types

- `NOT_NULL`: Validates that a field is not null
- `REGEX`: Validates a field against a regular expression
- `MIN_VALUE` / `MAX_VALUE`: Validates numeric values against min/max thresholds
- `CONDITIONAL`: Applies validation only if certain conditions are met
- `CROSS_FIELD`: Validates relationships between multiple fields
- `CALCULATED`: Uses calculated values for validation
- `DEPENDENT`: Rules that depend on other rules
- `COMPOSITE`: Combines multiple rules

## API Documentation

The service provides comprehensive API documentation through Swagger/OpenAPI:

- **Swagger UI**: http://localhost:8085/swagger-ui.html
- **OpenAPI Spec**: http://localhost:8085/api-docs

### Validation Endpoints

- `POST /api/validation/entity/{entityType}`: Validate entity using simple rules
- `POST /api/validation/entity/{entityType}/enhanced`: Validate entity using enhanced rules
- `POST /api/validation/product`: Validate a product
- `POST /api/validation/category`: Validate a category

### Rule Management Endpoints

- `GET /api/validation/rules`: Get all validation rules
- `GET /api/validation/rules/enhanced`: Get all enhanced validation rules
- `GET /api/validation/rules/active`: Get active simple validation rules
- `GET /api/validation/rules/enhanced/active`: Get active enhanced validation rules
- `POST /api/validation/rules/sample/create`: Create sample validation rules

## Monitoring and Management

The service exposes Spring Boot Actuator endpoints for monitoring and management:

- **Health**: http://localhost:8085/actuator/health
- **Info**: http://localhost:8085/actuator/info
- **Metrics**: http://localhost:8085/actuator/metrics
- **Beans**: http://localhost:8085/actuator/beans
- **Environment**: http://localhost:8085/actuator/env
- **All Actuator Endpoints**: http://localhost:8085/actuator

## Technology Stack

- Spring Boot
- MongoDB
- Spring Data MongoDB
- Spring Expression Language (SpEL)
- Rhino JavaScript Engine