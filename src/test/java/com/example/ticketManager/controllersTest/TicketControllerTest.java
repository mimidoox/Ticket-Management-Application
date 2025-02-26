/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.ticketManager.controllersTest;

/**
 *
 * @author macbookmimid
 */
import com.example.ticketManager.controllers.TicketController;
import com.example.ticketManager.entities.Ticket;
import com.example.ticketManager.entities.User;
import com.example.ticketManager.services.TicketService;
import com.example.ticketManager.services.UserService;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TicketControllerTest {

    @Mock
    private TicketService ticketService;

    @Mock
    private UserService userService;

    @InjectMocks
    private TicketController ticketController;

    private Ticket ticket;
    private User user;

    @BeforeEach
    public void setUp() {
        user = new User();
        user.setUsername("testuser");

        ticket = new Ticket();
        ticket.setTicketId(1L);
        ticket.setTitle("Test Ticket");
        ticket.setDescription("Test Description");
        ticket.setCreatedBy(user);
    }

    @Test
    public void testCreateTicket() {
        when(ticketService.createTicket(any(Ticket.class))).thenReturn(ticket);

        ResponseEntity<Ticket> response = ticketController.createTicket(ticket);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals("Test Ticket", response.getBody().getTitle());
        verify(ticketService, times(1)).createTicket(ticket);
    }

    @Test
    public void testGetAllTickets() {
        when(ticketService.getAllTickets()).thenReturn(Arrays.asList(ticket));

        List<Ticket> tickets = ticketController.getAllTickets();

        assertNotNull(tickets);
        assertEquals(1, tickets.size());
        assertEquals("Test Ticket", tickets.get(0).getTitle());
        verify(ticketService, times(1)).getAllTickets();
    }

    @Test
    public void testGetTicketsByUser_UserFound() {
        when(userService.getUserByUsername("testuser")).thenReturn(Optional.of(user));
        when(ticketService.getTicketsByUser(user)).thenReturn(Arrays.asList(ticket));

        ResponseEntity<List<Ticket>> response = ticketController.getTicketsByUser("testuser");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
        assertEquals("Test Ticket", response.getBody().get(0).getTitle());
    }

    @Test
    public void testGetTicketsByUser_UserNotFound() {
        when(userService.getUserByUsername("unknown")).thenReturn(Optional.empty());

        ResponseEntity<List<Ticket>> response = ticketController.getTicketsByUser("unknown");

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void testUpdateTicketStatus_Success() {
        when(ticketService.updateTicketStatus(1L, "In Progress", "admin")).thenReturn(ticket);

        ResponseEntity<?> response = ticketController.updateTicketStatus(1L, "In Progress", "admin");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Test Ticket", ((Ticket) response.getBody()).getTitle());
    }

    @Test
    public void testUpdateTicketStatus_TicketNotFound() {
        when(ticketService.updateTicketStatus(1L, "In Progress", "admin")).thenThrow(new EntityNotFoundException("Ticket not found."));

        ResponseEntity<?> response = ticketController.updateTicketStatus(1L, "In Progress", "admin");

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Ticket not found.", response.getBody());
    }

    @Test
    public void testDeleteTicket_Success() {
        doNothing().when(ticketService).deleteTicket(1L);

        ResponseEntity<String> response = ticketController.deleteTicket(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Ticket with ID 1 deleted successfully.", response.getBody());
    }

    @Test
    public void testDeleteTicket_TicketNotFound() {
        doThrow(new EntityNotFoundException("Ticket not found.")).when(ticketService).deleteTicket(1L);

        ResponseEntity<String> response = ticketController.deleteTicket(1L);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Ticket not found.", response.getBody());
    }
}
