# Datasets API

A Spring Boot REST API service for managing datasets with PostgreSQL persistence.

## Requirements

- **Java 21** or higher
- Maven (or use included Maven wrapper)
- **PostgreSQL 16** running on localhost:5432
- Database named `datasets` (password: `4c257b5768be4a5ea40e6f071cf42b6c`)

## Database Setup

Make sure PostgreSQL is running and create the database:
```sql
CREATE DATABASE datasets;
```

The application is pre-configured to connect with:
- **URL**: `jdbc:postgresql://localhost:5432/datasets`
- **Username**: `postgres`
- **Password**: `4c257b5768be4a5ea40e6f071cf42b6c`

## 30-Second Quick Start

```bash
./mvnw spring-boot:run
```

The application will start on `http://localhost:8080`

## Default Endpoints

- **Health Check**: [http://localhost:8080/actuator/health](http://localhost:8080/actuator/health)
- **API Documentation**: [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)
- **OpenAPI Spec**: [http://localhost:8080/v3/api-docs](http://localhost:8080/v3/api-docs)

## Tech Stack

- Java 21
- Spring Boot 3.5.3
- Spring Web (REST APIs)
- Spring Data JPA (Database persistence)
- Spring Validation (Input validation)
- Spring Actuator (Monitoring)
- PostgreSQL 16 (Database)
- SpringDoc OpenAPI (API Documentation)

## Development

### Build
```bash
./mvnw clean compile
```

### Test
```bash
./mvnw test
```

### Code Coverage Report
```bash
./mvnw test jacoco:report
# View report at target/site/jacoco/index.html
```



### Package
```bash
./mvnw package
```

### Run
```bash
# Development
./mvnw spring-boot:run

# Production (after package)
java -jar target/datasets-0.0.1-SNAPSHOT.jar
```

## Project Structure

```
datasets-api/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/assignment/datasets/
│   │   │       └── DatasetsApiApplication.java
│   │   └── resources/
│   │       ├── application.properties
│   │       ├── static/
│   │       └── templates/
│   └── test/
│       └── java/
│           └── com/assignment/datasets/
│               └── DatasetsApiApplicationTests.java
├── target/
├── pom.xml
├── mvnw
├── mvnw.cmd
└── README.md
```

## Maven Plugins

- **Spring Boot Plugin**: Application packaging and running
- **JaCoCo Plugin**: Code coverage reporting

## Database Connection

Connection string: `jdbc:postgresql://localhost:5432/datasets`

Ensure PostgreSQL is running before starting the application. 


 Start-Service postgresql-x64-16