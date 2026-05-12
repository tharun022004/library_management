# Library Management System - Backend

This is a comprehensive backend application for a Library Management System. Built using **Spring Boot 3.2.5** and **Java 17**, it provides robust RESTful APIs to manage library operations, including maintaining book catalogs, managing members, and processing book issues and returns.

## 🚀 Technology Stack

*   **Java Version:** 17
*   **Framework:** Spring Boot 3.2.5
    *   **Spring Web:** For building REST APIs.
    *   **Spring Data JPA:** For ORM and Database interaction.
    *   **Spring Security:** To secure application endpoints.
    *   **Spring Boot Validation:** For request payload validations.
*   **Database:** PostgreSQL (`org.postgresql.Driver`)
*   **API Documentation:** Springdoc OpenAPI / Swagger UI (`springdoc-openapi-starter-webmvc-ui` 2.5.0)
*   **Developer Tools:** Lombok, Spring Boot DevTools

## 🏗️ Project Architecture & Entities

The application follows a standard layered architecture: Controllers, Services, Repositories, Entities, and DTOs.

### Core Entities

1.  **Book (`books`)**
    *   `bookId` (Long, Primary Key)
    *   `title` (String, required)
    *   `author` (String, required)
    *   `available` (boolean, defaults to true)
    *   `createdAt` (LocalDateTime, auto-generated)
    *   *Relationships:* One-to-Many with `IssueRecord`

2.  **Member (`members`)**
    *   `memberId` (Long, Primary Key)
    *   `name` (String, required)
    *   `email` (String, required, unique)
    *   `createdAt` (LocalDateTime, auto-generated)
    *   *Relationships:* One-to-Many with `IssueRecord`

3.  **IssueRecord (`issue_records`)**
    *   `issueId` (Long, Primary Key)
    *   `issueDate` (LocalDateTime)
    *   `dueDate` (LocalDateTime)
    *   `returnDate` (LocalDateTime, nullable)
    *   `status` (Enum - `IssueStatus`)
    *   *Relationships:* Many-to-One with `Member` and `Book`

## ⚙️ Configuration Properties

The application relies on PostgreSQL for data persistence. Configure your `src/main/resources/application.properties` with the correct database credentials:

```properties
server.port=8080
spring.application.name=Library_management

# Database Configuration
spring.datasource.url=jdbc:postgresql://localhost:5432/lib_man
spring.datasource.username=postgres
spring.datasource.password=<YOUR_PASSWORD>
spring.datasource.driver-class-name=org.postgresql.Driver

# Hibernate / JPA Settings
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true

# Swagger Documentation Paths
springdoc.swagger-ui.path=/swagger-ui.html
springdoc.api-docs.path=/v3/api-docs
```

## 🛡️ Security Configuration

The application is equipped with Spring Security. Currently, the `SecurityConfig.java` is configured to disable CSRF and allow unauthenticated access to all endpoints (`permitAll()`) for rapid development and testing purposes. 

*(Note: Before deploying to production, this should be updated with proper authentication mechanisms like JWT or OAuth2).*

## 📚 REST API Endpoints

The system provides controllers to handle different operational domains. You can test all endpoints directly using the built-in Swagger UI.

### 1. Books API (`BookController`)
*   Provides endpoints for adding new books, fetching book details by ID, listing all books, updating book information, and deleting books.

### 2. Members API (`MemberController`)
*   Provides endpoints for registering new library members, fetching member details, and managing member records.

### 3. Issues & Returns API (`IssueController`)
*   Manages the process of issuing a book to a member.
*   Handles returning a book, which updates the `returnDate`, modifies the `IssueStatus`, and sets the book's availability back to `true`.

### Exception Handling
The project includes a global exception handler (`GlobalExceptionHandler`) to gracefully capture errors such as `ResourceNotFoundException` and `InvalidReturnException`, ensuring clients receive consistent and meaningful HTTP error responses.

## 🛠️ How to Build and Run

1.  **Database Setup:** Create a PostgreSQL database named `lib_man`.
2.  **Navigate to the project directory:**
    ```bash
    cd Backend
    ```
3.  **Clean and Install Dependencies:**
    ```bash
    mvn clean install
    ```
4.  **Run the Application:**
    ```bash
    mvn spring-boot:run
    ```

Once the application is running successfully, the server will listen on port `8080`.

## 📖 API Documentation (Swagger)

Interactive API documentation is automatically generated. You can access it via your web browser:
*   **Swagger UI Interface:** [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)
*   **OpenAPI JSON Specification:** [http://localhost:8080/v3/api-docs](http://localhost:8080/v3/api-docs)
