#!/bin/bash

echo "Stopping Discovery Service..."
PID=$(ps aux | grep 'discovery-service' | grep -v grep | awk '{print $2}')

if [ -z "$PID" ]; then
  echo "Discovery Service is not running."
else
  echo "Stopping Discovery Service (PID: $PID)..."
  kill $PID
  echo "Discovery Service stopped."
fi
