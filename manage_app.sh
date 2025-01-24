#!/bin/bash

# Function to build and start the application
function start_app() {
    echo "Running Maven package..."
    mvn package -DskipTests
    if [ $? -ne 0 ]; then
        echo "Maven package failed. Exiting."
        exit 1
    fi

    echo "Starting Docker Compose services..."
    docker compose -f ./docker-compose.yml up
}

# Function to stop and remove Docker Compose services
function stop_app() {
    echo "Stopping Docker Compose services..."
    docker compose -f ./docker-compose.yml down
}

# Main menu
echo "Choose an option:"
echo "1) Start Application"
echo "2) Stop Application"
read -p "Enter your choice: " choice

case $choice in
    1)
        start_app
        ;;
    2)
        stop_app
        ;;
    *)
        echo "Invalid choice. Exiting."
        exit 1
        ;;
esac