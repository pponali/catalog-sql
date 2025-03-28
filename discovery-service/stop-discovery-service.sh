#!/bin/bash

# Stop Discovery Service
cd "$(dirname "$0")"

if [ -f discovery-service.pid ]; then
    PID=$(cat discovery-service.pid)
    if ps -p $PID > /dev/null; then
        echo "Stopping Discovery Service (PID: $PID)..."
        kill $PID
        sleep 2
        if ps -p $PID > /dev/null; then
            echo "Discovery Service did not stop gracefully, forcing..."
            kill -9 $PID
        fi
    else
        echo "Discovery Service is not running with PID: $PID"
    fi
    rm -f discovery-service.pid
    echo "Discovery Service stopped"
else
    echo "No PID file found for Discovery Service"
fi