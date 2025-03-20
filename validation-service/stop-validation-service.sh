#!/bin/bash

# Find the PID of the validation service
VALIDATION_PID=$(ps -ef | grep "validation-service-0.0.1-SNAPSHOT.jar" | grep -v grep | awk '{print $2}')

if [ -z "$VALIDATION_PID" ]; then
    echo "Validation service is not running."
else
    echo "Stopping validation service (PID: $VALIDATION_PID)..."
    kill $VALIDATION_PID
    echo "Validation service stopped."
fi