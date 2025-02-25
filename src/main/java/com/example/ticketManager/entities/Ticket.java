/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.ticketManager.entities;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.List;

/**
 *
 * @author macbookmimid
 */

@Entity
@Table(name="TICKET")
public class Ticket {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long ticketId;
    private String title;
    private String description;
    private String priority; // Low, Medium, High
    @ManyToOne
    private Category category; // Network, Hardware, Software, Other
    private LocalDateTime creationDate = LocalDateTime.now();
    private String status; // New, In Progress, Resolved
    
    @ManyToOne
    private User createdBy;

    @OneToMany(mappedBy = "ticket", cascade = CascadeType.ALL)
    private List<Comment> comments;

    public Ticket() {
    }

    public Ticket(String title, String description, String priority, Category category, String status, User createdBy, List<Comment> comments) {
        this.title = title;
        this.description = description;
        this.priority = priority;
        this.category = category;
        this.creationDate = LocalDateTime.now();
        this.status = status;
        this.createdBy = createdBy;
        this.comments = comments;
    }
    
    public Ticket(String title, String description, String priority, Category category, String status, User createdBy) {
        this.title = title;
        this.description = description;
        this.priority = priority;
        this.category = category;
        this.creationDate = LocalDateTime.now();
        this.status = status;
        this.createdBy = createdBy;
        
    }

    public Ticket(Long ticketId, String title, String description, String priority, Category category, String status, User createdBy, List<Comment> comments) {
        this.ticketId = ticketId;
        this.title = title;
        this.description = description;
        this.priority = priority;
        this.category = category;
        this.creationDate = LocalDateTime.now();
        this.status = status;
        this.createdBy = createdBy;
        this.comments = comments;
    }

    public Long getTicketId() {
        return ticketId;
    }

    public void setTicketId(Long ticketId) {
        this.ticketId = ticketId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public LocalDateTime getCreationDate() {
        return this.creationDate;
    }

    public void setCreationDate(LocalDateTime creationDate) {
        this.creationDate = creationDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public User getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(User createdBy) {
        this.createdBy = createdBy;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }

    @Override
    public String toString() {
        return "Ticket{" + "ticketId=" + ticketId + ", title=" + title + ", description=" + description + ", priority=" + priority + ", category=" + category + ", creationDate=" + creationDate + ", status=" + status + ", createdBy=" + createdBy + ", comments=" + comments + '}';
    }
    
    
    
}
