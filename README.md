# Ticket Management Application

## Overview
This is a Ticket Management Application developed for Hahn Software. It is built using Spring Boot (Java 17) for the backend and Java Swing for the frontend. The application provides a comprehensive solution for managing tickets efficiently.

## Features

### Spring Boot Backend
- Secure API with Spring Security
- Swagger documentation for API exploration
- Dockerized Backend & Database

### Java Swing Frontend
- **All UI forms are located in the package:** `com.example.ticketManager.forms` 
- **Packaged as a JAR file:** `ticketManager-0.0.1-SNAPSHOT.jar`

- 
### Architecture & Design

### Class Diagram
Below is the high-level class diagram for the application:  

<img width="938" alt="Image" src="https://github.com/user-attachments/assets/5730013a-12d3-42a3-8128-9220c6e449df" />


### Database
- Uses Oracle Database, deployed via Docker Compose

## Installation & Setup

### Prerequisites
Ensure you have the following installed:
- Java 17
- Docker & Docker Compose
- Maven (for building the project)

### Database Initialization

#### SQL Schema Setup
The `tables.sql` file in the project root contains the database schema and initial data for Oracle Database. It includes all insert data.

### Backend Setup
1. Navigate to the backend project directory.
2. Build the project using Maven:
   ```bash
   mvn clean install
3. Start the application using Docker Compose:
   ```bash
   docker-compose up -d

4. The backend will be running at:
   http://localhost:9090

### Frontend Setup
1. Run the Swing application:
   ```bash
   java -jar target/ticketManager-0.0.1-SNAPSHOT.jar

### API Documentation
The API is documented with Swagger. Access the Swagger UI once the backend is running:
http://localhost:9090/swagger-ui.html

