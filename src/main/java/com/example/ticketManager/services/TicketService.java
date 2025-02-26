/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.ticketManager.services;

import com.example.ticketManager.entities.AuditLog;
import com.example.ticketManager.entities.Category;
import com.example.ticketManager.entities.Comment;
import com.example.ticketManager.entities.Ticket;
import com.example.ticketManager.entities.User;
import com.example.ticketManager.repos.AuditLogRepository;
import com.example.ticketManager.repos.CommentRepository;
import com.example.ticketManager.repos.TicketRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author macbookmimid
 */
@Service
public class TicketService {
    
    @Autowired
    private TicketRepository ticketRepository;
    
    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private AuditLogRepository auditLogRepository;
    
    public TicketService(TicketRepository ticketRepository, CommentRepository commentRepository,AuditLogRepository auditLogRepository) {
        this.ticketRepository = ticketRepository;
        this.commentRepository = commentRepository;
        this.auditLogRepository = auditLogRepository;
    }

    public Ticket createTicket(Ticket ticket) {
        ticket.setCreationDate(LocalDateTime.now());
        return ticketRepository.save(ticket);
    }

    public List<Ticket> getTicketsByUser(User user) {
        return ticketRepository.findByCreatedBy(user);
    }
    @Transactional
    public void deleteTicket(Long id) {
        if (!ticketRepository.existsById(id)) {
            throw new EntityNotFoundException("Ticket with id " + id + " not found.");
        }
        ticketRepository.deleteById(id);
    }

    public Ticket updateTicketStatus(Long ticketId, String newStatus, String changedBy) {
        Ticket ticket = ticketRepository.findById(ticketId).orElseThrow(() -> new RuntimeException("Ticket not found"));
        String oldStatus = ticket.getStatus();
        ticket.setStatus(newStatus);
        ticketRepository.save(ticket);

        AuditLog auditLog = new AuditLog();
        auditLog.setTicketId(ticketId);
        auditLog.setAction("Status Change");
        auditLog.setOldValue(oldStatus);
        auditLog.setNewValue(newStatus);
        auditLog.setChangeDate(LocalDateTime.now());
        auditLog.setChangedBy(changedBy);
        auditLogRepository.save(auditLog);

        return ticket;
    }
    
    public List<Comment> getCommentsByTicket(Ticket ticket) {
        return commentRepository.findByTicket(ticket);
}

    public void addCommentToTicket(Ticket ticket, String commentText, User supportUser) {
    
    Comment newComment = new Comment();
    newComment.setTicket(ticket);
    newComment.setCommentText(commentText);
    newComment.setCreatedBy(supportUser); 
    newComment.setCreatedDate(LocalDateTime.now()); 

    
    commentRepository.save(newComment);
    
    AuditLog auditLog = new AuditLog();
        auditLog.setTicketId(ticket.getTicketId());
        auditLog.setAction("New comment");
        auditLog.setChangeDate(LocalDateTime.now());
        auditLog.setChangedBy(supportUser.getFirstName()+" "+supportUser.getLastName());
        auditLog.setComment(commentText);
        auditLogRepository.save(auditLog);
}
    public Optional<Ticket> getTicketById(Long ticketId) {
        return ticketRepository.findById(ticketId);
    }
    
    public List<Ticket> getAllTickets() {
        return ticketRepository.findAll();
    }
}
