#!/bin/bash

echo "Stopping all microservices..."

# Stop the services in reverse order of dependency
echo "Stopping Catalog Service..."
CATALOG_PID=$(ps aux | grep 'catalog-service' | grep -v grep | awk '{print $2}')
if [ ! -z "$CATALOG_PID" ]; then
  kill $CATALOG_PID
  echo "Catalog Service stopped."
fi

echo "Stopping Partner Service..."
PARTNER_PID=$(ps aux | grep 'partner-service' | grep -v grep | awk '{print $2}')
if [ ! -z "$PARTNER_PID" ]; then
  kill $PARTNER_PID
  echo "Partner Service stopped."
fi

echo "Stopping Channel Service..."
CHANNEL_PID=$(ps aux | grep 'channel-service' | grep -v grep | awk '{print $2}')
if [ ! -z "$CHANNEL_PID" ]; then
  kill $CHANNEL_PID
  echo "Channel Service stopped."
fi

echo "Stopping Vendor Service..."
VENDOR_PID=$(ps aux | grep 'vendor-service' | grep -v grep | awk '{print $2}')
if [ ! -z "$VENDOR_PID" ]; then
  kill $VENDOR_PID
  echo "Vendor Service stopped."
fi

echo "Stopping Rules Service..."
RULES_PID=$(ps aux | grep 'rules-service' | grep -v grep | awk '{print $2}')
if [ ! -z "$RULES_PID" ]; then
  kill $RULES_PID
  echo "Rules Service stopped."
fi

echo "Stopping Validation Service..."
VALIDATION_PID=$(ps aux | grep 'validation-service' | grep -v grep | awk '{print $2}')
if [ ! -z "$VALIDATION_PID" ]; then
  kill $VALIDATION_PID
  echo "Validation Service stopped."
fi

# Stop the discovery service last
echo "Stopping Discovery Service..."
./stop-discovery-service.sh

echo "All microservices have been stopped."