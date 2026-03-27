# JWT Authentication & Swagger API Documentation

## Overview

This project provides JWT (JSON Web Token) authentication integrated with Swagger/OpenAPI documentation. JWT is used for stateless authentication, perfect for REST APIs and microservices.

## Table of Contents

1. [Quick Start](#quick-start)
2. [JWT Authentication](#jwt-authentication)
3. [Swagger Documentation](#swagger-documentation)
4. [API Endpoints](#api-endpoints)
5. [Testing the API](#testing-the-api)
6. [Configuration](#configuration)
7. [Security Best Practices](#security-best-practices)

---

## Quick Start

### 1. Run the Application

```bash
# Using Maven
mvn spring-boot:run -Dspring-boot.run.arguments="--spring.profiles.active=dev"

# Or with Docker Compose
docker-compose up -d
```

### 2. Access Swagger UI

Open your browser and navigate to:

```
http://localhost:8080/swagger-ui.html
```

You should see the interactive API documentation with all endpoints.

### 3. Login and Get JWT Token

Use the **Authentication** section in Swagger UI:

1. Click on **POST /api/auth/login**
2. Click **Try it out**
3. Enter credentials:
   ```json
   {
     "username": "admin",
     "password": "admin123"
   }
   ```
4. Click **Execute**
5. Copy the token from the response

### 4. Authorize Future Requests

In Swagger UI:

1. Click the **Authorize** button (top-right)
2. Enter the token in the format: `Bearer <your_token>`
3. Click **Authorize**
4. All subsequent requests will include the JWT token

---

## JWT Authentication

### What is JWT?

JWT (JSON Web Token) is a compact, self-contained way of transmitting information between parties as a JSON object. It consists of three parts:

- **Header**: Token type and hashing algorithm
- **Payload**: Claims (user information)
- **Signature**: Cryptographic signature for verification

Example Token Structure:
```
eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJhZG1pbiIsImlhdCI6MTcxNDI1ODAwMCwiZXhwIjoxNzE0MjYxNjAwfQ.signature
```

### Token Components

**Header (decoded):**
```json
{
  "alg": "HS512",
  "typ": "JWT"
}
```

**Payload (decoded):**
```json
{
  "sub": "admin",
  "iat": 1714258000,
  "exp": 1714261600
}
```

### Configuration

Update `application.properties`:

```properties
# JWT Secret (minimum 32 characters for HS512)
jwt.secret=MyVerySecureJWTSecretKeyWithMinimum32CharactersForHS512Algorithm

# Token expiration in milliseconds (3600000 = 1 hour)
jwt.expiration=3600000
```

⚠️ **Important**: In production, use environment variables:
```bash
export JWT_SECRET="your-secure-secret-key"
export JWT_EXPIRATION=3600000
```

### Default Credentials

For development and testing:

| Username | Password | Role |
|----------|----------|------|
| admin | admin123 | ADMIN, USER |
| user | user123 | USER |

---

## Swagger Documentation

### Access Points

- **Swagger UI**: `http://localhost:8080/swagger-ui.html`
- **API Docs JSON**: `http://localhost:8080/v3/api-docs`
- **API Docs YAML**: `http://localhost:8080/v3/api-docs.yaml`

### Features

✅ Interactive API testing  
✅ Request/Response examples  
✅ JWT authentication integration  
✅ Auto-generated API documentation  
✅ Schema definitions for all DTOs  

### Swagger Configuration

Located in [OpenApiConfig.java](src/main/java/com/example/ticketrisingsystem/config/OpenApiConfig.java):

```java
@Configuration
@SecurityScheme(
    name = "bearer_token",
    type = SecuritySchemeType.HTTP,
    scheme = "bearer",
    bearerFormat = "JWT",
    description = "JWT authentication token"
)
public class OpenApiConfig {
    // Configuration details
}
```

---

## API Endpoints

### Authentication Endpoints

#### 1. Login
- **Endpoint**: `POST /api/auth/login`
- **Description**: Authenticate user and get JWT token
- **Request Body**:
  ```json
  {
    "username": "admin",
    "password": "admin123"
  }
  ```
- **Response** (200 OK):
  ```json
  {
    "token": "eyJhbGciOiJIUzUxMiJ9...",
    "type": "Bearer",
    "username": "admin",
    "expiresIn": 3600000
  }
  ```

#### 2. Validate Token
- **Endpoint**: `GET /api/auth/validate`
- **Description**: Check if JWT token is valid
- **Headers**:
  ```
  Authorization: Bearer <token>
  ```
- **Response** (200 OK):
  ```json
  {
    "valid": true,
    "username": "admin"
  }
  ```

#### 3. Refresh Token
- **Endpoint**: `POST /api/auth/refresh`
- **Description**: Refresh an existing JWT token
- **Headers**:
  ```
  Authorization: Bearer <token>
  ```
- **Response** (200 OK):
  ```json
  {
    "token": "eyJhbGciOiJIUzUxMiJ9...",
    "type": "Bearer",
    "username": "admin",
    "expiresIn": 3600000
  }
  ```

### Ticket Endpoints

#### 1. Health Check
- **Endpoint**: `GET /api/tickets/health`
- **Description**: Check if API is running
- **Authentication**: Not required
- **Response** (200 OK):
  ```
  Ticket Rising System API is running
  ```

#### 2. Get All Tickets
- **Endpoint**: `GET /api/tickets`
- **Description**: Retrieve all tickets
- **Authentication**: Required (JWT Token)
- **Response** (200 OK):
  ```json
  {
    "message": "All tickets retrieved successfully",
    "count": 0,
    "data": []
  }
  ```

#### 3. Get Ticket by ID
- **Endpoint**: `GET /api/tickets/{id}`
- **Description**: Retrieve specific ticket
- **Authentication**: Required (JWT Token)
- **Path Parameters**: `id` (Long)
- **Response** (200 OK):
  ```
  Ticket with ID: 1
  ```

---

## Testing the API

### Using Swagger UI (Recommended)

1. Open `http://localhost:8080/swagger-ui.html`
2. Click **Authorize** button
3. Login to get JWT token
4. Use token for authenticated requests

### Using cURL

#### Login and Get Token
```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"admin","password":"admin123"}'
```

Response:
```json
{
  "token": "eyJhbGciOiJIUzUxMiJ9...",
  "type": "Bearer",
  "username": "admin",
  "expiresIn": 3600000
}
```

#### Get All Tickets (with JWT Token)
```bash
TOKEN="<your-token-here>"
curl -X GET http://localhost:8080/api/tickets \
  -H "Authorization: Bearer $TOKEN"
```

#### Validate Token
```bash
TOKEN="<your-token-here>"
curl -X GET http://localhost:8080/api/auth/validate \
  -H "Authorization: Bearer $TOKEN"
```

### Using Postman

1. **Create Environment Variable**:
   - Variable: `token`
   - Value: Leave empty (will be set after login)

2. **Create Login Request**:
   - Method: `POST`
   - URL: `http://localhost:8080/api/auth/login`
   - Body (JSON):
     ```json
     {
       "username": "admin",
       "password": "admin123"
     }
     ```
   - Scripts (Tests tab):
     ```javascript
     var jsonData = pm.response.json();
     pm.environment.set("token", jsonData.token);
     ```

3. **Create Authenticated Request** (Get Tickets):
   - Method: `GET`
   - URL: `http://localhost:8080/api/tickets`
   - Headers:
     - Key: `Authorization`
     - Value: `Bearer {{token}}`

---

## Configuration

### JWT Secret Key

The JWT secret key must be:
- **Minimum 32 characters** for HS512 algorithm
- **Unique and secure**
- **Stored securely** (not in code for production)

Generate a secure key:
```bash
# Using OpenSSL
openssl rand -base64 32

# Using Python
python3 -c "import secrets; print(secrets.token_urlsafe(32))"
```

### Environment Variables

For production deployment:

```bash
# .env file or deployment configuration
JWT_SECRET=your-generated-secret-key-here
JWT_EXPIRATION=3600000
SWAGGER_ENABLED=false
SPRING_PROFILES_ACTIVE=prod
```

### Application Profiles

#### Development Profile
```bash
mvn spring-boot:run -Dspring-boot.run.arguments="--spring.profiles.active=dev"
```

#### Production Profile
```bash
mvn spring-boot:run -Dspring-boot.run.arguments="--spring.profiles.active=prod"
```

---

## Security Best Practices

### 1. ✅ Keep JWT Secret Secure
```
❌ DON'T: Store secret in code
✅ DO: Use environment variables or secret management tools
```

### 2. ✅ Use HTTPS in Production
```
❌ DON'T: Send tokens over HTTP
✅ DO: Always use HTTPS/TLS for token transmission
```

### 3. ✅ Set Appropriate Token Expiration
```
❌ DON'T: Set tokens to never expire
✅ DO: Use reasonable expiration (1-24 hours)
```

### 4. ✅ Implement Token Refresh
```
❌ DON'T: Make users re-login when token expires
✅ DO: Implement token refresh endpoint
```

### 5. ✅ Validate Tokens on Each Request
```
❌ DON'T: Trust unvalidated tokens
✅ DO: Always validate token signature and expiration
```

### 6. ✅ Use Strong Password Hashing
```
bcrypt: ✅ Recommended
MD5: ❌ Avoid
SHA256: ⚠️ Only with salt
```

### 7. ✅ Disable Swagger in Production
```properties
# production
springdoc.swagger-ui.enabled=false
```

---

## Troubleshooting

### Issue: "Invalid JWT signature"
- **Cause**: JWT secret key mismatch
- **Solution**: Ensure same secret key in config

### Issue: "JWT claims string is empty"
- **Cause**: Token not provided in Authorization header
- **Solution**: Include `Authorization: Bearer <token>` header

### Issue: "Expired JWT token"
- **Cause**: Token has expired
- **Solution**: Use `/api/auth/refresh` endpoint to get new token

### Issue: "Swagger UI not loading"
- **Cause**: Swagger disabled or misconfigured
- **Solution**: Check `springdoc.swagger-ui.enabled=true`

---

## Additional Resources

- [JWT.io - Debugger](https://jwt.io/)
- [OpenAPI 3.0 Specification](https://spec.openapis.org/oas/v3.0.3)
- [Spring Security Documentation](https://spring.io/projects/spring-security)
- [Springdoc-OpenAPI Documentation](https://springdoc.org/)

---

**Last Updated**: March 2026
