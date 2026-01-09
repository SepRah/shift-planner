# ShiftPlanner

## Overview

ShiftPlanner is a web-based scheduling application designed to assign tasks to employees in a structured and role-aware way. Each employee has a personal login and can view their assigned tasks, while users with extended permissions (e.g. managers) can create, manage, and assign tasks to employees.

The application enforces business rules such as qualification requirements and employment levels (FTE) to ensure tasks are only assigned to suitable employees.

---

## Business Logic & Requirements

### Employees
Each employee has:
- A personal login
- A qualification level
- A full-time equivalent value (FTE)

### Tasks
- Define a minimum required qualification level
- Can only be assigned to employees who meet the qualification requirements

### Role-based Access Control
- Not all users are allowed to perform all actions
- Users with elevated roles (e.g. managers) can:
    - Create, edit, and delete tasks
    - Assign tasks to employees
    - Manage employees (create, update, deactivate)

---

## Technical Architecture

The application is implemented as a **Single-Page Application (SPA)** with a clear separation between backend and frontend.

### Backend
- Built with **Spring Boot**
- Uses **Spring Security** for authentication and authorization
- **JWT (JSON Web Tokens)** are used for stateless authentication
- User roles and permissions are stored in the JWT and validated on each request
- RESTful API for all business operations

### Frontend
- Built with **React** and **Vite**
- Communicates with the backend via REST APIs using **Axios**
- JWT is automatically attached to outgoing requests
- Authentication state is managed centrally using an **AuthContext**
- **React Router** is used for client-side navigation without page reloads
- **FullCalendar** is integrated to visually display tasks and shifts

---

## Key Features

- Secure login with JWT-based authentication
- Role-based authorization on frontend and backend
- Employee and task management
- Qualification-based task assignment
- Calendar-based visualization of shifts and tasks

---
## Authors

- Sina Enzmann
- Benjamin Traffelet
- Sepehr Rahmani Khajouei