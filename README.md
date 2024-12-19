# Catalog SQL DB POC

A Spring Boot application with PostgreSQL database integration, featuring Spring DevTools, Actuator, OpenAPI documentation, and JPA.

## Prerequisites

- Java 17
- Maven
- PostgreSQL database

## Setup

1. Create a PostgreSQL database named `catalogdb`
2. Update database credentials in `src/main/resources/application.yaml` if needed
3. Build the project: `mvn clean install`
4. Run the application: `mvn spring-boot:run`

## Features

- Spring Boot 3.2.1
- Spring Data JPA
- PostgreSQL Integration
- Spring DevTools for development
- Spring Actuator for monitoring
- OpenAPI/Swagger Documentation
- Lombok for reducing boilerplate code

## API Documentation

Access the API documentation at:
- Swagger UI: http://localhost:8080/swagger-ui.html
- OpenAPI JSON: http://localhost:8080/api-docs

## Actuator Endpoints

Access actuator endpoints at:
- http://localhost:8080/actuator

## Database Schema

The database schema for this project is documented in the `Schema.pdf` file in the root directory. This document contains:
- Detailed entity relationships
- Table structures
- Field descriptions
- Data types and constraints

Please refer to `Schema.pdf` for a complete understanding of the database design before making any changes to the data model.

## Development

The application uses Spring DevTools for automatic restart during development. Any changes to the classpath will trigger an automatic restart.
