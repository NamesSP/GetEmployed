#!/bin/bash

echo "Starting Config Server..."
cd config-server
mvn spring-boot:run &
CONFIG_PID=$!

echo "Waiting for Config Server to start..."
sleep 30

echo "Config Server started with PID: $CONFIG_PID"
echo "Config Server is running on http://localhost:8888"
echo "To stop the Config Server, run: kill $CONFIG_PID"
