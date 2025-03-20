# Rules Service

The Rules Service is a microservice responsible for storing, managing, and evaluating validation and business rules for the catalog system. It provides an API for other services to validate their data against these rules.

## Features

- Storage and retrieval of validation rules for data validation
- Storage and retrieval of business rules for business logic execution
- Data import from CSV files
- REST API for rule management
- Rule evaluation for products, categories, partners, etc.

## Data Models

### Validation Rules

Validation rules are used to validate data against specific conditions. They have the following attributes:

- `ruleId`: Unique identifier for the rule
- `name`: Name of the rule
- `description`: Description of the rule
- `entityType`: Type of entity the rule applies to (PRODUCT, PARTNER, CATEGORY, etc.)
- `attribute`: The attribute this rule validates
- `condition`: The validation condition (e.g., NOT_NULL, REGEX, MIN_LENGTH, etc.)
- `conditionValue`: The value used in the condition (e.g., regex pattern, min length, etc.)
- `message`: The message to display if validation fails
- `severity`: ERROR, WARNING, INFO
- `priority`: Higher numbers = higher priority
- `active`: Whether the rule is active
- `category`: Business category this rule applies to (ELECTRONICS, FASHION, etc.)

### Business Rules

Business rules are used to execute business logic conditionally. They have the following attributes:

- `ruleId`: Unique identifier for the rule
- `name`: Name of the rule
- `description`: Description of the rule
- `ruleType`: Type of rule (PRICING, DISCOUNT, PROMOTION, SHIPPING, etc.)
- `entityType`: Type of entity the rule applies to (PRODUCT, ORDER, CUSTOMER, etc.)
- `condition`: The condition expression
- `action`: The action to perform when the condition is met
- `actionParameters`: JSON parameters for the action
- `priority`: Rule priority (higher number = higher priority)
- `active`: Whether the rule is active
- `category`: Business category this rule applies to
- `startDate`: When the rule becomes active
- `endDate`: When the rule expires

## API Endpoints

### Data Import API

- `POST /mongodb/import/validation-rules`: Import validation rules from CSV
- `POST /mongodb/import/business-rules`: Import business rules from CSV
- `POST /mongodb/import/all`: Import all rules from CSV

## Sample Data

The service comes with sample CSV data files:

- `validation_rules.csv`: Sample validation rules
- `business_rules.csv`: Sample business rules

## Setup and Configuration

### MongoDB Configuration

The service uses MongoDB for storing rules data. Configure MongoDB in the `application.yaml` file:

```yaml
spring:
  data:
    mongodb:
      uri: mongodb://localhost:27017/rules_service
      auto-index-creation: true
```

### Running the Service

The service runs on port 8084 by default. You can start it using:

```
./mvnw spring-boot:run
```

## Integration with Other Services

The Rules Service can be integrated with other services to provide validation and business logic execution. Integration can be done via:

1. REST API calls
2. Kafka event processing
3. gRPC service calls (for high-performance validation)

## Future Enhancements

- Rule versioning and history
- Rule execution engine with complex conditions
- Support for rule inheritance and composition
- Rule testing and simulation
- Integration with a rules DSL for more complex rule definitions