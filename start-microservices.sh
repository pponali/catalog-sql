#!/bin/bash

# Function to execute a command in a new terminal window
function run_in_new_terminal {
    osascript -e "tell application \"Terminal\" to do script \"cd $(pwd) && $1\""
}

echo "Starting microservices in the correct order..."

# Step 1: Start Discovery Service
echo "Starting Discovery Service..."
run_in_new_terminal "./start-discovery-service.sh"

# Wait for Discovery Service to be available
echo "Waiting for Discovery Service to be available..."
sleep 10

# Step 2: Start dependent services in order
echo "Starting Validation Service..."
run_in_new_terminal "cd validation-service && mvn spring-boot:run"

echo "Starting Rules Service..."
run_in_new_terminal "cd rules-service && mvn spring-boot:run"

echo "Waiting for core services to initialize..."
sleep 5

echo "Starting Vendor Service..."
run_in_new_terminal "cd vendor-service && mvn spring-boot:run"

echo "Starting Channel Service..."
run_in_new_terminal "cd channel-service && mvn spring-boot:run"

echo "Starting Partner Service..."
run_in_new_terminal "cd partner-service && mvn spring-boot:run"

echo "Waiting for second-tier services to initialize..."
sleep 5

echo "Starting Catalog Service..."
run_in_new_terminal "cd catalog-service && mvn spring-boot:run"

echo "All microservices have been started."
echo "To stop all services, run: ./stop-microservices.sh"