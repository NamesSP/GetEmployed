# Centralized Configuration Setup

This project now uses Spring Cloud Config Server for centralized configuration management.

## Problem Solved

Previously, each microservice had its own `application.properties` file with duplicated database credentials and other configurations. When database credentials changed, you had to update them in every service individually.

## Solution

Now all configurations are centralized in the `config-repo` directory and served by the Config Server.

## Architecture

```
Config Server (Port 8888)
    ↓
config-repo/
    ├── application.yml (Global config)
    ├── auth-service.yml
    ├── user-service.yml
    ├── job-service.yml
    ├── company-service.yml
    ├── application-service.yml
    └── experience-service.yml
```

## How to Use

### 1. Start Config Server First
```bash
# On Windows
start-config-server.bat

# On Linux/Mac
chmod +x start-config-server.sh
./start-config-server.sh
```

### 2. Start Other Services
The services will automatically connect to the Config Server and fetch their configurations.

### 3. Update Configuration
To change database credentials or any other configuration:

1. Edit the appropriate file in `config-repo/` directory
2. Restart the affected services (or use Spring Boot Actuator refresh endpoint)

## Configuration Files

- **Global Configuration**: `config-repo/application.yml` - Common settings for all services
- **Service-Specific**: `config-repo/{service-name}.yml` - Service-specific overrides

## Benefits

1. **Single Source of Truth**: All configurations in one place
2. **Easy Updates**: Change database credentials once, affects all services
3. **Environment Management**: Easy to maintain different environments
4. **Version Control**: All configurations are version controlled
5. **Security**: Sensitive data can be externalized

## Feign Client

Feign client dependency has been added to all services for inter-service communication. This allows services to communicate with each other using declarative REST clients.

## Testing Configuration

You can test the configuration by accessing:
- Config Server: http://localhost:8888
- Service Config: http://localhost:8888/{service-name}/default

## Troubleshooting

1. **Config Server not starting**: Check if port 8888 is available
2. **Services can't connect**: Ensure Config Server is running first
3. **Configuration not loading**: Check bootstrap.properties files

## Next Steps

1. Consider using Git repository instead of local files for production
2. Add encryption for sensitive data
3. Implement configuration refresh without restart
4. Add monitoring and health checks
