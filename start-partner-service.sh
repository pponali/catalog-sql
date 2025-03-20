#!/bin/bash

echo "Starting Partner Service..."

# Constants
SERVICE_NAME="partner-service"
JAR_PATH="partner-service/target/${SERVICE_NAME}*.jar"
LOG_FILE="${SERVICE_NAME}.log"
PID_FILE="${SERVICE_NAME}.pid"

# Check if MongoDB is running
if ! nc -z localhost 27017 > /dev/null 2>&1; then
  echo "ERROR: MongoDB is not running on port 27017. Please start MongoDB first."
  exit 1
fi

# Check if service is already running
if [ -f "${PID_FILE}" ]; then
  PID=$(cat "${PID_FILE}")
  if ps -p "${PID}" > /dev/null; then
    echo "WARNING: ${SERVICE_NAME} already running with PID ${PID}"
    exit 0
  else
    echo "Stale PID file found. Removing it."
    rm -f "${PID_FILE}"
  fi
fi

# Check if JAR exists and is built
if [ ! -f $(ls ${JAR_PATH} 2>/dev/null | head -n 1) ]; then
  echo "Building ${SERVICE_NAME}..."
  mvn clean package -DskipTests -f partner-service/pom.xml
fi

# Find the JAR file
JAR_FILE=$(ls ${JAR_PATH} 2>/dev/null | head -n 1)
if [ ! -f "${JAR_FILE}" ]; then
  echo "ERROR: Could not find JAR file for ${SERVICE_NAME}. Please build the service first."
  exit 1
fi

# Start the service
echo "Starting ${SERVICE_NAME} with JAR ${JAR_FILE}..."
nohup java -jar "${JAR_FILE}" > "${LOG_FILE}" 2>&1 &

# Save the PID
PID=$!
echo ${PID} > "${PID_FILE}"
echo "${SERVICE_NAME} started with PID ${PID}. Logs available in ${LOG_FILE}"

# Wait for service to be ready
echo -n "Waiting for ${SERVICE_NAME} to be ready"
for i in {1..30}; do
  if grep -q "Started PartnerServiceApplication" "${LOG_FILE}" 2>/dev/null; then
    echo -e "\n${SERVICE_NAME} is ready!"
    
    # Extract the port from the logs
    PORT=$(grep "Tomcat started on port" "${LOG_FILE}" | grep -o "[0-9]\+" | head -1)
    if [ -n "${PORT}" ]; then
      echo "${SERVICE_NAME} running on port ${PORT}"
    else
      echo "${SERVICE_NAME} running on port 8089"
    fi
    
    exit 0
  fi
  
  echo -n "."
  sleep 1
done

echo -e "\nWARNING: ${SERVICE_NAME} did not become ready within 30 seconds, but startup continues in background."
echo "Check ${LOG_FILE} for details."