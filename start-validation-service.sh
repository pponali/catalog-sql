#!/bin/bash

# Colors for output
GREEN='\033[0;32m'
RED='\033[0;31m'
YELLOW='\033[0;33m'
NC='\033[0m' # No Color

echo -e "${YELLOW}Starting validation-service in debug mode...${NC}"

# First, make sure MongoDB is running
if ! pgrep -x "mongod" > /dev/null; then
  echo -e "${YELLOW}MongoDB is not running. Starting MongoDB...${NC}"
  if [ ! -d ~/data/db ]; then
    echo -e "${YELLOW}Creating MongoDB data directory...${NC}"
    mkdir -p ~/data/db
  fi
  mongod --dbpath ~/data/db &
  sleep 5
  echo -e "${GREEN}MongoDB started${NC}"
else
  echo -e "${GREEN}MongoDB is already running${NC}"
fi

# Create a test database
echo -e "${YELLOW}Creating validation_service database...${NC}"
mongo --eval "db = db.getSiblingDB('validation_service')" 2>/dev/null || mongosh --eval "db = db.getSiblingDB('validation_service')" 2>/dev/null

# Go to validation service directory
cd validation-service

# Create application.yml if it doesn't exist
if [ ! -f "src/main/resources/application.yml" ]; then
  echo -e "${YELLOW}Creating application.yml...${NC}"
  mkdir -p src/main/resources
  cat > src/main/resources/application.yml << EOF
spring:
  application:
    name: validation-service
  data:
    mongodb:
      host: localhost
      port: 27017
      database: validation_service
      auto-index-creation: true

server:
  port: 8081

logging:
  level:
    root: INFO
    com.nosql.poc: DEBUG
    org.springframework.data.mongodb: DEBUG
EOF
  echo -e "${GREEN}Created application.yml${NC}"
fi

# Run Maven with explicit package goal
echo -e "${YELLOW}Building validation-service...${NC}"
mvn clean compile

# Start the service in the foreground for debugging
echo -e "${YELLOW}Starting validation-service...${NC}"
echo -e "${GREEN}Use Ctrl+C to stop the service${NC}"
mvn spring-boot:run -Dspring-boot.run.jvmArguments="-Xdebug -Xrunjdwp:transport=dt_socket,server=y,suspend=n,address=5005"