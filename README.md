# Spring Boot M-Pesa Integration Project

This project is a Spring Boot application that integrates with the M-Pesa APIs (Auth, C2B, B2C, and Transaction Status) and uses a PostgreSQL database. The application is containerized using Docker and Docker Compose.

### Directory Structure

src/
├── main/
│   ├── java/
│   │   └── co/
│   │       └── ke/
│   │           └── integration/
│   │               ├── config/           # Configuration classes
│   │               ├── constant/         # Constant values and enums
│   │               ├── controller/       # REST API controllers
│   │               │   └── api/          # Controllers for data display APIs
│   │               ├── dto/              # Data Transfer Objects
│   │               │   ├── request/      # Request DTOs
│   │               │   └── response/     # Response DTOs
│   │               ├── entity/           # JPA entities
│   │               ├── exception/        # Custom exceptions and handlers
│   │               ├── repository/       # Spring Data JPA repositories
│   │               ├── service/          # Business logic services
│   │               └── util/             # Utility classes
│   └── resources/
│       ├── application.properties        # Main application properties
│       ├── application-dev.properties    # Development-specific properties
│       └── application-prod.properties   # Production-specific properties
└── test/
└── java/
└── com/
└── example/
└── mpesaintegration/
├── controller/       # Controller tests
├── service/          # Service tests
└── util/             # Utility class tests

## Features

- Integration with M-Pesa APIs:
    - Auth API for access token generation.
    - C2B API for customer-to-business transactions.
    - B2C API for business-to-customer payments.
    - Transaction Status API to check the status of a transaction.

- Uses PostgreSQL as the relational database for transaction management.

## Technologies Used

- Java 17
- Spring Boot
- Maven
- PostgreSQL
- Docker
- Docker Compose
- RestTemplate for REST API calls

## Prerequisites

Before you begin, ensure you have the following installed:

- [Java 17](https://www.oracle.com/java/technologies/javase-jdk17-downloads.html)
- [Maven 3.8+](https://maven.apache.org/install.html)
- [Docker](https://docs.docker.com/get-docker/)
- [Docker Compose](https://docs.docker.com/compose/install/)

## Project Structure


## Environment Variables

The application relies on the following environment variables for connecting to the database and interacting with the M-Pesa APIs:

| Environment Variable         | Description                                    |
| ---------------------------- | ---------------------------------------------- |
| `SPRING_DATASOURCE_URL`       | The JDBC URL for the PostgreSQL database.      |
| `SPRING_DATASOURCE_USERNAME`  | Database username.                             |
| `SPRING_DATASOURCE_PASSWORD`  | Database password.                             |
| `MPESA_CONSUMER_KEY`          | Your M-Pesa consumer key.                      |
| `MPESA_CONSUMER_SECRET`       | Your M-Pesa consumer secret.                   |
| `SPRING_JPA_HIBERNATE_DDL_AUTO` | Hibernate property to manage DB schema updates. |

## How to Run the Project

### 1. Clone the Repository

```bash
git clone https://github.com/yourusername/springboot-mpesa-integration.git
cd springboot-mpesa-integration
```
### 2. Build and run with docker

#### a. Build and run the application using Docker Compose:
```bash
docker-compose up --build

```

This will:

Build the Spring Boot application.
Start the Spring Boot app and PostgreSQL database containers.
The Spring Boot application will be available at http://localhost:8080, and PostgreSQL will be accessible at localhost:5432.

#### b. Stopping the application:

To stop the running containers, use:

```bash
docker-compose down

```
This will stop and remove the containers but keep the database data intact using Docker volumes.


### Access the Application
Once the containers are up, you can test the M-Pesa API integration by hitting the endpoints exposed by the application. For example:

GET /api/mpesa/auth - To generate an access token.

POST /api/mpesa/c2b - To initiate a C2B transaction.

POST /api/mpesa/b2c - To make a B2C payment.

GET /api/mpesa/transaction-status - To check the status of a transaction.

### Running Tests


