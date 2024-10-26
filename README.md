# Spring Boot M-Pesa Integration Project

This project is a Spring Boot application that integrates with the M-Pesa APIs (Auth, C2B, B2C, and Transaction Status) and uses a PostgreSQL database. The application is containerized using Docker and Docker Compose.

## Features

- Integration with M-Pesa APIs:
    - Auth API for access token generation.
    - C2B API for customer-to-business transactions.
    - B2C API for business-to-customer payments.
    - Transaction Status API to check the status of a transaction.

- Uses PostgreSQL as the relational database for transaction management. (For dev purposes, using the built in H2 db)

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




