# Voluntrack - Volunteer Management Platform

## 🌍 Live Demo
👉 https://awbd-voluntrack-production.up.railway.app/

---

## Overview
The Volunteer Management Platform is a web-based application designed to manage volunteer activities. It allows administrators to create and manage volunteer events, while users can browse available events, join them, and track their participation. The goal of this platform is to simplify how volunteer events are organized and how participants engage with them, all within a structured and user-friendly system.

The application focuses on clean architecture, role-based security, validation, pagination, testing, CI/CD, containerization, and cloud deployment.

---

## Objectives
The main objective of this project is to build a web database application that:

- Supports efficient management of volunteer events
- Allows users to register and participate in events
- Manages data related to users, events, categories, venues, and registrations
- Implements authentication, authorization, validation, pagination, sorting, logging, and testing
- Provides a clean and easy-to-use interface

---

## Target Users

### Admin
Admins are responsible for managing all data in the system, including events, categories, venues, users, skills, and registrations.

### User
Users (volunteers) can browse events, join them, and track their participation history.

---

## Main Modules

### Authentication & Authorization
Handles:
- Login & logout
- User registration
- Role-based access control
- Session management

### User & Profile Management
Manages:
- User accounts
- Volunteer profiles
- Skills selection
- Participation history

### Event Management
Handles:
- Event creation
- Event update
- Event deletion
- Event viewing
- Event filtering & search

### Registration Management
Handles:
- Event participation
- Registration approval/rejection
- Participation tracking

### Category & Venue Management
Organizes volunteer events by:
- Category
- Venue
- Location

### Dashboard & Monitoring
Provides:
- Admin dashboard statistics
- User dashboard overview
- Monitoring & health endpoints

---

## Business Rules

- A user cannot join the same event more than once
- A user cannot join an event that is already full or closed or completed
- A user can only cancel registration before the event starts
- Registration status is managed by the system and admin (pending, approved, rejected, etc.)
- Only admins can create, edit, or delete events
- Each event must belong to a category and a venue
- Each user has exactly one profile

---

## Functional Requirements

### Authentication
- Users and admins must be able to log in and log out
- Access must be restricted based on roles

### User Management
- Users can register accounts
- Users can update their profile

### Event Management
- Admin can create, update, and delete events
- Users can view events

### Category & Venue Management
- Admin can manage categories and venues

### Registration Management
- Users can join events
- Users can cancel participation
- Admin can manage registrations and update their status

### Search, Pagination, and Sorting
- Event list supports search
- Pagination is applied to event, user, and registration lists
- Sorting is available for multiple fields

### Validation & Error Handling
- Input must be validated on the server side
- Clear error messages should be displayed
- Custom error pages (e.g., 404, 500)

### Logging
- System logs important actions and errors

### Testing
- Unit tests for service layer
- Integration tests for main flows

---

## Non-Functional Requirements

- **Usability**  
  The application should be simple and easy to use.

- **Maintainability**  
  Code should be clean, modular, and well-structured.

- **Security**  
  Passwords must be stored securely and admin access must be protected.

- **Performance**  
  Data should be handled efficiently using pagination and sorting.

- **Reliability**  
  The system should handle invalid input and errors gracefully.

---

## Technology Stack

| Category | Technology |
|---|---|
| Backend | Spring Boot |
| Frontend | Thymeleaf + Bootstrap 5 |
| Database | PostgreSQL (production), H2 (testing) |
| Security | Spring Security |
| Persistence | Spring Data JPA + Hibernate |
| Testing | JUnit 5 + Mockito |
| Build Tool | Maven |
| CI/CD | GitHub Actions |
| Monitoring | Spring Boot Actuator |
| Deployment | Railway |
| Containerization | Docker |

---

## DevOps & Monitoring

### CI/CD
GitHub Actions automatically:
- Builds the project
- Runs tests
- Validates pushes & pull requests

### Monitoring
Spring Boot Actuator endpoints:
- `/actuator/health`
- `/actuator/info`

### Containerization
Docker support is included through a production-ready Dockerfile.

---

## Branch Strategy

- `main` → stable production-ready branch
- `dev` → active development branch

---

## Setup Instructions

### Prerequisites
- Java 17+
- Maven
- PostgreSQL
- Docker (optional)

### Clone Repository
```bash
git clone <repository-url>
