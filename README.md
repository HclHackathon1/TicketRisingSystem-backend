# Ticket Rising System

A Spring Boot application for managing and tracking ticket escalation workflows.

## Project Information

- **Java Version**: 21 (LTS)
- **Spring Boot Version**: 4.0.5
- **Build Tool**: Maven

## Features

- Ticket management with JPA/Hibernate
- RESTful API with Spring Web MVC
- OpenAPI/Swagger documentation
- Database integration with Spring Data JDBC and JPA

## Prerequisites

- Java 21 (LTS) or higher
- Maven 3.8+
- Database (configured in application.properties)

## Getting Started

### 1. Clone the Repository

```bash
git clone <repository-url>
cd TicketRisingSystem
```

### 2. Configure Environment

Copy the `.env.example` file to `.env` and configure your environment variables:

```bash
cp .env.example .env
```

Edit `.env` with your specific configuration values.

### 3. Build the Project

```bash
mvn clean install
```

Or use the Maven wrapper:

```bash
./mvnw clean install
```

### 4. Run the Application

```bash
mvn spring-boot:run
```

Or:

```bash
./mvnw spring-boot:run
```

The application will start on `http://localhost:8080`

## API Documentation

Once the application is running, access the Swagger UI at:

```
http://localhost:8080/swagger-ui.html
```

## Project Structure

```
src/
├── main/
│   ├── java/
│   │   └── com/example/ticketrisingsystem/
│   │       └── TicketRisingSystemApplication.java
│   └── resources/
│       ├── application.properties
│       ├── static/
│       └── templates/
└── test/
    └── java/
        └── com/example/ticketrisingsystem/
            └── TicketRisingSystemApplicationTests.java
```

## Database Configuration

Configure your database in `src/main/resources/application.properties`:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/ticket_rising_system
spring.datasource.username=root
spring.datasource.password=your_password
spring.jpa.hibernate.ddl-auto=update
```

## Docker

### Build Docker Image

```bash
docker build -t ticket-rising-system:latest .
```

### Run with Docker Compose

```bash
docker-compose up -d
```

## Development Tools

- **IDE**: IntelliJ IDEA or Spring Tool Suite (STS)
- **Code Formatting**: See `.editorconfig` for consistency rules
- **Testing**: JUnit 5 and Spring Test

## Testing

Run all tests:

```bash
mvn test
```

Or:

```bash
./mvnw test
```

## Building for Production

```bash
mvn clean package -DskipTests
```

This creates a JAR file in the `target/` directory.

## Contributing

Please follow the guidelines in `CONTRIBUTING.md` (if available).

## License

This project is licensed under the MIT License - see the LICENSE file for details.

## Support

For issues, questions, or suggestions, please open an issue on the repository.

---

**Last Updated**: March 2026
