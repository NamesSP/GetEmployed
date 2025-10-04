# Test script for Gateway Authentication
# This script tests the complete authentication flow through the API Gateway

Write-Host "Testing Gateway Authentication Flow" -ForegroundColor Green
Write-Host "=================================" -ForegroundColor Green

# Base URLs
$GATEWAY_URL = "http://localhost:8091"
$AUTH_URL = "$GATEWAY_URL/api/auth"

Write-Host "`n1. Testing User Registration..." -ForegroundColor Yellow
$registerData = @{
    username = "testuser"
    email = "test@example.com"
    password = "password123"
    role = "SEEKER"
} | ConvertTo-Json

try {
    $registerResponse = Invoke-RestMethod -Uri "$AUTH_URL/register" -Method POST -Body $registerData -ContentType "application/json"
    Write-Host "Registration successful: $($registerResponse.message)" -ForegroundColor Green
} catch {
    Write-Host "Registration failed: $($_.Exception.Message)" -ForegroundColor Red
}

Write-Host "`n2. Testing User Login..." -ForegroundColor Yellow
$loginData = @{
    username = "testuser"
    password = "password123"
} | ConvertTo-Json

try {
    $loginResponse = Invoke-RestMethod -Uri "$AUTH_URL/login" -Method POST -Body $loginData -ContentType "application/json"
    $token = $loginResponse.token
    Write-Host "Login successful. Token received." -ForegroundColor Green
    Write-Host "Token: $($token.Substring(0, 50))..." -ForegroundColor Cyan
} catch {
    Write-Host "Login failed: $($_.Exception.Message)" -ForegroundColor Red
    exit 1
}

Write-Host "`n3. Testing Protected Endpoint (without token)..." -ForegroundColor Yellow
try {
    $protectedResponse = Invoke-RestMethod -Uri "$GATEWAY_URL/api/users" -Method GET
    Write-Host "ERROR: Should have been blocked!" -ForegroundColor Red
} catch {
    Write-Host "Correctly blocked: $($_.Exception.Message)" -ForegroundColor Green
}

Write-Host "`n4. Testing Protected Endpoint (with token)..." -ForegroundColor Yellow
$headers = @{
    "Authorization" = "Bearer $token"
}

try {
    $protectedResponse = Invoke-RestMethod -Uri "$GATEWAY_URL/api/users" -Method GET -Headers $headers
    Write-Host "Protected endpoint accessed successfully!" -ForegroundColor Green
} catch {
    Write-Host "Protected endpoint access failed: $($_.Exception.Message)" -ForegroundColor Red
}

Write-Host "`n5. Testing Token Validation..." -ForegroundColor Yellow
try {
    $validateResponse = Invoke-RestMethod -Uri "$AUTH_URL/validate" -Method POST -Headers $headers
    Write-Host "Token validation: $validateResponse" -ForegroundColor Green
} catch {
    Write-Host "Token validation failed: $($_.Exception.Message)" -ForegroundColor Red
}

Write-Host "`nGateway Authentication Test Complete!" -ForegroundColor Green
