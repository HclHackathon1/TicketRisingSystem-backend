# Quick Reference - JWT & Swagger Setup

## 📋 What Was Added

### Dependencies (pom.xml)
- ✅ Spring Security for authentication
- ✅ JJWT (JSON Web Token) library
- ✅ Springdoc OpenAPI (already present) with JWT schema

### Configuration Files
- ✅ `SecurityConfig.java` - Spring Security configuration
- ✅ `OpenApiConfig.java` - Swagger/OpenAPI with JWT schema
- ✅ `JwtTokenProvider.java` - JWT token generation and validation
- ✅ `JwtAuthenticationFilter.java` - JWT authentication filter

### Controllers
- ✅ `AuthController.java` - Login, token refresh, validation endpoints
- ✅ `TicketController.java` - Sample protected endpoints

### DTOs
- ✅ `LoginRequest.java` - Login request schema
- ✅ `TokenResponse.java` - JWT token response schema

### Configuration
- ✅ `application.properties` - Main configuration
- ✅ `application-dev.properties` - Development profile
- ✅ `application-prod.properties` - Production profile

---

## 🚀 Getting Started (30 seconds)

### Step 1: Build the Project
```bash
cd c:\Users\Aadhitya\Downloads\TicketRisingSystem
mvn clean install
```

### Step 2: Run the Application
```bash
mvn spring-boot:run
```

### Step 3: Open Swagger UI
```
http://localhost:8080/swagger-ui.html
```

### Step 4: Login and Test
1. Expand **Authentication** section
2. Click **POST /api/auth/login**
3. Click **Try it out**
4. Use credentials: `admin` / `admin123`
5. Copy the token
6. Click **Authorize** button (top right)
7. Enter: `Bearer <your_token>`
8. Test other endpoints

---

## 🔐 JWT Token Structure

**Example Token:**
```
eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJhZG1pbiIsImlhdCI6MTcxNDI1ODAwMCwiZXhwIjoxNzE0MjYxNjAwfQ.signature
```

**Decoded Payload:**
```json
{
  "sub": "admin",
  "iat": 1714258000,
  "exp": 1714261600
}
```

---

## 📊 Key Configuration Properties

| Property | Value | Description |
|----------|-------|-------------|
| `jwt.secret` | 32+ chars | Secret for signing tokens |
| `jwt.expiration` | 3600000 | Token validity (1 hour) |
| `springdoc.swagger-ui.enabled` | true | Enable Swagger UI |
| `spring.security.filter.order` | 5 | Security filter position |

---

## 👤 Default Users

| Username | Password | Roles |
|----------|----------|-------|
| admin | admin123 | ADMIN, USER |
| user | user123 | USER |

---

## 🔌 API Endpoints

### Public Endpoints (No Auth Required)
```
POST   /api/auth/login              - Get JWT token
GET    /api/tickets/health          - Health check
GET    /v3/api-docs                 - OpenAPI JSON
GET    /swagger-ui.html             - Swagger UI
```

### Protected Endpoints (JWT Required)
```
GET    /api/auth/validate           - Validate token
POST   /api/auth/refresh            - Refresh token
GET    /api/tickets                 - Get all tickets
GET    /api/tickets/{id}            - Get ticket by ID
```

---

## 🧪 Testing Quick Commands

### Login
```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"admin","password":"admin123"}'
```

### Get Tickets (with token)
```bash
curl -X GET http://localhost:8080/api/tickets \
  -H "Authorization: Bearer <token>"
```

### Validate Token
```bash
curl -X GET http://localhost:8080/api/auth/validate \
  -H "Authorization: Bearer <token>"
```

---

## 📦 Postman Collection

Import file: `postman_collection.json`

**Steps:**
1. Open Postman
2. Click **Import**
3. Select `postman_collection.json`
4. Environment variables auto-saved after login
5. All endpoints ready to use

---

## 🔒 Security Checklist

- [ ] Change `jwt.secret` in production
- [ ] Use environment variables for secrets
- [ ] Enable HTTPS/TLS for production
- [ ] Disable Swagger UI in production
- [ ] Set appropriate token expiration
- [ ] Rotate JWT secrets regularly
- [ ] Implement rate limiting
- [ ] Monitor authentication failures

---

## 📚 Documentation Files

| File | Purpose |
|------|---------|
| `JWT_SWAGGER_GUIDE.md` | Comprehensive JWT & Swagger guide |
| `README.md` | Project overview and setup |
| `CONTRIBUTING.md` | Contribution guidelines |
| `postman_collection.json` | Postman API collection |

---

## 🐛 Common Issues

### Issue: "Invalid JWT signature"
**Solution**: Check JWT secret matches in config and token validation

### Issue: "Token not found"
**Solution**: Include `Authorization: Bearer <token>` header

### Issue: "Swagger not showing security"
**Solution**: Make sure `SecurityScheme` annotation is present

---

## 📝 Next Steps

1. ✅ Configure database connection
2. ✅ Create Ticket entity and repository
3. ✅ Implement ticket CRUD operations
4. ✅ Create user management endpoints
5. ✅ Add logging and monitoring
6. ✅ Deploy to production

---

**All JWT and Swagger components are ready to use!** 🎉

For detailed documentation, see: `JWT_SWAGGER_GUIDE.md`
