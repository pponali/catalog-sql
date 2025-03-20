#!/bin/bash

# Environment variables
export SPRING_DATA_MONGODB_HOST=localhost
export SPRING_DATA_MONGODB_PORT=27017
export SPRING_DATA_MONGODB_DATABASE=channel_service
export SERVER_PORT=8087

# Log directory
LOG_DIR="$(pwd)/logs"
mkdir -p "$LOG_DIR"

# Build and start the service
cd "$(dirname "$0")/channel-service"
echo "Building channel-service..."
mvn clean package -DskipTests > "$LOG_DIR/channel-service-build.log" 2>&1

if [ $? -ne 0 ]; then
    echo "Failed to build channel-service. Check $LOG_DIR/channel-service-build.log for details."
    exit 1
fi

echo "Starting channel-service..."
java -jar target/channel-service-0.0.1-SNAPSHOT.jar > "$LOG_DIR/channel-service.log" 2>&1 &

# Save PID for later
echo $! > "$(pwd)/channel-service.pid"
echo "Channel service started with PID: $(cat "$(pwd)/channel-service.pid")"
