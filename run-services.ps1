# run-services.ps1

# Function to start a service
function Start-Microservice {
    param (
        [string]$Name,
        [string]$Directory,
        [int]$WaitSeconds = 20
    )

    Write-Host "Starting $Name..."
    Start-Process powershell -ArgumentList "-NoExit", "-Command", "cd $Directory; ./mvnw spring-boot:run" `
        -WindowStyle Minimized

    Start-Sleep -Seconds $WaitSeconds
    Write-Host "$Name started."
}

# Base directory where all microservices are located
$baseDir = "C:\Users\Administrator\Desktop\SpringBoot\GetEmployed"

# Start core services in order
Start-Microservice -Name "discovery-server" -Directory "$baseDir\discovery-server" -WaitSeconds 25
Start-Microservice -Name "config-server" -Directory "$baseDir\config-server" -WaitSeconds 25
Start-Microservice -Name "auth-service" -Directory "$baseDir\auth-service" -WaitSeconds 25
Start-Microservice -Name "gateway-service" -Directory "$baseDir\gateway-service" -WaitSeconds 25

# Define remaining services (to start in parallel)
$otherServices = @(
    "application-service",
    "company-service",
    "experience-service",
    "job-service",
    "logging-service",
    "user-service"
)

# Start other services in parallel
foreach ($svc in $otherServices) {
    Start-Process powershell -ArgumentList "-NoExit", "-Command", "cd $baseDir\$svc; ./mvnw spring-boot:run" `
        -WindowStyle Minimized
    Write-Host "$svc started in parallel."
}

Write-Host "All microservices launched (or attempted)."
