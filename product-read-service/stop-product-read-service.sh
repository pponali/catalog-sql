#!/bin/bash

SERVICE_NAME="product-read-service"
PID_FILE="$SERVICE_NAME.pid"

# Check if service is running
if [ ! -f "$PID_FILE" ]; then
    echo "$SERVICE_NAME is not running."
    exit 0
fi

PID=$(cat "$PID_FILE")
if ! kill -0 "$PID" 2>/dev/null; then
    echo "$SERVICE_NAME is not running but PID file exists. Cleaning up..."
    rm -f "$PID_FILE"
    exit 0
fi

# Stop the service gracefully
echo "Stopping $SERVICE_NAME (PID: $PID)..."
kill -15 "$PID"

# Wait for the service to stop
TIMEOUT=30
COUNT=0
while kill -0 "$PID" 2>/dev/null; do
    sleep 1
    COUNT=$((COUNT + 1))
    if [ $COUNT -ge $TIMEOUT ]; then
        echo "Timeout reached. Forcing kill..."
        kill -9 "$PID"
        break
    fi
done

# Clean up PID file
rm -f "$PID_FILE"
echo "$SERVICE_NAME stopped."