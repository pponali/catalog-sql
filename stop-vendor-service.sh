#!/bin/bash

# Find PID from PID file
PID_FILE="$(pwd)/vendor-service.pid"

if [ -f "$PID_FILE" ]; then
    PID=$(cat "$PID_FILE")
    
    if ps -p "$PID" > /dev/null; then
        echo "Stopping vendor-service (PID: $PID)..."
        kill "$PID"
        
        # Wait for process to terminate
        COUNTER=0
        while ps -p "$PID" > /dev/null && [ $COUNTER -lt 10 ]; do
            sleep 1
            COUNTER=$((COUNTER+1))
        done
        
        # Force kill if still running
        if ps -p "$PID" > /dev/null; then
            echo "Force killing vendor-service (PID: $PID)..."
            kill -9 "$PID"
        fi
        
        # Verify it's stopped
        if ps -p "$PID" > /dev/null; then
            echo "Failed to stop vendor-service (PID: $PID)"
        else
            echo "Vendor service stopped successfully"
            rm "$PID_FILE"
        fi
    else
        echo "Vendor service is not running (stale PID file)"
        rm "$PID_FILE"
    fi
else
    echo "Vendor service is not running (no PID file)"
fi
