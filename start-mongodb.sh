#!/bin/bash

# Colors for output
GREEN='\033[0;32m'
RED='\033[0;31m'
YELLOW='\033[0;33m'
NC='\033[0m' # No Color

# Check if MongoDB is installed
if ! command -v mongod &> /dev/null; then
    echo -e "${RED}MongoDB is not installed. Please install MongoDB and try again.${NC}"
    echo "On macOS: brew install mongodb-community"
    echo "On Ubuntu: sudo apt-get install -y mongodb"
    echo "On Windows: Please visit https://www.mongodb.com/try/download/community"
    exit 1
fi

# Check if MongoDB is running
echo -e "${YELLOW}Checking if MongoDB is running...${NC}"

# For macOS
if [[ "$OSTYPE" == "darwin"* ]]; then
    if pgrep -x "mongod" > /dev/null; then
        echo -e "${GREEN}MongoDB is already running.${NC}"
    else
        echo -e "${YELLOW}MongoDB is not running. Starting MongoDB...${NC}"
        
        # Create MongoDB data directory if it doesn't exist
        if [ ! -d ~/data/db ]; then
            echo -e "${YELLOW}Creating MongoDB data directory...${NC}"
            mkdir -p ~/data/db
        fi
        
        # Start MongoDB as a background process
        mongod --dbpath ~/data/db &
        
        # Wait for MongoDB to start
        sleep 3
        if pgrep -x "mongod" > /dev/null; then
            echo -e "${GREEN}MongoDB started successfully.${NC}"
        else
            echo -e "${RED}Failed to start MongoDB. Please check logs for details.${NC}"
            exit 1
        fi
    fi
# For Linux
elif [[ "$OSTYPE" == "linux-gnu"* ]]; then
    if systemctl is-active --quiet mongodb || systemctl is-active --quiet mongod; then
        echo -e "${GREEN}MongoDB is already running.${NC}"
    else
        echo -e "${YELLOW}MongoDB is not running. Starting MongoDB...${NC}"
        sudo systemctl start mongodb || sudo systemctl start mongod
        
        # Wait for MongoDB to start
        sleep 3
        if systemctl is-active --quiet mongodb || systemctl is-active --quiet mongod; then
            echo -e "${GREEN}MongoDB started successfully.${NC}"
        else
            echo -e "${RED}Failed to start MongoDB. Please check logs for details.${NC}"
            sudo journalctl -u mongodb -n 20
            exit 1
        fi
    fi
else
    echo -e "${YELLOW}Unable to automatically start MongoDB on this platform.${NC}"
    echo -e "${YELLOW}Please ensure MongoDB is running before continuing.${NC}"
    read -p "Press Enter to continue once you've confirmed MongoDB is running..."
fi

# Create test connection to verify MongoDB is working
echo -e "${YELLOW}Testing MongoDB connection...${NC}"
mongo --eval "db.version()" 2>/dev/null || mongosh --eval "db.version()" 2>/dev/null

if [ $? -ne 0 ]; then
    echo -e "${RED}Failed to connect to MongoDB. Please check your installation.${NC}"
    exit 1
else
    echo -e "${GREEN}MongoDB connection successful.${NC}"
fi

echo -e "${GREEN}MongoDB is ready to use.${NC}"