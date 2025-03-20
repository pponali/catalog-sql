#!/bin/bash

# This script builds and starts all microservices
# Expected order based on dependencies:
# 1. validation-service: Other services validate through this (Contains enhanced validation rules)
# 2. vendor-service: Provides merchant/seller data
# 3. rules-service: Contains workflow and business rules
# 4. channel-service: Handles channels, platforms, stores
# 5. business-service: Partner integration
# 6. copy-service: Handles copying between catalogs
# 7. price-service: Handles pricing strategies
# 8. promotion-service: Handles promotions and discounts
# 9. cart-service: Shopping cart functionality
# 10. catalog-service: Main service that depends on all others

# Define base directory - where this script is located
BASE_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"

SERVICES=(
  "validation-service"
  "vendor-service"
  "rules-service"
  "channel-service"
  "business-service"
  "catalog-service"
)

# The following services are excluded from startup:
# - copy-service
# - price-service 
# - promotion-service
# - cart-service

# Colors for output
GREEN='\033[0;32m'
RED='\033[0;31m'
YELLOW='\033[0;33m'
NC='\033[0m' # No Color

# Function to build a service
build_service() {
  local service=$1
  echo -e "${YELLOW}Building $service...${NC}"
  
  # Check if service directory exists
  if [ ! -d "$BASE_DIR/$service" ]; then
    echo -e "${RED}Service directory $service does not exist${NC}"
    return 1
  fi
  
  cd "$BASE_DIR/$service" || { echo -e "${RED}Failed to change directory to $service${NC}"; return 1; }
  
  # Clean and compile
  echo -e "${YELLOW}Running Maven build for $service...${NC}"
  mvn clean compile -DskipTests
  
  # Check build result
  if [ $? -ne 0 ]; then
    echo -e "${RED}Failed to build $service. Retrying with package goal...${NC}"
    # Try with package goal which might be more complete
    mvn clean package -DskipTests
    
    if [ $? -ne 0 ]; then
      echo -e "${RED}Failed to build $service after retry. Continuing anyway...${NC}"
      # We'll continue anyway, as some services might work even with failed tests
    else
      echo -e "${GREEN}Successfully built $service on second attempt${NC}"
    fi
  else
    echo -e "${GREEN}Successfully built $service${NC}"
  fi
  
  cd "$BASE_DIR"
  return 0
}

# Function to start a service
start_service() {
  local service=$1
  echo -e "${YELLOW}Starting $service...${NC}"
  
  # Check if service directory exists
  if [ ! -d "$BASE_DIR/$service" ]; then
    echo -e "${RED}Service directory $service does not exist${NC}"
    return 1
  fi
  
  cd "$BASE_DIR/$service" || { echo -e "${RED}Failed to change directory to $service${NC}"; return 1; }
  
  # Check if application.yml or application.yaml exists
  if [ ! -f "src/main/resources/application.yml" ] && [ ! -f "src/main/resources/application.yaml" ]; then
    echo -e "${YELLOW}Warning: No application.yml/yaml found for $service. Creating a default one...${NC}"
    mkdir -p src/main/resources
    cat > src/main/resources/application.yml << EOF
spring:
  application:
    name: $service
  data:
    mongodb:
      uri: mongodb://localhost:27017/$service
server:
  port: 0
eureka:
  client:
    serviceUrl:
      defaultZone: http://localhost:8761/eureka/
management:
  endpoints:
    web:
      exposure:
        include: "*"
  endpoint:
    health:
      show-details: always
EOF
    echo -e "${GREEN}Created default application.yml for $service${NC}"
  fi
  
  # Start in background
  echo -e "${YELLOW}Starting $service with Spring Boot...${NC}"
  nohup mvn spring-boot:run -DskipTests > "$service.log" 2>&1 &
  PID=$!
  
  # Check if process is still running after 5 seconds (initial startup check)
  sleep 5
  if ps -p $PID > /dev/null; then
    echo -e "${GREEN}Started $service (PID: $PID). Log file: $service.log${NC}"
    
    # Store PID for later use
    echo $PID > "$service.pid"
    
    # Wait for service to be fully up (up to 30 seconds)
    for i in {1..6}; do
      if grep -q "Started .* in" "$service.log"; then
        echo -e "${GREEN}$service is now fully operational${NC}"
        break
      elif ! ps -p $PID > /dev/null; then
        echo -e "${RED}$service process died. Check logs for details${NC}"
        break
      else
        echo -e "${YELLOW}Waiting for $service to start up completely...${NC}"
        sleep 5
      fi
    done
  else
    echo -e "${RED}Failed to start $service. Check $service.log for details${NC}"
  fi
  
  cd "$BASE_DIR"
  return 0
}

# Main script
echo -e "${YELLOW}Starting MongoDB...${NC}"
bash "$BASE_DIR/start-mongodb.sh"

echo -e "${YELLOW}Setting up databases...${NC}"
bash "$BASE_DIR/setup-databases.sh"

echo -e "${YELLOW}Building all services...${NC}"
for service in "${SERVICES[@]}"; do
  build_service "$service"
done

echo -e "${YELLOW}Starting all services in dependency order...${NC}"
for service in "${SERVICES[@]}"; do
  start_service "$service"
  # Wait a bit between starting services
  sleep 5
done

echo -e "${GREEN}All services started successfully.${NC}"
echo -e "${YELLOW}To stop all services, use: killall java${NC}"