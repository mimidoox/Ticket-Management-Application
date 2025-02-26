/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.ticketManager.servicesTest;

/**
 *
 * @author macbookmimid
 */

import com.example.ticketManager.entities.Comment;
import com.example.ticketManager.entities.Ticket;
import com.example.ticketManager.repos.CommentRepository;
import com.example.ticketManager.services.CommentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CommentServiceTest {

    @Mock
    private CommentRepository commentRepository;

    @InjectMocks
    private CommentService commentService;

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
        when(commentRepository.save(any(Comment.class))).thenReturn(comment);

        Comment createdComment = commentService.addComment(comment);

        assertNotNull(createdComment);
        assertEquals("Test Comment", createdComment.getCommentText());
        verify(commentRepository, times(1)).save(comment);
    }

    @Test
    public void testGetCommentsByTicket() {
        when(commentRepository.findByTicket(ticket)).thenReturn(Arrays.asList(comment));

        List<Comment> comments = commentService.getCommentsByTicket(ticket);

        assertNotNull(comments);
        assertEquals(1, comments.size());
        assertEquals("Test Comment", comments.get(0).getCommentText());
        verify(commentRepository, times(1)).findByTicket(ticket);
    }
}
