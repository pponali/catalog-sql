#!/bin/bash

echo "Stopping Rules Service..."

# Constants
SERVICE_NAME="rules-service"
PID_FILE="${SERVICE_NAME}.pid"

# Check if PID file exists
if [ ! -f "${PID_FILE}" ]; then
  echo "No PID file found for ${SERVICE_NAME}."
  
  # Check if there's any Java process with service name
  PID=$(ps -ef | grep java | grep "${SERVICE_NAME}" | grep -v grep | awk '{print $2}')
  if [ -z "${PID}" ]; then
    echo "${SERVICE_NAME} is not running."
    exit 0
  else
    echo "Found running ${SERVICE_NAME} with PID ${PID}."
  fi
else
  PID=$(cat "${PID_FILE}")
  echo "Found PID file with PID ${PID}."
fi

# Check if process is running
if ps -p "${PID}" > /dev/null; then
  echo "Stopping ${SERVICE_NAME} with PID ${PID}..."
  kill "${PID}"
  
  # Wait for process to terminate
  echo -n "Waiting for ${SERVICE_NAME} to stop"
  for i in {1..30}; do
    if ! ps -p "${PID}" > /dev/null; then
      break
    fi
    echo -n "."
    sleep 1
  done
  echo ""
  
  # Check if process still running after timeout
  if ps -p "${PID}" > /dev/null; then
    echo "WARNING: ${SERVICE_NAME} did not stop gracefully. Sending SIGKILL..."
    kill -9 "${PID}"
    sleep 2
  fi
  
  echo "${SERVICE_NAME} stopped."
else
  echo "${SERVICE_NAME} is not running with PID ${PID}."
fi

# Clean up PID file
rm -f "${PID_FILE}"