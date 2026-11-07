@echo off
REM ============================================================
REM Maven Build Fix Script for Selenium Capstone
REM This script clears Maven cache and rebuilds the project
REM ============================================================

echo.
echo ====================================
echo Maven Build Fix Script
echo ====================================
echo.

REM Step 1: Display current Java version
echo [1/5] Checking Java version...
java -version
echo.

REM Step 2: Clear Maven cache
echo [2/5] Clearing Maven repository cache...
if exist "%USERPROFILE%\.m2\repository" (
    echo Removing %USERPROFILE%\.m2\repository
    rmdir /s /q "%USERPROFILE%\.m2\repository"
    echo Cache cleared successfully!
) else (
    echo No existing cache to clear.
)
echo.

REM Step 3: Check if pom.xml exists
echo [3/5] Verifying project structure...
if not exist "pom.xml" (
    echo ERROR: pom.xml not found in current directory!
    echo Please run this script from the project root directory.
    pause
    exit /b 1
)
echo pom.xml found - OK
echo.

REM Step 4: Run Maven clean
echo [4/5] Running Maven clean...
call mvn clean
if errorlevel 1 (
    echo ERROR: Maven clean failed!
    pause
    exit /b 1
)
echo.

REM Step 5: Run Maven install with updates
echo [5/5] Installing dependencies (this may take a few minutes)...
call mvn install -U
if errorlevel 1 (
    echo.
    echo ERROR: Maven install failed!
    echo.
    echo Troubleshooting steps:
    echo 1. Clear Maven cache: rmdir /s /q "%%USERPROFILE%%\.m2\repository"
    echo 2. Retry: mvn clean install -U
    echo 3. With settings: mvn clean install --settings maven-settings.xml
    echo 4. Debug mode: mvn clean install -U -X
    echo.
    echo If you see "module jdk.compiler" error:
    echo   This is Java 17 module system issue - already fixed in pom.xml
    echo   Try: mvn clean compile (just compile, don't install)
    echo.
    pause
    exit /b 1
)

REM Success!
echo.
echo ====================================
echo BUILD SUCCESSFUL!
echo ====================================
echo.
echo Next steps:
echo 1. Run tests: mvn test
echo 2. View report: start test-results\reports\ExtentReport_*.html
echo.
pause
