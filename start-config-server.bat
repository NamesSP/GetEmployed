@echo off
echo Starting Config Server...
cd config-server
start "Config Server" mvn spring-boot:run

echo Waiting for Config Server to start...
timeout /t 30 /nobreak

echo Config Server should be running on http://localhost:8888
echo Check the Config Server window for status
