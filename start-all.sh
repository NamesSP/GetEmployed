#!/bin/bash

# This script builds and starts all the microservices in the project.

# --- Prerequisites ---
# - Java 17 or higher must be installed and JAVA_HOME configured.
# - Maven must be installed to build the 'commonDTO' module. You can install it using your system's package manager (e.g., 'sudo apt-get install maven').

# --- Build Stage ---
echo "Making mvnw scripts executable..."
find . -name "mvnw" -exec chmod +x {} \;

echo "Building all services..."

# Build common libraries first
echo "Building common..."
(cd common && ./mvnw clean install)
if [ $? -ne 0 ]; then
    echo "Failed to build common. Aborting."
    exit 1
fi

echo "Building commonDTO... (Requires 'mvn' to be installed and in your PATH)"
(cd commonDTO && mvn clean install)
if [ $? -ne 0 ]; then
    echo "Failed to build commonDTO. Aborting. Make sure 'mvn' is installed."
    exit 1
fi


# Build all other services
for service in application-service auth-service company-service config-server discovery-server experience-service gateway-service job-service logging-service user-service; do
    if [ -d "$service" ]; then
        echo "Building $service..."
        (cd "$service" && ./mvnw clean install)
        if [ $? -ne 0 ]; then
            echo "Failed to build $service. Aborting."
            exit 1
        fi
    fi
done

# --- Run Stage ---
echo "Starting all services..."

# Start services in order
echo "Starting discovery-server..."
nohup java -jar discovery-server/target/discovery-server-*.jar > discovery-server.log 2>&1 &

# Wait for discovery-server to start
echo "Waiting for discovery-server to start..."
sleep 20

echo "Starting config-server..."
nohup java -jar config-server/target/config-server-*.jar > config-server.log 2>&1 &

# Wait for config-server to start
echo "Waiting for config-server to start..."
sleep 20

echo "Starting gateway-service..."
nohup java -jar gateway-service/target/gateway-service-*.jar > gateway-service.log 2>&1 &

# Wait for gateway to start
sleep 10

echo "Starting auth-service..."
nohup java -jar auth-service/target/auth-service-*.jar > auth-service.log 2>&1 &

# Start the rest of the services
for service in application-service company-service experience-service job-service logging-service user-service; do
    if [ -d "$service" ]; then
        echo "Starting $service..."
        nohup java -jar "$service/target/$service-*.jar" > "$service.log" 2>&1 &
    fi
done

echo "All services have been started in the background."
echo "You can check the logs for each service in the corresponding .log file in the root directory (e.g., discovery-server.log)."
echo "To stop all services, you can use the 'pkill -f java' command."
