#!/bin/bash

# Start Discovery Service
cd "$(dirname "$0")"

echo "Building Discovery Service..."
mvn clean package -DskipTests

echo "Starting Discovery Service..."
nohup java -jar target/discovery-service-0.0.1-SNAPSHOT.jar > discovery-service.log 2>&1 &

echo $! > discovery-service.pid
echo "Discovery Service started with PID: $(cat discovery-service.pid)"
echo "Logs are available at: $PWD/discovery-service.log"