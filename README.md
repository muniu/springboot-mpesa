# Spring Boot M-Pesa Integration Project

This project is a Spring Boot application that integrates with the M-Pesa APIs (Auth, C2B, B2C, and Transaction Status) and uses a PostgreSQL (H2 for dev) database. The application is containerized using Docker and Docker Compose.

## Features

### Implemented APIs
- **Authentication**
  - OAuth token generation
  - Token management

- **Account Balance**
  - Query account balance
  - Handle validation and confirmation
  - Support for different account types (Working, Utility, Charges)

- **Business to Customer (B2C)**
  - Support for different payment types:
    - Salary Payment
    - Business Payment
    - Promotion Payment
  - Callback handling
  - Timeout management
- **Customer to Business (C2B)**
  - - Buy Goods
    - Pay Bill
  **Transaction Status**
    - Query the status of transactions

### Coming Soon
- Business to Business (B2B)
- Transaction Reversal
- Tax Remittance

### Key Technical Details:
* **Code structure:**
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
  * Reversals: Implements reversal functionalities for failed or erroneous transactions - TODO
* **Clean Code Structure:**
  * Separation of Concerns: Well-defined DTOs, entities, repositories, and services for each transaction type
  * Maintainability: Adherence to best practices for code organization and maintainability

## Technologies Used

- Java 17
- Spring Boot 3.x
- Spring Data JPA
- PostgreSQL
- Lombok
- OpenAPI (Swagger)

## Project Structure

```plaintext
src/
├── main/
│   ├── java/
│   │   └── co/ke/integration/mpesa/
│   │       ├── client/        # HTTP clients
│   │       ├── config/        # Configuration classes
│   │       ├── controller/    # REST endpoints
│   │       ├── dto/           # Data Transfer Objects
│   │       ├── entity/        # Database entities
│   │       ├── exception/     # Exception handling
│   │       ├── repository/    # Data access
│   │       ├── service/       # Business logic
│   │       └── util/          # Utility classes
│   └── resources/
│       └── application.properties




