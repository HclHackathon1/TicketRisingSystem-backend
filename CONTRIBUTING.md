# Contributing to Ticket Rising System

Thank you for your interest in contributing to the Ticket Rising System project! This document provides guidelines and instructions for contributing.

## Getting Started

1. **Fork the repository** on GitHub
2. **Clone your fork** locally
3. **Create a new branch** for your changes: `git checkout -b feature/your-feature-name`
4. **Make your changes** following the code guidelines
5. **Commit your changes** with clear, descriptive messages
6. **Push to your fork** and **submit a pull request**

## Code Guidelines

### Java Code Style

- **Indentation**: Use 4 spaces (configured in `.editorconfig`)
- **Line Length**: Keep lines under 120 characters
- **Naming Conventions**:
  - Classes: `PascalCase`
  - Methods & Variables: `camelCase`
  - Constants: `UPPER_SNAKE_CASE`
- **Formatting**: Use your IDE's auto-format feature (Ctrl+Shift+F in IntelliJ IDEA)

### Commit Messages

- Use clear, descriptive commit messages
- Start with a verb in imperative mood (e.g., "Add", "Fix", "Update")
- Format: `<type>(<scope>): <description>`
- Types: `feat`, `fix`, `docs`, `style`, `refactor`, `test`, `chore`

Examples:
```
feat(api): Add ticket escalation endpoint
fix(database): Resolve connection pool leak
docs(readme): Update installation instructions
```

## Pull Request Process

1. **Update the README.md** if documentation changes are needed
2. **Add tests** for any new functionality
3. **Ensure all tests pass**: `mvn test`
4. **Ensure code builds**: `mvn clean package`
5. **Provide a clear description** of your changes in the PR

## Testing Requirements

- Write unit tests for all new features
- Write integration tests for critical functionality
- Ensure test coverage doesn't decrease
- Run tests before submitting: `mvn test`

## Running Tests

```bash
# Run all tests
mvn test

# Run a specific test class
mvn test -Dtest=YourTestClass

# Run with coverage
mvn test jacoco:report
```

## Code Review

- Be open to feedback and suggestions
- Respond promptly to review comments
- Make requested changes in new commits (don't force-push unless asked)
- Keep discussions professional and constructive

## Reporting Bugs

When reporting bugs, include:

- Clear description of the issue
- Steps to reproduce
- Expected behavior
- Actual behavior
- Environment details (Java version, OS, etc.)
- Error messages or logs

## Feature Requests

When suggesting features:

- Clearly describe the use case
- Explain why it would be beneficial
- Provide examples if possible
- Be open to discussion about implementation

## Development Setup

### Using Docker (Recommended)

```bash
docker-compose up -d
```

This starts MySQL and the application automatically.

### Local Setup

1. Install Java 21
2. Install MySQL 8.0
3. Create database: `CREATE DATABASE ticket_rising_system;`
4. Run: `mvn spring-boot:run`

## Questions?

Feel free to open an issue for any questions or discussions about contributing.

---

**Thank you for contributing!** 🎉
