#!/bin/bash

SERVICE_NAME="product-read-service"
PID_FILE="$SERVICE_NAME.pid"
LOG_FILE="$SERVICE_NAME.log"

echo "Starting $SERVICE_NAME..."

# Check if service is already running
if [ -f "$PID_FILE" ]; then
    PID=$(cat "$PID_FILE")
    if kill -0 "$PID" 2>/dev/null; then
        echo "$SERVICE_NAME is already running with PID $PID"
        exit 1
    else
        # PID file exists but process is not running
        rm -f "$PID_FILE"
    fi
fi

# Create logs directory if it doesn't exist
mkdir -p logs

# Build the service if needed
if [ ! -f "target/$SERVICE_NAME-0.0.1-SNAPSHOT.jar" ]; then
    echo "Building $SERVICE_NAME..."
    mvn clean package -DskipTests
fi

# Start the service
nohup java -jar target/$SERVICE_NAME-0.0.1-SNAPSHOT.jar > "$LOG_FILE" 2>&1 &
PID=$!
echo $PID > "$PID_FILE"

echo "$SERVICE_NAME started with PID $PID"
echo "Logs available at $LOG_FILE"