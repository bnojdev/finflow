# FinFlow

A simple banking application built with Spring Boot, featuring user registration, OTP verification, JWT authentication, money transfers, and transaction history.

## Features

- User registration with OTP verification
- JWT-based authentication and logout
- Money transfer between users
- Transaction history retrieval
- H2 in-memory database (configurable for file-based)
- Swagger API documentation
- Docker support
- Resilience4j for retry mechanisms

## Tech Stack

- **Backend**: Spring Boot 3.3.5, Java 21
- **Database**: H2 (in-memory or file-based)
- **Security**: JWT, Spring Security
- **Documentation**: Swagger/OpenAPI
- **Containerization**: Docker, Docker Compose
- **Build Tool**: Maven

## Prerequisites

- Java 21
- Maven 3.6+
- Docker (optional, for containerized deployment)

## Installation and Setup

### Local Development

1. Clone the repository:
   ```bash
   git clone https://github.com/bnojdev/finflow.git
   cd finflow
   ```

2. Build the project:
   ```bash
   ./mvnw clean install
   ```

3. Run the application:
   ```bash
   ./mvnw spring-boot:run
   ```

4. Access the application:
   - API: http://localhost:8096
   - Swagger UI: http://localhost:8096/swagger-ui/index.html
   - H2 Console: http://localhost:8096/h2-console (JDBC URL: jdbc:h2:mem:finflow-db)

### Docker Deployment

1. Build and run with Docker Compose:
   ```bash
   docker-compose up --build
   ```

2. Access the application at http://localhost:8096

3. Override configurations via `application-override.properties` or environment variables in `docker-compose.yml`

## API Endpoints

### Authentication

#### Register User
- **URL**: `POST /api/register`
- **Body**:
  ```json
  {
    "name": "Binoj",
    "email": "binoj@test.com",
    "mobile": "9876543210"
  }
  ```
- **Response**: User registered message with OTP sent to console.

#### Verify OTP
- **URL**: `POST /api/verify-otp?mobile=9876543210&otp=123456`
- **Response**: User verified and account created.

#### Login
- **URL**: `POST /api/login?mobile=9876543210`
- **Response**: JWT token for authentication.

#### Logout
- **URL**: `POST /api/logout`
- **Headers**: `Authorization: Bearer <token>`
- **Response**: Logged out successfully.

### Transactions

#### Transfer Money
- **URL**: `POST /api/transfer`
- **Headers**: `Authorization: Bearer <token>`
- **Body**:
  ```json
  {
    "senderId": 1,
    "receiverId": 2,
    "amount": 100.00,
    "idempotencyKey": "unique-key-123"
  }
  ```
- **Response**: Transfer successful.

#### Get Transaction History
- **URL**: `GET /api/transactions/{userId}`
- **Headers**: `Authorization: Bearer <token>`
- **Response**: List of transactions.

## Sample Data

### Register Users
- User 1: `{"name": "Binoj", "email": "binoj@test.com", "mobile": "9876543210"}`
- User 2: `{"name": "Manu", "email": "manu@test.com", "mobile": "9876543211"}`

After registration and OTP verification, users get accounts with initial balance of 1000.00.

## Configuration

- **JWT Secret**: Configurable via `jwt.secret` in `application.properties`
- **Database**: Switch to file-based in `application-docker.properties`
- **Logging**: Configurable via `logback-spring.xml`
- **Overrides**: Use `application-override.properties` or environment variables

## Postman Collection

Import `FinFlow API Collection (JWT Enabled).postman_collection.json` for testing APIs.

## Contributing

1. Fork the repository
2. Create a feature branch
3. Commit changes
4. Push and create a PR

## License

This project is licensed under the MIT License.
