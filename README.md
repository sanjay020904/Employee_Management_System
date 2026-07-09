# Employee Leave Management System

A simple REST API-based Employee Leave Management System built with **Java**, **Spring Boot**, **Hibernate**, and **MySQL**.

## Tech Stack

| Technology | Purpose |
|------------|---------|
| Java 21 | Core language |
| Spring Boot 4.1 | Application framework |
| Spring Data JPA (Hibernate) | ORM & database access |
| MySQL | Relational database |
| Lombok | Boilerplate reduction |
| Postman | API testing |

## Features

- **Employee Login** — Authenticate via username/password
- **Apply Leave** — Submit leave requests (Sick, Casual, Annual)
- **Approve/Reject Leave (Admin)** — Admin can approve or reject pending requests with remarks
- **View Leave History** — Employees can view their leave history
- **CRUD Operations** — Full Create, Read, Update, Delete for Users and Leave Requests

## Project Structure

```
src/main/java/com/employee/leavemanager/
├── LeavemanagerApplication.java
├── controller/
│   ├── AuthController.java
│   ├── UserController.java
│   └── LeaveRequestController.java
├── dto/
│   ├── LoginRequest.java
│   ├── LoginResponse.java
│   ├── LeaveApplyRequest.java
│   └── LeaveStatusUpdateRequest.java
├── loader/
│   └── DatabaseSeeder.java
├── model/
│   ├── User.java
│   ├── Role.java
│   ├── LeaveRequest.java
│   └── LeaveStatus.java
├── repository/
│   ├── UserRepository.java
│   └── LeaveRequestRepository.java
└── service/
    ├── UserService.java
    └── LeaveRequestService.java
```

## Prerequisites

- Java 21+
- MySQL Server running on `localhost:3306`
- Postman (for API testing)

## Setup & Run

1. **Create the database**:
   ```sql
   CREATE DATABASE IF NOT EXISTS leave_management_db;
   ```

2. **Configure database credentials** in `src/main/resources/application.properties`:
   ```properties
   spring.datasource.username=root
   spring.datasource.password=1234
   ```

3. **Run the application**:
   ```bash
   ./mvnw spring-boot:run
   ```

4. The app starts on `http://localhost:8080` and automatically creates the database tables via Hibernate.

## Default Users (Auto-Seeded)

| Role | Username | Password |
|------|----------|----------|
| Admin | `admin` | `admin123` |
| Employee | `employee` | `emp123` |

## API Endpoints

### Authentication
| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/auth/login` | Login with username & password |
| POST | `/api/auth/register` | Register a new user |

### Leave Management
| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/leaves/apply` | Apply for leave |
| GET | `/api/leaves` | Get all leave requests |
| GET | `/api/leaves/pending` | Get pending leave requests |
| GET | `/api/leaves/employee/{id}` | Get leave history by employee |
| GET | `/api/leaves/{id}` | Get leave request by ID |
| PUT | `/api/leaves/{id}/status` | Approve/Reject leave (Admin) |
| PUT | `/api/leaves/{id}` | Update a pending leave request |
| DELETE | `/api/leaves/{id}` | Delete a leave request |

### User Management (CRUD)
| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/users` | Create a new user |
| GET | `/api/users` | Get all users |
| GET | `/api/users/{id}` | Get user by ID |
| PUT | `/api/users/{id}` | Update user |
| DELETE | `/api/users/{id}` | Delete user |

## Postman Collection

Import `Employee_Leave_Management_System.postman_collection.json` from the project root into Postman for pre-configured requests.
