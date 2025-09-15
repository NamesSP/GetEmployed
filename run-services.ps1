# ==========================
# Run Microservices Script
# ==========================
Set-StrictMode -Version Latest
$ErrorActionPreference = 'Stop'

function Stop-PortProcess {
    param([int]$Port)
    $processes = Get-NetTCPConnection -LocalPort $Port | Where-Object { $_.State -eq 'Listen' }
    foreach ($p in $processes) {
        $process = Get-Process -Id $p.OwningProcess -ErrorAction SilentlyContinue
        if ($process) {
            Write-Host "Killing process $($process.Id) using port $Port..." -ForegroundColor Yellow
            $process | Stop-Process -Force
            Start-Sleep -Seconds 2
        }
    }
}

function Start-Microservice {
    param(
        [Parameter(Mandatory = $true)] [string] $Name,
        [Parameter(Mandatory = $true)] [string] $Directory,
        [int] $Port = 8080,
        [int] $TimeoutSeconds = 120,
        [int] $MaxRetries = 3
    )

    $attempt = 1
    while ($attempt -le $MaxRetries) {
        Write-Host "Starting $Name (attempt $attempt/$MaxRetries) from $Directory on port $Port..." -ForegroundColor Cyan

        # Ensure port is free before each attempt
        Stop-PortProcess -Port $Port

        # Build the command
        $mvnw = Join-Path $Directory 'mvnw.cmd'
        if (Test-Path $mvnw) {
            $command = "& { Set-Location '$Directory'; & './mvnw.cmd' spring-boot:run -DskipTests }"
        }
        else {
            $command = "& { Set-Location '$Directory'; mvn spring-boot:run -DskipTests }"
        }

        # Start the process in a new terminal window
        $process = $null
        try {
            $process = Start-Process powershell.exe -ArgumentList "-NoExit", "-Command $command" -PassThru -ErrorAction Stop
            Write-Host "$Name process started with PID $($process.Id)" -ForegroundColor DarkGray
        }
        catch {
            Write-Host "$Name ❌ Failed to launch process: $($_.Exception.Message)" -ForegroundColor Red
        }

        # Wait briefly before health checks to allow bootstrapping
        Start-Sleep -Seconds 5

        # Wait for service health within timeout
        $healthy = $false
        $deadline = (Get-Date).AddSeconds($TimeoutSeconds)
        while (-not $healthy -and (Get-Date) -lt $deadline) {
            try {
                $response = Invoke-WebRequest -Uri "http://localhost:$Port/actuator/health" -UseBasicParsing -TimeoutSec 5 -ErrorAction Stop
                if ($response.StatusCode -eq 200) {
                    $healthy = $true
                    break
                }
            }
            catch {
                # Not healthy yet; wait and retry
                Start-Sleep -Seconds 3
            }
        }

        if ($healthy) {
            Write-Host "$Name ✅ RUNNING on port $Port" -ForegroundColor Green
            return $true
        }

        Write-Host "$Name did not become healthy within $TimeoutSeconds seconds." -ForegroundColor Yellow

        # Stop the process from this attempt before retrying
        if ($process -and -not $process.HasExited) {
            try {
                Write-Host "Stopping $Name process PID $($process.Id) before retry..." -ForegroundColor Yellow
                Stop-Process -Id $process.Id -Force -ErrorAction SilentlyContinue
                Start-Sleep -Seconds 2
            }
            catch {}
        }

        $attempt++
    }

    Write-Host "$Name ❌ FAILED to start after $MaxRetries attempts on port $Port" -ForegroundColor Red
    return $false
}

# ==========================
# Root folder
$root = $PSScriptRoot

# Core services (sequential with dependency)
$coreServices = @(
    @{ Name = 'discovery-server'; Dir = Join-Path $root 'discovery-server'; Port = 8761 },
    @{ Name = 'config-server'; Dir = Join-Path $root 'config-server'; Port = 8888 },
    @{ Name = 'gateway-service'; Dir = Join-Path $root 'gateway-service'; Port = 8080 },
    @{ Name = 'auth-service'; Dir = Join-Path $root 'auth-service'; Port = 8081 },
    @{ Name = 'user-service'; Dir = Join-Path $root 'user-service'; Port = 8082 }
)

# Other services (can start after core services)
$otherServices = @(
    @{ Name = 'application-service'; Dir = Join-Path $root 'application-service'; Port = 8083 },
    @{ Name = 'company-service'; Dir = Join-Path $root 'company-service'; Port = 8084 },
    @{ Name = 'experience-service'; Dir = Join-Path $root 'experience-service'; Port = 8085 },
    @{ Name = 'job-service'; Dir = Join-Path $root 'job-service'; Port = 8086 },
    @{ Name = 'logging-service'; Dir = Join-Path $root 'logging-service'; Port = 8087 }
)

# ==========================
# Start Core Services sequentially
foreach ($svc in $coreServices) {
    if (Test-Path $svc.Dir) {
        Start-Microservice -Name $svc.Name -Directory $svc.Dir -Port $svc.Port -TimeoutSeconds 120 -MaxRetries 3
    }
    else {
        Write-Warning "Skipping $($svc.Name) — directory not found at $($svc.Dir)"
    }
}

# ==========================
# Start Other Services (regardless of core services)
foreach ($svc in $otherServices) {
    if (Test-Path $svc.Dir) {
        Start-Microservice -Name $svc.Name -Directory $svc.Dir -Port $svc.Port -TimeoutSeconds 60 -MaxRetries 2
    }
    else {
        Write-Warning "Skipping $($svc.Name) — directory not found at $($svc.Dir)"
    }
}

Write-Host "All microservices launched (or attempted)." -ForegroundColor Green
