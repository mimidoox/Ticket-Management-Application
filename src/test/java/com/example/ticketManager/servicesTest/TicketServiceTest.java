/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.ticketManager.servicesTest;

/**
 *
 * @author macbookmimid
 */


import com.example.ticketManager.entities.AuditLog;
import com.example.ticketManager.entities.Comment;
import com.example.ticketManager.entities.Ticket;
import com.example.ticketManager.entities.User;
import com.example.ticketManager.repos.AuditLogRepository;
import com.example.ticketManager.repos.CommentRepository;
import com.example.ticketManager.repos.TicketRepository;
import com.example.ticketManager.services.TicketService;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TicketServiceTest {

    @Mock
    private TicketRepository ticketRepository;

    @Mock
    private CommentRepository commentRepository;

    @Mock
    private AuditLogRepository auditLogRepository;

    @InjectMocks
    private TicketService ticketService;

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
        ticket.setCreationDate(LocalDateTime.now());
    }

    @Test
    public void testCreateTicket() {
        when(ticketRepository.save(any(Ticket.class))).thenReturn(ticket);

        Ticket createdTicket = ticketService.createTicket(ticket);

        assertNotNull(createdTicket);
        assertEquals("Test Ticket", createdTicket.getTitle());
        verify(ticketRepository, times(1)).save(ticket);
    }

    @Test
    public void testGetTicketsByUser() {
        when(ticketRepository.findByCreatedBy(user)).thenReturn(Arrays.asList(ticket));

        List<Ticket> tickets = ticketService.getTicketsByUser(user);

        assertNotNull(tickets);
        assertEquals(1, tickets.size());
        assertEquals("Test Ticket", tickets.get(0).getTitle());
        verify(ticketRepository, times(1)).findByCreatedBy(user);
    }

    @Test
    public void testDeleteTicket() {
        when(ticketRepository.existsById(1L)).thenReturn(true);

        ticketService.deleteTicket(1L);

        verify(ticketRepository, times(1)).deleteById(1L);
    }

    @Test
    public void testDeleteTicket_NotFound() {
        when(ticketRepository.existsById(1L)).thenReturn(false);

        assertThrows(EntityNotFoundException.class, () -> ticketService.deleteTicket(1L));
    }

    @Test
    public void testUpdateTicketStatus() {
        when(ticketRepository.findById(1L)).thenReturn(Optional.of(ticket));
        when(ticketRepository.save(any(Ticket.class))).thenReturn(ticket);

        Ticket updatedTicket = ticketService.updateTicketStatus(1L, "Closed", "admin");

        assertNotNull(updatedTicket);
        assertEquals("Closed", updatedTicket.getStatus());
        verify(auditLogRepository, times(1)).save(any(AuditLog.class));
    }

    @Test
    public void testGetCommentsByTicket() {
        Comment comment = new Comment();
        comment.setCommentText("Test Comment");

        when(commentRepository.findByTicket(ticket)).thenReturn(Arrays.asList(comment));

        List<Comment> comments = ticketService.getCommentsByTicket(ticket);

        assertNotNull(comments);
        assertEquals(1, comments.size());
        assertEquals("Test Comment", comments.get(0).getCommentText());
        verify(commentRepository, times(1)).findByTicket(ticket);
    }

    @Test
    public void testAddCommentToTicket() {
        Comment newComment = new Comment();
        newComment.setCommentText("New Comment");

        when(commentRepository.save(any(Comment.class))).thenReturn(newComment);

        ticketService.addCommentToTicket(ticket, "New Comment", user);

        verify(commentRepository, times(1)).save(any(Comment.class));
        verify(auditLogRepository, times(1)).save(any(AuditLog.class));
    }

    @Test
    public void testGetTicketById() {
        when(ticketRepository.findById(1L)).thenReturn(Optional.of(ticket));

        Optional<Ticket> foundTicket = ticketService.getTicketById(1L);

        assertTrue(foundTicket.isPresent());
        assertEquals("Test Ticket", foundTicket.get().getTitle());
    }

    @Test
    public void testGetAllTickets() {
        when(ticketRepository.findAll()).thenReturn(Arrays.asList(ticket));

        List<Ticket> tickets = ticketService.getAllTickets();

        assertNotNull(tickets);
        assertEquals(1, tickets.size());
        assertEquals("Test Ticket", tickets.get(0).getTitle());
    }
}
