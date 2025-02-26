/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.ticketManager.controllersTest;

/**
 *
 * @author macbookmimid
 */
import com.example.ticketManager.controllers.CommentController;
import com.example.ticketManager.entities.Comment;
import com.example.ticketManager.entities.Ticket;
import com.example.ticketManager.services.CommentService;
import com.example.ticketManager.services.TicketService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CommentControllerTest {

    @Mock
    private CommentService commentService;

    @Mock
    private TicketService ticketService;

    @InjectMocks
    private CommentController commentController;

    private Comment comment;
    private Ticket ticket;

    @BeforeEach
    public void setUp() {
        ticket = new Ticket();
        ticket.setTicketId(1L);

        comment = new Comment();
        comment.setCommentText("Test Comment");
        comment.setTicket(ticket);
    }

    @Test
    public void testAddComment() {
        when(commentService.addComment(any(Comment.class))).thenReturn(comment);

        Comment createdComment = commentController.addComment(comment);

        assertNotNull(createdComment);
        assertEquals("Test Comment", createdComment.getCommentText());
        verify(commentService, times(1)).addComment(comment);
    }

    @Test
    public void testGetCommentsByTicket_TicketFound() {
        when(ticketService.getTicketById(1L)).thenReturn(Optional.of(ticket));
        when(commentService.getCommentsByTicket(ticket)).thenReturn(Arrays.asList(comment));

        ResponseEntity<List<Comment>> response = commentController.getCommentsByTicket(1L);

        assertEquals(HttpStatusCode.valueOf(200), response.getStatusCode());
        assertEquals(1, response.getBody().size());
        assertEquals("Test Comment", response.getBody().get(0).getCommentText());
    }

    @Test
    public void testGetCommentsByTicket_TicketNotFound() {
        when(ticketService.getTicketById(1L)).thenReturn(Optional.empty());

        ResponseEntity<List<Comment>> response = commentController.getCommentsByTicket(1L);

        assertEquals(HttpStatusCode.valueOf(404), response.getStatusCode());
    }
}
