# Ticket Management Application

## Overview
This is a Ticket Management Application developed for Hahn Software. It is built using Spring Boot (Java 17) for the backend and Java Swing for the frontend. The application provides a comprehensive solution for managing tickets efficiently with secure authentication and a user-friendly interface.

## Features

### Spring Boot Backend
- **Secure API with Spring Security**: Passwords are securely encoded using BCrypt.
- **JUnit & Mockito Tests**: Includes unit tests for backend functionalities with Mockito for mocking dependencies.
- **Swagger Documentation**: Explore and test the API endpoints with Swagger.
- **Dockerized Backend & Database**: Use Docker Compose to spin up the backend and Oracle database with pre-populated data.
- **API Authentication**: Secure endpoints with Spring Security.

### Java Swing Frontend
- **UI Forms**: All UI forms are located in the package: `com.example.ticketManager.forms`
- **Packaged as a JAR**: `ticketManager-0.0.1-SNAPSHOT.jar`
- **Easy Installation**: Can be executed easily with the JAR file for desktop use.

### Architecture & Design

#### Class Diagram
Below is the high-level class diagram for the application:

![Class Diagram](https://github.com/user-attachments/assets/5730013a-12d3-42a3-8128-9220c6e449df)

### Database
- Uses Oracle Database, deployed via Docker Compose.

## Installation & Setup

### Prerequisites
Ensure you have the following installed:
- **Java 17**: For building and running the backend and frontend.
- **Docker & Docker Compose**: For running the backend and database containers.
- **Maven**: For building the project and running tests.

### Database Initialization

#### SQL Schema Setup
The `tables.sql` file located in the root of the project contains the schema and initial data. It will populate the Oracle database with necessary tables and data for development and testing.

### Backend Setup
1. **Clone the repository**:
   ```bash
   git clone https://github.com/your-repository/ticketManager.git
   cd ticketManager
2. **Build the project:**
   ```bash
   mvn clean install
This will compile the project and package the backend into a deployable artifact.

3. **Start the backend and database using Docker**
   Navigate to the directory where your Dockerfile is located and run the following command to build the Docker image for your backend:
   
   ```bash
   docker build -t backend .
   docker-compose up -d
   
This will build the Docker image with the name backend using the Dockerfile in the current directory.
Then use this command This to build and start the backend and database containers in detached mode.
   

   
4. **Backend now is running at:**:
   ```bash
   http://localhost:9090

### Frontend Setup
1. **Run the Swing application:**
   Navigate to the target directory where the JAR is built and execute:
   ```bash
   java -jar target/ticketManager-0.0.1-SNAPSHOT.jar

### API Documentation
The API is documented with Swagger. Once the backend is up and running, you can explore the API endpoints using the Swagger UI: 
`http://localhost:9090/swagger-ui.html`

### Testing with JUnit & Mockito

## Unit Tests with Mockito

Mockito is used for mocking dependencies like the UserRepository and BCryptPasswordEncoder. The following tests are included for user-related services:
   
- Test Create User: Validates user creation and password encoding.
- Test Default Role: Checks the default role when a user does not specify one.
- Test Get User by Username: Verifies the user retrieval by username.
- Test User Authentication: Checks user authentication with correct and incorrect credentials.

  #### Example JUnit Test (UserServiceTest)
     ```bash

   @ExtendWith(MockitoExtension.class)
   public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private BCryptPasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    private User user;

    @BeforeEach
    public void setUp() {
        user = new User();
        user.setUsername("testuser");
        user.setPassword("password123");
        user.setRole("Admin");
    }

    @Test
    public void testCreateUser_ValidUser() {
        when(passwordEncoder.encode(any(String.class))).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenReturn(user);

        User createdUser = userService.createUser(user);

        assertNotNull(createdUser);
        assertEquals("encodedPassword", createdUser.getPassword());
        assertEquals("Admin", createdUser.getRole());
        verify(userRepository, times(1)).save(user);
    }
   }
#### Spring Security: BCrypt Password Encoding
In the backend, Spring Security is used to encode passwords securely using BCrypt. This ensures that passwords are stored in a hashed format, making them secure for authentication.

## Conclusion

This Ticket Management Application is a complete solution for managing tickets with secure authentication and a robust backend API. Using Docker and Swagger simplifies the setup and interaction, while Java Swing provides a clean and intuitive frontend for desktop use.

  











