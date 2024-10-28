# Spring Boot M-Pesa Integration Project

This project is a Spring Boot application that integrates with the M-Pesa APIs (Auth, C2B, B2C, and Transaction Status) and uses a PostgreSQL database. The application is containerized using Docker and Docker Compose.

## Features

* **M-Pesa API Integration:**
  * Supported APIs: B2B, B2C, C2B, Pay Bill, Buy Goods
  * Centralized Communication: Single point of access for all M-Pesa API calls
  * Consistent Error Handling: Standardized error handling with retry mechanisms
  * Standardized Logging: Request and response logging at DEBUG level
* **Robust Error Handling:**
  * Custom Exceptions: Proper mapping of M-Pesa error codes to custom exceptions
  * Detailed Logging: Detailed error logging with request IDs for correlation
  * Informative Messages: Informative error messages to the user
* **Enhanced Security:**
  * Secure Credentials: Secure storage and management of M-Pesa API credentials - TODO
  * Token Management: Token-based authentication with refresh tokens
  * Secure Headers: Secure header management and protection against vulnerabilities
* **Specific Transaction Handling:**
  * Pay Bill & Buy Goods: Specialized handling for Pay Bill and Buy Goods transactions, including validation and confirmation URL processing
* **Support Services:**
  * Balance Inquiry: Provides functionalities for querying M-Pesa account balances
  * Transaction Status Check: Enables checking the status of initiated transactions
  * Reversals: Implements reversal functionalities for failed or erroneous transactions
* **Clean Code Structure:**
  * Separation of Concerns: Well-defined DTOs, entities, repositories, and services for each transaction type
  * Maintainability: Adherence to best practices for code organization and maintainability

## Technologies Used

- Java 17
- Spring Boot
- Maven
- PostgreSQL for transaction management in a production environment. (For development purposes, the built-in H2 database can be used).
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




