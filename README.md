# Car Rental System

A Java-based car rental system with CI/CD implementation.

## Project Structure

The project is organized into the following main components:

- `src/main/java/com/carrental/` - Main application code
- `src/test/java/com/carrental/` - Test code
- `.github/workflows/` - CI/CD configuration

## Features

- Car rental management
- User management
- Booking system
- Payment processing
- Reporting

## Setup

1. Ensure you have Java 17 installed
2. Clone the repository
3. Build the project:
   ```bash
   ./gradlew build
   ```
4. Run the application:
   ```bash
   ./gradlew run
   ```

## Development

The project uses:
- Java 17
- Gradle
- JUnit 5 for testing
- GitHub Actions for CI/CD

## CI/CD Pipeline

The project implements continuous integration and continuous delivery:
- Automated testing on pull requests to main
- Automated testing on pushes to dev
- Automated deployment to GitHub Packages after successful testing 