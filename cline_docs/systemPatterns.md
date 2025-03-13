# System Patterns

## How the system is built
The system is built as a Spring Boot application with a layered architecture:

1. **Controller Layer**: REST APIs for managing entities
2. **Service Layer**: Business logic implementation
3. **Repository Layer**: Data access using Spring Data JPA
4. **Entity Layer**: JPA entities representing the data model
5. **DTO Layer**: Data transfer objects for API requests and responses
6. **Validation Layer**: Validation using Drools rules engine and Jakarta Bean Validation

## Key technical decisions
1. **Spring Boot**: Used as the core framework for building the application
2. **PostgreSQL**: Used as the database for storing catalog data
3. **JPA/Hibernate**: Used for ORM and database access
4. **Drools**: Used as the rules engine for complex validations
5. **Builder Pattern**: Used extensively for creating entities
6. **Repository Pattern**: Used for data access
7. **DTO Pattern**: Used for API requests and responses
8. **Validation Framework**: Custom validation framework using Drools rules

## Architecture patterns
1. **Layered Architecture**: The system follows a standard layered architecture with clear separation of concerns
2. **Domain-Driven Design**: The system is designed around the domain model with entities representing business concepts
3. **Repository Pattern**: Used for data access with Spring Data JPA repositories
4. **Builder Pattern**: Used for creating complex objects
5. **Factory Pattern**: Used for creating objects with complex initialization logic
6. **Strategy Pattern**: Used for implementing different validation strategies
7. **Polymorphic Validation**: The validation framework supports polymorphic validation based on product category
