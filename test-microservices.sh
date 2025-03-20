#!/bin/bash

# Color codes for better readability
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[0;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

# Function to log messages
log() {
    local level=$1
    local message=$2
    local color=$3
    echo -e "${color}[${level}] ${message}${NC}"
}

# Function to test a service's health endpoint
test_service_health() {
    local service_name=$1
    local url=$2
    
    log "INFO" "Testing ${service_name} health..." "$BLUE"
    
    response=$(curl -s "$url")
    status=$(echo "$response" | grep -o '"status":"[^"]*"' | cut -d'"' -f4)
    
    if [ "$status" = "UP" ]; then
        log "SUCCESS" "${service_name} is healthy (status: UP)" "$GREEN"
        return 0
    else
        log "ERROR" "${service_name} is not healthy (status: ${status:-UNKNOWN})" "$RED"
        return 1
    fi
}

# Function to test validation service API
test_validation_service() {
    log "INFO" "Testing validation service API..." "$BLUE"
    
    # Test getting all enhanced validation rules
    log "INFO" "Fetching enhanced validation rules..." "$BLUE"
    response=$(curl -s http://localhost:8085/api/validation/rules/enhanced)
    
    if [ -n "$response" ]; then
        log "SUCCESS" "Successfully retrieved validation rules" "$GREEN"
    else
        log "ERROR" "Failed to retrieve validation rules" "$RED"
        return 1
    fi
    
    # Test validation with a valid product
    log "INFO" "Testing product validation with valid product..." "$BLUE"
    valid_product='{"name":"Test Product","price":100,"description":"A test product","sku":"ABC-1234"}'
    response=$(curl -s -H "Content-Type: application/json" -d "$valid_product" http://localhost:8085/api/validation/product/enhanced)
    
    valid=$(echo "$response" | grep -o '"valid":[^,}]*' | cut -d':' -f2)
    
    if [ "$valid" = "true" ]; then
        log "SUCCESS" "Valid product validation passed" "$GREEN"
    else
        log "ERROR" "Valid product validation failed: $response" "$RED"
        return 1
    fi
    
    return 0
}

# Main execution starts here
echo -e "${BLUE}=== Microservices Health Check ===${NC}"

# Test validation-service
test_service_health "validation-service" "http://localhost:8085/actuator/health" || exit 1

# Test vendor-service
test_service_health "vendor-service" "http://localhost:8086/actuator/health" || exit 1

# Test channel-service
test_service_health "channel-service" "http://localhost:8087/actuator/health" || exit 1

# Test business-service
test_service_health "business-service" "http://localhost:8088/actuator/health" || exit 1

echo -e "\n${BLUE}=== Validation Service API Tests ===${NC}"
test_validation_service || exit 1

echo -e "\n${GREEN}✓ All tests completed successfully!${NC}"
echo -e "${BLUE}Service URLs:${NC}"
echo -e "${GREEN}validation-service:${NC} http://localhost:8085"
echo -e "${GREEN}vendor-service:${NC} http://localhost:8086"
echo -e "${GREEN}channel-service:${NC} http://localhost:8087"
echo -e "${GREEN}business-service:${NC} http://localhost:8088"