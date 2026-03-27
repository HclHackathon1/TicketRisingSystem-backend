# SSL/TLS Configuration Guide

## Overview

This guide covers how to set up and use SSL/TLS (HTTPS) with your Ticket Rising System Spring Boot application.

## Table of Contents

1. [Quick Start](#quick-start)
2. [SSL Certificate Generation](#ssl-certificate-generation)
3. [Configuration](#configuration)
4. [Running with SSL](#running-with-ssl)
5. [Testing HTTPS](#testing-https)
6. [Troubleshooting](#troubleshooting)
7. [Production Setup](#production-setup)

---

## Quick Start

### For Development (HTTP - No SSL)

```bash
# HTTP on port 8080 (default)
mvn spring-boot:run
```

Access: `http://localhost:8080/swagger-ui.html`

### For Development (HTTPS with Self-Signed Certificate)

```bash
# 1. Generate self-signed certificate (Windows)
generate-ssl-certificate.bat

# OR (Linux/Mac)
bash generate-ssl-certificate.sh

# 2. Run with SSL profile
mvn spring-boot:run -Dspring-boot.run.arguments="--spring.profiles.active=ssl"
```

Access: `https://localhost:8443/swagger-ui.html`

---

## SSL Certificate Generation

### Windows

1. **Navigate to project directory**:
   ```powershell
   cd C:\Users\Aadhitya\Downloads\TicketRisingSystem
   ```

2. **Run the certificate generation script**:
   ```powershell
   .\generate-ssl-certificate.bat
   ```

3. **Or manually using keytool**:
   ```powershell
   keytool -genkey -alias tomcat `
     -keyalg RSA `
     -keysize 2048 `
     -keystore src\main\resources\keystore.p12 `
     -keypass changeit `
     -storepass changeit `
     -validity 365 `
     -dname "CN=localhost,OU=Development,O=Ticket Rising System,L=Local,ST=State,C=US" `
     -ext "SAN=DNS:localhost,DNS:127.0.0.1,IP:127.0.0.1"
   ```

### Linux/Mac

1. **Navigate to project directory**:
   ```bash
   cd ~/Downloads/TicketRisingSystem
   ```

2. **Run the certificate generation script**:
   ```bash
   bash generate-ssl-certificate.sh
   ```

3. **Or manually using keytool**:
   ```bash
   keytool -genkey -alias tomcat \
     -keyalg RSA \
     -keysize 2048 \
     -keystore src/main/resources/keystore.p12 \
     -keypass changeit \
     -storepass changeit \
     -validity 365 \
     -dname "CN=localhost,OU=Development,O=Ticket Rising System,L=Local,ST=State,C=US" \
     -ext "SAN=DNS:localhost,DNS:127.0.0.1,IP:127.0.0.1"
   ```

### Certificate Details

- **Alias**: `tomcat`
- **Keystore Type**: PKCS12 (.p12 file)
- **Keystore Password**: `changeit`
- **Validity**: 365 days
- **Algorithm**: RSA 2048-bit
- **Location**: `src/main/resources/keystore.p12`

### Verify Certificate

```bash
keytool -list -v -keystore src/main/resources/keystore.p12 -storepass changeit
```

---

## Configuration

### Application Profiles

#### 1. Development (HTTP) - `application-dev.properties`

```properties
server.port=8080
spring.jpa.hibernate.ddl-auto=create-drop
logging.level.com.example.ticketrisingsystem=DEBUG
```

Run with: `--spring.profiles.active=dev`

#### 2. SSL Development (HTTPS) - `application-ssl.properties`

```properties
server.port=8443
server.ssl.key-store=classpath:keystore.p12
server.ssl.key-store-password=changeit
server.ssl.key-store-type=PKCS12
server.ssl.enabled=true
server.http2.enabled=true
```

Run with: `--spring.profiles.active=ssl`

#### 3. Production (HTTPS) - `application-prod.properties`

```properties
server.port=8443
server.ssl.key-store=${SSL_KEYSTORE_PATH:classpath:keystore.p12}
server.ssl.key-store-password=${SSL_KEYSTORE_PASSWORD:changeit}
server.ssl.enabled=true
spring.jpa.hibernate.ddl-auto=validate
logging.level.root=WARN
```

Run with: `--spring.profiles.active=prod`

---

## Running with SSL

### Using Maven

#### Development (HTTP)
```bash
mvn spring-boot:run -Dspring-boot.run.arguments="--spring.profiles.active=dev"
```

#### Development (HTTPS)
```bash
mvn spring-boot:run -Dspring-boot.run.arguments="--spring.profiles.active=ssl"
```

#### Production (HTTPS)
```bash
mvn spring-boot:run -Dspring-boot.run.arguments="--spring.profiles.active=prod"
```

### Using Docker Compose

```bash
# Build image
docker build -t ticket-rising-system:latest .

# Run with SSL (production configuration in docker-compose can be updated)
docker-compose up -d
```

### Running JAR Directly

```bash
# Build JAR
mvn clean package

# Run with SSL profile
java -jar target/TicketRisingSystem-0.0.1-SNAPSHOT.jar \
  --spring.profiles.active=ssl \
  --server.port=8443
```

---

## Testing HTTPS

### Using Browser

1. **Navigate to**: `https://localhost:8443/swagger-ui.html`
2. **Browser Warning**: You'll see a security warning because the certificate is self-signed
3. **Proceed**: Click "Advanced" and "Proceed to localhost"

### Using cURL

```bash
# Ignore self-signed certificate warning
curl -k -X POST https://localhost:8443/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"admin","password":"admin123"}'

# Or with certificate verification (production)
curl -X POST https://yourdomain.com:8443/api/auth/login \
  --cacert /path/to/ca-cert.pem \
  -H "Content-Type: application/json" \
  -d '{"username":"admin","password":"admin123"}'
```

### Using Postman

1. **Disable SSL verification** (for self-signed certs):
   - Settings → General → SSL certificate verification: **OFF**

2. **Create request**:
   - Method: `POST`
   - URL: `https://localhost:8443/api/auth/login`
   - Body (JSON):
     ```json
     {
       "username": "admin",
       "password": "admin123"
     }
     ```

### Using Java Client

```java
import javax.net.ssl.SSLContext;
import org.apache.http.ssl.SSLContexts;

// For development (ignore certificate verification)
SSLContext sslContext = SSLContexts.custom()
    .loadTrustMaterial(null, (chain, authType) -> true)
    .build();

// Use sslContext in HTTP client configuration
```

---

## Troubleshooting

### Issue: Certificate Not Found

**Error**: `FileNotFoundException: class path resource [keystore.p12] cannot be opened`

**Solution**:
1. Run certificate generation script
2. Verify file exists: `src/main/resources/keystore.p12`
3. Rebuild: `mvn clean package`

### Issue: Port Already in Use

**Error**: `Address already in use`

**Solution**:
```bash
# Find process using port 8443
netstat -ano | findstr :8443

# Kill process (Windows)
taskkill /PID <PID> /F

# Or change port in application properties
server.port=8444
```

### Issue: Self-Signed Certificate Warning

This is **normal and expected** for development.

**For development**: Use browser's "Proceed Anyway" option

**For production**: Use certificate from trusted Certificate Authority (Let's Encrypt, DigiCert, etc.)

### Issue: Wrong Password

**Error**: `Cannot recover key`

**Solution**:
1. Delete keystore file
2. Regenerate with correct password
3. Update `server.ssl.key-store-password` in config

### Issue: TLS Handshake Failure

**Error**: `javax.net.ssl.SSLHandshakeException`

**Solution**:
1. Verify certificate is valid: `keytool -list -v -keystore keystore.p12 -storepass changeit`
2. Rebuild application: `mvn clean package`
3. Restart application

---

## Production Setup

### 1. Obtain Production Certificate

Use a trusted Certificate Authority:

- **Let's Encrypt** (Free): `https://letsencrypt.org`
- **DigiCert**, **Sectigo**, **GlobalSign** (Commercial)

### 2. Convert PEM to PKCS12

```bash
openssl pkcs12 -export \
  -in certificate.crt \
  -inkey private-key.key \
  -out keystore.p12 \
  -name tomcat
```

### 3. Update Configuration

```properties
# application-prod.properties
server.ssl.key-store=/secure/path/to/keystore.p12
server.ssl.key-store-password=${SSL_KEYSTORE_PASSWORD}
```

### 4. Set Environment Variables

```bash
export SSL_KEYSTORE_PATH=/secure/path/to/keystore.p12
export SSL_KEYSTORE_PASSWORD=your-secure-password
export JWT_SECRET=your-256-bit-secret
export SPRING_PROFILES_ACTIVE=prod
```

### 5. Run with Production Profile

```bash
mvn clean package
java -jar target/TicketRisingSystem-0.0.1-SNAPSHOT.jar
```

### 6. Security Best Practices

✅ **DO**:
- Use HTTPS in production (port 8443)
- Keep keystore password secure (use environment variables)
- Rotate certificates annually
- Use strong algorithms (TLS 1.2+)
- Enable HTTP/2
- Enable compression for responses

❌ **DON'T**:
- Store passwords in code
- Use self-signed certificates in production
- Use outdated TLS versions (< 1.2)
- Expose keystore files
- Share SSL certificates

---

## Monitoring SSL

### Check Certificate Expiration

```bash
keytool -list -keystore src/main/resources/keystore.p12 -storepass changeit | grep "Valid from"
```

### Test SSL Configuration

```bash
# Test TLS version and cipher suites
openssl s_client -connect localhost:8443 -tls1_2

# Or using testssl.sh
bash testssl.sh https://localhost:8443
```

### Logs

Monitor for SSL-related errors:

```bash
# Check Spring Boot logs
tail -f logs/application.log | grep SSL

# In application.properties
logging.level.org.springframework.boot.web.server=DEBUG
```

---

## Additional Resources

- [SSL/TLS Best Practices](https://cheatsheetseries.owasp.org/cheatsheets/Transport_Layer_Protection_Cheat_Sheet.html)
- [Keytool Documentation](https://docs.oracle.com/javase/8/docs/technotes/tools/unix/keytool.html)
- [Spring Boot SSL Documentation](https://spring.io/blog/2014/12/01/use-tomcat-behind-an-https-proxy-to-handle-ssl-termination)
- [Let's Encrypt](https://letsencrypt.org)
- [Mozilla SSL Configuration](https://ssl-config.mozilla.org/)

---

**Last Updated**: March 2026
