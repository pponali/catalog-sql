# Technical Context

## Technologies used
1. **Java 21**: Programming language
2. **Spring Boot 3.2.1**: Core framework
3. **Spring Data JPA**: ORM for database access
4. **PostgreSQL**: Database
5. **Drools**: Rules engine for validation
6. **Lombok**: Reducing boilerplate code
7. **Jackson**: JSON processing
8. **OpenAPI/Swagger**: API documentation
9. **Spring DevTools**: Development tools
10. **Spring Actuator**: Monitoring
11. **Caffeine**: Caching

## Development setup
1. **Java 21**: Required for running the application
2. **Maven**: Used for building the project
3. **PostgreSQL**: Required for the database
4. **IDE**: Any Java IDE (IntelliJ IDEA, Eclipse, VS Code)

## Technical constraints
1. **Database**: The application is designed to work with PostgreSQL
2. **Java Version**: The application requires Java 21
3. **Validation Framework**: The validation framework is based on Drools rules engine
4. **Entity Structure**: The entity structure follows a specific pattern with BaseEntity as the base class
5. **Builder Pattern**: The application uses the builder pattern extensively for creating entities
6. **Polymorphic Validation**: The validation framework supports polymorphic validation based on product category
