package com.example.ticketManager.servicesTest;



import com.example.ticketManager.entities.User;
import com.example.ticketManager.repos.UserRepository;
import com.example.ticketManager.services.UserService;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;



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
        // Arrange
        when(passwordEncoder.encode(any(String.class))).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenReturn(user);

        // Act
        User createdUser = userService.createUser(user);

        // Assert
        assertNotNull(createdUser);
        assertEquals("encodedPassword", createdUser.getPassword());
        assertEquals("Admin", createdUser.getRole());
        verify(userRepository, times(1)).save(user);
    }

    @Test
    public void testCreateUser_DefaultRole() {
        // Arrange
        user.setRole("");
        when(passwordEncoder.encode(any(String.class))).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenReturn(user);

        // Act
        User createdUser = userService.createUser(user);

        // Assert
        assertEquals("Employee", createdUser.getRole());
    }

    @Test
    public void testGetUserByUsername_UserFound() {
        // Arrange
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(user));

        // Act
        Optional<User> foundUser = userService.getUserByUsername("testuser");

        // Assert
        assertTrue(foundUser.isPresent());
        assertEquals("testuser", foundUser.get().getUsername());
    }

    @Test
    public void testGetUserByUsername_UserNotFound() {
        // Arrange
        when(userRepository.findByUsername("unknown")).thenReturn(Optional.empty());

        // Act
        Optional<User> foundUser = userService.getUserByUsername("unknown");

        // Assert
        assertFalse(foundUser.isPresent());
    }

    @Test
    public void testAuthenticateUser_ValidCredentials() {
        // Arrange
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("password123", user.getPassword())).thenReturn(true);

        // Act
        User authenticatedUser = userService.authenticateUser("testuser", "password123");

        // Assert
        assertNotNull(authenticatedUser);
        assertEquals("testuser", authenticatedUser.getUsername());
    }

    @Test
    public void testAuthenticateUser_InvalidPassword() {
        // Arrange
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("wrongpassword", user.getPassword())).thenReturn(false);

        // Act
        User authenticatedUser = userService.authenticateUser("testuser", "wrongpassword");

        // Assert
        assertNull(authenticatedUser);
    }

    @Test
    public void testAuthenticateUser_UserNotFound() {
        // Arrange
        when(userRepository.findByUsername("unknown")).thenReturn(Optional.empty());

        // Act
        User authenticatedUser = userService.authenticateUser("unknown", "password123");

        // Assert
        assertNull(authenticatedUser);
    }
}