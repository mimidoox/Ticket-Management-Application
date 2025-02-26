/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.ticketManager.controllersTest;

/**
 *
 * @author macbookmimid
 */

import com.example.ticketManager.controllers.AuthController;
import com.example.ticketManager.entities.User;
import com.example.ticketManager.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AuthControllerTest {

    @Mock
    private UserService userService;

    @InjectMocks
    private AuthController authController;

    private User user;

    @BeforeEach
    public void setUp() {
        user = new User();
        user.setUsername("testuser");
        user.setPassword("password123");
        user.setRole("Admin");
    }

    @Test
    public void testRegisterUser() {
        when(userService.createUser(any(User.class))).thenReturn(user);

        User registeredUser = authController.registerUser(user);

        assertNotNull(registeredUser);
        assertEquals("testuser", registeredUser.getUsername());
        verify(userService, times(1)).createUser(user);
    }

    @Test
    public void testLoginUser_Success() {
        when(userService.authenticateUser("testuser", "password123")).thenReturn(user);

        ResponseEntity<?> response = authController.loginUser(Map.of("username", "testuser", "password", "password123"));

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Login successful!", ((Map<?, ?>) response.getBody()).get("message"));
        verify(userService, times(1)).authenticateUser("testuser", "password123");
    }

    @Test
    public void testLoginUser_Failure() {
        when(userService.authenticateUser("testuser", "wrongpassword")).thenReturn(null);

        ResponseEntity<?> response = authController.loginUser(Map.of("username", "testuser", "password", "wrongpassword"));

        assertEquals(401, response.getStatusCodeValue());
        assertEquals("Invalid username or password", response.getBody());
    }

    @Test
    public void testGetUser_UserFound() {
        when(userService.getUserByUsername("testuser")).thenReturn(Optional.of(user));

        ResponseEntity<User> response = authController.getUser("testuser");

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("testuser", response.getBody().getUsername());
    }

    @Test
    public void testGetUser_UserNotFound() {
        when(userService.getUserByUsername("unknown")).thenReturn(Optional.empty());

        ResponseEntity<User> response = authController.getUser("unknown");

        assertEquals(404, response.getStatusCodeValue());
    }
}
