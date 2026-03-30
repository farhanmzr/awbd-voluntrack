# Voluntrack - Volunteer Management Platform

## Overview
The Volunteer Management Platform is a web-based application designed to manage volunteer activities. It allows administrators to create and manage volunteer events, while users can browse available events, join them, and track their participation. The goal of this platform is to simplify how volunteer events are organized and how participants engage with them, all within a structured and user-friendly system.

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
Admins are responsible for managing all data in the system, including events, categories, venues, users, and registrations.

### User
Users (volunteers) can browse events, join them, and track their participation history.

---

## Main Modules

- **Authentication & Authorization**  
  Handles login, logout, and role-based access control.

- **User & Profile Management**  
  Manages user accounts and volunteer profiles.

- **Event Management**  
  Handles creation, update, deletion, and viewing of events.

- **Registration Management**  
  Manages event participation and registration status.

- **Category & Venue Management**  
  Organizes events by category and location.

- **Dashboard & Monitoring**  
  Provides a summary view of important data for admin and users.

---

## Features

### User Features
- Register an account
- Login and logout
- View event list
- Search and filter events
- View event details
- Join an event
- Cancel registration
- View joined events
- Edit profile

### Admin Features
- Login and logout
- Manage events (create, update, delete)
- Manage categories
- Manage venues
- Manage users
- Manage registrations
- Approve or reject registrations
- View participants per event
- Dashboard overview

---

## UI Pages

### Public Pages
- Home Page
- Event List Page (with search, filter, pagination, sorting)
- Event Detail Page
- Login Page
- Register Page

### User Pages
- User Dashboard
- My Profile Page
- My Events Page
- Participation History Page

### Admin Pages
- Admin Dashboard
- Manage Events Page
- Create/Edit Event Page
- Manage Categories Page
- Manage Venues Page
- Manage Users Page
- Manage Registrations Page
- Event Participants Page

---

## Business Rules

- A user cannot join the same event more than once
- A user cannot join an event that is already full
- A user cannot join an event that is closed or completed
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

- **Backend & Frontend**: Spring Boot, Thymeleaf, Bootstrap 5  
- **Database**: PostgreSQL (development), H2 (testing)  
- **Security**: Spring Security  
- **Persistence**: Spring Data JPA, Hibernate  
- **Testing**: JUnit 5, Mockito  
- **Build Tool**: Maven or Gradle  

---
