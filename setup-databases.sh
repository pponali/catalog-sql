#!/bin/bash

# This script creates the databases for all microservices
# Prerequisites: PostgreSQL server must be running

# Set PostgreSQL credentials
pg_user="myuser"
pg_password="mypassword"
pg_host="localhost"
pg_port="5432"

echo "Using PostgreSQL credentials:"
echo "Username: $pg_user"
echo "Host: $pg_host:$pg_port"

# Database names
CATALOG_DB="catalog_db"
VENDOR_DB="vendor_db"
RULES_DB="rules_db"
BUSINESS_DB="business_db"
VALIDATION_DB="validation_db"
CHANNEL_DB="channel_db"

# Create databases function
create_db() {
  local db_name=$1
  echo "Creating database: $db_name..."
  
  # Check if database exists
  if PGPASSWORD=$pg_password psql -h $pg_host -p $pg_port -U $pg_user -lqt | cut -d \| -f 1 | grep -qw $db_name; then
    echo "Database $db_name already exists. Skipping..."
  else
    # Create the database
    PGPASSWORD=$pg_password psql -h $pg_host -p $pg_port -U $pg_user -c "CREATE DATABASE \"$db_name\";"
    echo "Database $db_name created successfully."
  fi
}

# Create all databases
create_db $CATALOG_DB
create_db $VENDOR_DB
create_db $RULES_DB
create_db $BUSINESS_DB
create_db $VALIDATION_DB
create_db $CHANNEL_DB

# Save credentials to application properties files
for service in validation-service vendor-service rules-service channel-service business-service catalog-service; do
  # Create MongoDB connection for services that use it
  if [[ "$service" == "vendor-service" || "$service" == "channel-service" || "$service" == "validation-service" || "$service" == "rules-service" ]]; then
    echo "Creating MongoDB configuration for $service"
    
    mkdir -p "$service/src/main/resources"
    cat << EOF > "$service/src/main/resources/application.yaml"
spring:
  application:
    name: ${service}
  data:
    mongodb:
      host: localhost
      port: 27017
      database: ${service//-/_}
      auto-index-creation: true

server:
  port: 0  # Random port assignment for Eureka
  
eureka:
  client:
    serviceUrl:
      defaultZone: http://localhost:8761/eureka/
  instance:
    preferIpAddress: true
    
management:
  endpoints:
    web:
      exposure:
        include: "*"
  endpoint:
    health:
      show-details: always
EOF
  else
    # Create PostgreSQL connection for services that use it
    echo "Creating PostgreSQL configuration for $service"
    
    mkdir -p "$service/src/main/resources"
    cat << EOF > "$service/src/main/resources/application.yaml"
spring:
  datasource:
    url: jdbc:postgresql://$pg_host:$pg_port/${service//-/_}
    username: $pg_user
    password: $pg_password
    driver-class-name: org.postgresql.Driver
  jpa:
    hibernate:
      ddl-auto: update
    show-sql: true
    properties:
      hibernate:
        format_sql: true
        dialect: org.hibernate.dialect.PostgreSQLDialect

server:
  port: 0  # Random port assignment
EOF
  fi
done

echo "All databases created successfully."