#!/bin/bash

# Define base directory - where this script is located
BASE_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"

# Colors for output
GREEN='\033[0;32m'
RED='\033[0;31m'
YELLOW='\033[0;33m'
NC='\033[0m' # No Color

# Service list - same as in start-services.sh
SERVICES=(
  "validation-service"
  "vendor-service"
  "rules-service"
  "channel-service"
  "business-service"
  "catalog-service"
)

echo -e "${YELLOW}Stopping all microservices...${NC}"

# Method 1: Stop services using PID files (graceful)
for service in "${SERVICES[@]}"; do
  if [ -f "$BASE_DIR/$service.pid" ]; then
    pid=$(cat "$BASE_DIR/$service.pid")
    if ps -p $pid > /dev/null; then
      echo -e "${YELLOW}Stopping $service (PID: $pid)...${NC}"
      kill $pid
      sleep 2
      
      # Check if the process was killed
      if ! ps -p $pid > /dev/null; then
        echo -e "${GREEN}$service stopped successfully.${NC}"
        rm "$BASE_DIR/$service.pid"
      else
        echo -e "${YELLOW}$service is still running. Trying force kill...${NC}"
        kill -9 $pid
        if ! ps -p $pid > /dev/null; then
          echo -e "${GREEN}$service force stopped.${NC}"
          rm "$BASE_DIR/$service.pid"
        else
          echo -e "${RED}Failed to stop $service.${NC}"
        fi
      fi
    else
      echo -e "${YELLOW}$service is not running (PID: $pid).${NC}"
      rm "$BASE_DIR/$service.pid"
    fi
  fi
done

# Method 2: Use killall to stop any remaining Java processes related to these services (fallback)
echo -e "${YELLOW}Stopping any remaining Java processes...${NC}"
for service in "${SERVICES[@]}"; do
  if pgrep -f "$service.*jar" > /dev/null; then
    echo -e "${YELLOW}Killing remaining Java processes for $service...${NC}"
    pkill -f "$service.*jar"
  fi
done

# Check if any related processes are still running
if pgrep -f "spring-boot:run" > /dev/null; then
  echo -e "${YELLOW}Some Spring Boot processes are still running. Cleaning up...${NC}"
  pkill -f "spring-boot:run"
fi

echo -e "${GREEN}All services stopped.${NC}"

# Ask user if they want to stop MongoDB as well
read -p "Do you want to stop MongoDB as well? (y/n): " stop_mongo
if [[ "$stop_mongo" == "y" || "$stop_mongo" == "Y" ]]; then
  echo -e "${YELLOW}Stopping MongoDB...${NC}"
  
  if [[ "$OSTYPE" == "darwin"* ]]; then
    if pgrep -x "mongod" > /dev/null; then
      pkill -f mongod
      echo -e "${GREEN}MongoDB stopped.${NC}"
    else
      echo -e "${YELLOW}MongoDB is not running.${NC}"
    fi
  elif [[ "$OSTYPE" == "linux-gnu"* ]]; then
    if systemctl is-active --quiet mongodb || systemctl is-active --quiet mongod; then
      sudo systemctl stop mongodb || sudo systemctl stop mongod
      echo -e "${GREEN}MongoDB stopped.${NC}"
    else
      echo -e "${YELLOW}MongoDB is not running.${NC}"
    fi
  else
    echo -e "${YELLOW}Please stop MongoDB manually on your system.${NC}"
  fi
fi

echo -e "${GREEN}Cleanup complete. All services have been stopped.${NC}"