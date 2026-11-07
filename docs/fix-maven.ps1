# ============================================================
# Maven Build Fix Script for Selenium Capstone (PowerShell)
# Run: .\fix-maven.ps1
# ============================================================

Write-Host ""
Write-Host "====================================" -ForegroundColor Green
Write-Host "Maven Build Fix Script (PowerShell)" -ForegroundColor Green
Write-Host "====================================" -ForegroundColor Green
Write-Host ""

# Step 1: Check Java version
Write-Host "[1/5] Checking Java version..." -ForegroundColor Cyan
java -version
Write-Host ""

# Step 2: Clear Maven cache
Write-Host "[2/5] Clearing Maven repository cache..." -ForegroundColor Cyan
$m2Path = "$env:USERPROFILE\.m2\repository"

if (Test-Path $m2Path) {
    Write-Host "Removing $m2Path" -ForegroundColor Yellow
    Remove-Item -Recurse -Force $m2Path -ErrorAction SilentlyContinue
    Write-Host "Cache cleared successfully!" -ForegroundColor Green
} else {
    Write-Host "No existing cache to clear." -ForegroundColor Yellow
}
Write-Host ""

# Step 3: Verify pom.xml exists
Write-Host "[3/5] Verifying project structure..." -ForegroundColor Cyan
if (-not (Test-Path "pom.xml")) {
    Write-Host "ERROR: pom.xml not found!" -ForegroundColor Red
    Write-Host "Please run this script from the project root directory." -ForegroundColor Red
    Read-Host "Press Enter to exit"
    exit 1
}
Write-Host "pom.xml found - OK" -ForegroundColor Green
Write-Host ""

# Step 4: Run Maven clean
Write-Host "[4/5] Running Maven clean..." -ForegroundColor Cyan
& mvn clean

if ($LASTEXITCODE -ne 0) {
    Write-Host "ERROR: Maven clean failed!" -ForegroundColor Red
    Read-Host "Press Enter to exit"
    exit 1
}
Write-Host ""

# Step 5: Run Maven install
Write-Host "[5/5] Installing dependencies (this may take a few minutes)..." -ForegroundColor Cyan
& mvn install -U

if ($LASTEXITCODE -ne 0) {
    Write-Host ""
    Write-Host "ERROR: Maven install failed!" -ForegroundColor Red
    Write-Host ""
    Write-Host "Troubleshooting steps:" -ForegroundColor Yellow
    Write-Host "1. Clear Maven cache: Remove-Item -Recurse -Force '$env:USERPROFILE\.m2\repository'" -ForegroundColor Yellow
    Write-Host "2. Retry: mvn clean install -U" -ForegroundColor Yellow
    Write-Host "3. With settings: mvn clean install --settings maven-settings.xml" -ForegroundColor Yellow
    Write-Host "4. Debug mode: mvn clean install -U -X" -ForegroundColor Yellow
    Write-Host ""
    Write-Host "If you see 'module jdk.compiler' error:" -ForegroundColor Cyan
    Write-Host "  This is Java 17 module system issue - already fixed in pom.xml" -ForegroundColor Cyan
    Write-Host "  Try: mvn clean compile (just compile, don't install)" -ForegroundColor Cyan
    Write-Host ""
    Read-Host "Press Enter to exit"
    exit 1
}

# Success!
Write-Host ""
Write-Host "====================================" -ForegroundColor Green
Write-Host "BUILD SUCCESSFUL!" -ForegroundColor Green
Write-Host "====================================" -ForegroundColor Green
Write-Host ""
Write-Host "Next steps:" -ForegroundColor Cyan
Write-Host "1. Run tests: mvn test" -ForegroundColor White
Write-Host "2. View report: start test-results\reports\ExtentReport_*.html" -ForegroundColor White
Write-Host ""
Read-Host "Press Enter to exit"
