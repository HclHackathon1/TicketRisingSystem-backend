@echo off
REM SSL Certificate Generation Script for Windows
REM This script generates a self-signed certificate for development/testing purposes

setlocal enabledelayedexpansion

REM Configuration
set KEYSTORE_PATH=src\main\resources\keystore.p12
set KEYSTORE_PASSWORD=changeit
set ALIAS=tomcat
set VALIDITY=365
set KEYSIZE=2048

echo.
echo ========================================
echo SSL Certificate Generation Script
echo ========================================
echo.

REM Check if keytool is available
keytool -version >nul 2>&1
if errorlevel 1 (
    echo [ERROR] keytool not found. Please install Java or ensure JAVA_HOME is set.
    pause
    exit /b 1
)

REM Remove existing keystore if it exists
if exist "%KEYSTORE_PATH%" (
    echo [WARNING] Found existing keystore at %KEYSTORE_PATH%
    setlocal disabledelayedexpansion
    set /p REPLACE="Do you want to replace it? (y/n): "
    setlocal enabledelayedexpansion
    if /i "!REPLACE!"=="y" (
        del "%KEYSTORE_PATH%"
        echo [INFO] Removed existing keystore
    ) else (
        echo [INFO] Using existing keystore
        exit /b 0
    )
)

REM Generate self-signed certificate
echo [INFO] Generating self-signed certificate...
keytool -genkey -alias %ALIAS% ^
    -keyalg RSA ^
    -keysize %KEYSIZE% ^
    -keystore "%KEYSTORE_PATH%" ^
    -keypass %KEYSTORE_PASSWORD% ^
    -storepass %KEYSTORE_PASSWORD% ^
    -validity %VALIDITY% ^
    -dname "CN=localhost,OU=Development,O=Ticket Rising System,L=Local,ST=State,C=US" ^
    -ext "SAN=DNS:localhost,DNS:127.0.0.1,IP:127.0.0.1"

if errorlevel 1 (
    echo [ERROR] Failed to generate certificate
    pause
    exit /b 1
)

echo.
echo [SUCCESS] Keystore generated successfully at: %KEYSTORE_PATH%
echo.

REM Display certificate information
echo [INFO] Certificate Information:
keytool -list -v -keystore "%KEYSTORE_PATH%" -storepass %KEYSTORE_PASSWORD%

echo.
echo ========================================
echo Configuration:
echo   Keystore Path: %KEYSTORE_PATH%
echo   Keystore Password: %KEYSTORE_PASSWORD%
echo   Alias: %ALIAS%
echo   Port (SSL): 8443
echo ========================================
echo.

REM Instructions
echo [INFO] Usage:
echo.
echo 1. Run with SSL (development):
echo    mvn spring-boot:run -Dspring-boot.run.arguments="--spring.profiles.active=ssl"
echo.
echo 2. Run with SSL (production):
echo    mvn spring-boot:run -Dspring-boot.run.arguments="--spring.profiles.active=prod"
echo.
echo 3. Access the application at:
echo    https://localhost:8443/swagger-ui.html
echo.
echo [WARNING] This certificate is self-signed. Browsers will show a security warning.
echo [WARNING] For production, use a certificate from a trusted Certificate Authority.
echo.

pause
