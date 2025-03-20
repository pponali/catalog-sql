#!/bin/bash

# Color definitions
GREEN='\033[0;32m'
RED='\033[0;31m'
YELLOW='\033[0;33m'
NC='\033[0m' # No Color

# Function to check if a service is running
check_service() {
    local service_name=$1
    local pid_file="$(pwd)/${service_name}.pid"
    
    echo -e "\n${YELLOW}Checking ${service_name}...${NC}"
    
    if [ -f "$pid_file" ]; then
        local pid=$(cat "$pid_file")
        
        if ps -p $pid > /dev/null; then
            echo -e "${GREEN}✓ ${service_name} is running (PID: $pid)${NC}"
            return 0
        else
            echo -e "${RED}✗ ${service_name} is not running, but PID file exists${NC}"
            return 1
        fi
    else
        echo -e "${RED}✗ ${service_name} is not running (no PID file)${NC}"
        return 1
    fi
}

# Check MongoDB
echo -e "${YELLOW}Checking MongoDB...${NC}"
if pgrep mongod > /dev/null; then
    echo -e "${GREEN}✓ MongoDB is running${NC}"
else
    echo -e "${RED}✗ MongoDB is not running${NC}"
fi

# Check all services
echo -e "\n${YELLOW}=== Microservices Status ===${NC}"
check_service "validation-service"
check_service "vendor-service"
check_service "channel-service"
check_service "business-service"

# Check service ports
echo -e "\n${YELLOW}=== Service Ports ===${NC}"
netstat -tln | grep -E '8085|8086|8087|8088' | while read -r line; do
    port=$(echo "$line" | awk '{print $4}' | awk -F: '{print $NF}')
    case "$port" in
        "8085") echo -e "${GREEN}✓ Port 8085 (validation-service) is open${NC}" ;;
        "8086") echo -e "${GREEN}✓ Port 8086 (vendor-service) is open${NC}" ;;
        "8087") echo -e "${GREEN}✓ Port 8087 (channel-service) is open${NC}" ;;
        "8088") echo -e "${GREEN}✓ Port 8088 (business-service) is open${NC}" ;;
    esac
done