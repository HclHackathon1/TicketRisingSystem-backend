#!/bin/bash

# SSL Certificate Generation Script
# This script generates a self-signed certificate for development/testing purposes

set -e

# Configuration
KEYSTORE_PATH="src/main/resources/keystore.p12"
KEYSTORE_PASSWORD="changeit"
ALIAS="tomcat"
VALIDITY=365  # Days
KEYSIZE=2048

# Colors for output
GREEN='\033[0;32m'
BLUE='\033[0;34m'
YELLOW='\033[1;33m'
NC='\033[0m' # No Color

echo -e "${BLUE}========================================${NC}"
echo -e "${BLUE}SSL Certificate Generation Script${NC}"
echo -e "${BLUE}========================================${NC}"
echo

# Check if keytool is available
if ! command -v keytool &> /dev/null; then
    echo -e "${YELLOW}keytool not found. Please install Java or ensure JAVA_HOME is set.${NC}"
    exit 1
fi

# Remove existing keystore if it exists
if [ -f "$KEYSTORE_PATH" ]; then
    echo -e "${YELLOW}Found existing keystore at $KEYSTORE_PATH${NC}"
    read -p "Do you want to replace it? (y/n) " -n 1 -r
    echo
    if [[ $REPLY =~ ^[Yy]$ ]]; then
        rm -f "$KEYSTORE_PATH"
        echo -e "${GREEN}Removed existing keystore${NC}"
    else
        echo -e "${YELLOW}Using existing keystore${NC}"
        exit 0
    fi
fi

# Generate self-signed certificate
echo -e "${BLUE}Generating self-signed certificate...${NC}"
keytool -genkey -alias "$ALIAS" \
    -keyalg RSA \
    -keysize "$KEYSIZE" \
    -keystore "$KEYSTORE_PATH" \
    -keypass "$KEYSTORE_PASSWORD" \
    -storepass "$KEYSTORE_PASSWORD" \
    -validity "$VALIDITY" \
    -dname "CN=localhost,OU=Development,O=Ticket Rising System,L=Local,ST=State,C=US" \
    -ext "SAN=DNS:localhost,DNS:127.0.0.1,IP:127.0.0.1"

echo -e "${GREEN}✓ Keystore generated successfully at: $KEYSTORE_PATH${NC}"
echo

# Display certificate information
echo -e "${BLUE}Certificate Information:${NC}"
keytool -list -v -keystore "$KEYSTORE_PATH" -storepass "$KEYSTORE_PASSWORD"

echo
echo -e "${GREEN}========================================${NC}"
echo -e "${GREEN}Configuration:${NC}"
echo -e "${GREEN}  Keystore Path: $KEYSTORE_PATH${NC}"
echo -e "${GREEN}  Keystore Password: $KEYSTORE_PASSWORD${NC}"
echo -e "${GREEN}  Alias: $ALIAS${NC}"
echo -e "${GREEN}  Port (SSL): 8443${NC}"
echo -e "${GREEN}========================================${NC}"
echo

# Instructions
echo -e "${BLUE}Usage:${NC}"
echo "1. Run with SSL (development):"
echo "   mvn spring-boot:run -Dspring-boot.run.arguments='--spring.profiles.active=ssl'"
echo
echo "2. Run with SSL (production):"
echo "   mvn spring-boot:run -Dspring-boot.run.arguments='--spring.profiles.active=prod'"
echo
echo "3. Access the application at:"
echo "   https://localhost:8443/swagger-ui.html"
echo
echo -e "${YELLOW}Note: This certificate is self-signed. Browsers will show a security warning.${NC}"
echo -e "${YELLOW}For production, use a certificate from a trusted Certificate Authority.${NC}"
