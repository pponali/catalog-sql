#!/bin/bash

# Start Validation Service
cd "$(dirname "$0")"

echo "Building validation service..."
mvn clean package -DskipTests

echo "Starting validation service..."
nohup java -jar target/validation-service-0.0.1-SNAPSHOT.jar > validation-service.log 2>&1 &

echo $! > validation-service.pid
echo "Validation Service started with PID: $(cat validation-service.pid)"
echo "Logs are available at: $PWD/validation-service.log"