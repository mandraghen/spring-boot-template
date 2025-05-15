# Spring Boot Template

This repository serves as a template for creating new projects based on Spring Boot. It is designed to help developers quickly set up microservices or similar components.

## Features

- **Spring Boot 3.x**: Leverages the latest features of Spring Boot for rapid development.
- **Java 21**: Utilizes the latest Java version with virtual threads enabled for improved concurrency.
- **Gradle Build System**: Simplifies dependency management and build processes.
- **Docker Support**: Includes a `Dockerfile` and `compose.yaml` for containerized deployment.
- **Hibernate JPA**: Configured for efficient database interaction with PostgreSQL.
- **Profiles**: Supports multiple Spring profiles (`local`, `cds`) for environment-specific configurations.
- **Optimized Docker Images**: Uses layered JARs and Class Data Sharing (CDS) for faster startup and efficient builds.

## Prerequisites

- **Java 21**: Ensure the JDK is installed and configured.
- **Gradle**: Install Gradle for building the project.
- **Docker**: Required for containerized deployment.
- **PostgreSQL**: Docker Compose will manage the database instance automatically.

## Getting Started

### Clone the Repository

```bash
git clone https://github.com/mandraghen/spring-boot-template.git
cd spring-boot-template
```

### Build the Project

```bash
./gradlew build
```

### Run the Application Locally

```bash
./gradlew bootRun
```

### Run with Docker Compose

1. Ensure Docker is running.
2. Start the services:

```bash
docker compose up
```

This will start the application and manage all external dependencies, including a PostgreSQL database instance.

### Test the Application

Run the tests using:

```bash
./gradlew test
```

## Configuration

### Application Properties

The application is configured using YAML files located in `src/main/resources`:

- `application.yaml`: Default configuration.
- `application-local.yaml`: Configuration for the `local` profile. Active by default.
- `application-cds.yaml`: Configuration for the `cds` profile. It's used for the dockerization.

### Docker Compose

The `compose.yaml` file defines the services required for the application, including a PostgreSQL database.

### Dockerfile

The `Dockerfile` is optimized for layered builds and CDS. It includes a builder stage for extracting dependencies and a runtime stage for efficient startup.

## Dependencies

Key dependencies used in this project:

- `spring-boot-starter-data-jpa`: For database interaction.
- `spring-boot-starter-web`: For building RESTful APIs.
- `lombok`: For reducing boilerplate code.
- `spring-boot-docker-compose`: For Docker Compose integration.
- `testcontainers`: For running integration tests with containerized dependencies.

## Profiles

- **local**: Default profile for local development.
- **cds**: Profile optimized for Class Data Sharing (CDS).

## Contributing

Contributions are welcome! Please follow these steps:

1. Fork the repository.
2. Create a feature branch.
3. Commit your changes.
4. Submit a pull request.

## License

This project is licensed under the MIT License. See the `LICENSE` file for details.
