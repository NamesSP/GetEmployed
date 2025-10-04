# API Gateway Authentication Architecture

## Overview

This project has been restructured to implement proper API Gateway authentication where:

1. **Token authentication happens only once** at the gateway level
2. **All requests must pass through the API Gateway** - no direct access to microservices
3. **User context is propagated** from gateway to microservices via headers
4. **Individual microservices have no security configurations** - they trust the gateway

## Architecture Changes

### 1. Gateway Service (Port 8091)
- **Primary authentication point** - validates JWT tokens
- **Routes all requests** to appropriate microservices
- **Adds user context headers** to downstream requests:
  - `X-User-Name`: Username from token
  - `X-User-Role`: User role from token  
  - `X-User-Id`: User ID from token
- **Public endpoints**: `/api/auth/**` (login, register, validate)

### 2. Microservices (All other services)
- **No individual security configurations**
- **Trust the gateway** for authentication
- **Use UserContextUtil** to extract user information from headers
- **Exclude SecurityAutoConfiguration** in main application classes

### 3. Auth Service (Port 8082)
- **Handles login/register** operations
- **Generates JWT tokens** for successful authentication
- **Public endpoints** - no authentication required
- **Uses same JWT secret** as gateway service

## Key Components

### Gateway Authentication Filter
```java
// gateway-service/src/main/java/com/example/filter/GatewayJwtAuthenticationFilter.java
- Validates JWT tokens for all non-public endpoints
- Extracts user information from tokens
- Adds user context headers to downstream requests
- Returns 401 for invalid/missing tokens
```

### User Context Utility
```java
// commonDTO/src/main/java/com/example/util/UserContextUtil.java
- Static utility for extracting user context from headers
- Methods: getCurrentUsername(), getCurrentUserRole(), isAdmin(), etc.
- Used by all microservices to get current user information
```

### JWT Configuration
- **Shared JWT secret** between gateway and auth service
- **Config-server managed** configuration
- **Consistent token validation** across all services

## Request Flow

1. **Client** → **Gateway** (with/without token)
2. **Gateway** validates token (if not public endpoint)
3. **Gateway** adds user context headers
4. **Gateway** → **Microservice** (with user context)
5. **Microservice** uses UserContextUtil to get user info
6. **Microservice** → **Client** (via gateway)

## Configuration Files

### Gateway Service
- `gateway-service.yml` - JWT configuration
- `GatewayConfig.java` - Route definitions
- `GatewaySecurityConfig.java` - Security configuration

### Microservices
- All services exclude `SecurityAutoConfiguration`
- No individual security configurations
- Use `UserContextUtil` for user context

## Testing

Use the provided test script to verify the authentication flow:

```powershell
.\test-gateway-auth.ps1
```

This script tests:
1. User registration
2. User login (token generation)
3. Protected endpoint access (without token) - should fail
4. Protected endpoint access (with token) - should succeed
5. Token validation

## Security Benefits

1. **Centralized Authentication** - Single point of token validation
2. **Consistent Security** - All services use same authentication logic
3. **Performance** - Token validation happens only once
4. **Maintainability** - Security logic centralized in gateway
5. **Scalability** - Easy to add new microservices without security concerns

## Service Ports

- **Gateway Service**: 8091
- **Auth Service**: 8082
- **User Service**: 8081
- **Company Service**: 8083
- **Job Service**: 8084
- **Application Service**: 8085
- **Experience Service**: 8086
- **Discovery Server**: 8761
- **Config Server**: 8888

## Important Notes

1. **All requests must go through gateway** - Direct access to microservices is not allowed
2. **JWT secret must be consistent** between gateway and auth service
3. **User context is propagated via headers** - not through security context
4. **Microservices are stateless** - they don't maintain user sessions
5. **CORS is handled at gateway level** - microservices don't need CORS configuration
