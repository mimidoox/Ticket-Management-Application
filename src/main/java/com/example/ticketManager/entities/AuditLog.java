/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.ticketManager.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalDateTime;

/**
 *
 * @author macbookmimid
 */
@Entity
@Table(name = "AUDIT_LOG")
public class AuditLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long auditId;
    private Long ticketId;
    private String action; // Status Change, Comment Added
    private String oldValue;
    private String newValue;
    private LocalDateTime changeDate;
    private String changedBy;
    private String commentText;

    // Constructors, Getters, Setters, and toString() method

    public AuditLog() {
    }

    public AuditLog(Long ticketId, String action, String oldValue, String newValue, LocalDateTime changeDate, String changedBy) {
        this.ticketId = ticketId;
        this.action = action;
        this.oldValue = oldValue;
        this.newValue = newValue;
        this.changeDate = changeDate;
        this.changedBy = changedBy;
    }

    public AuditLog(Long ticketId, String action, LocalDateTime changeDate, String changedBy, String comment) {
        this.ticketId = ticketId;
        this.action = action;
        this.changeDate = changeDate;
        this.changedBy = changedBy;
        this.commentText = comment;
    }
    
    

    public AuditLog(Long auditId, Long ticketId, String action, String oldValue, String newValue, LocalDateTime changeDate, String changedBy) {
        this.auditId = auditId;
        this.ticketId = ticketId;
        this.action = action;
        this.oldValue = oldValue;
        this.newValue = newValue;
        this.changeDate = changeDate;
        this.changedBy = changedBy;
    }

    public Long getAuditId() {
        return auditId;
    }

    public void setAuditId(Long auditId) {
        this.auditId = auditId;
    }

    public Long getTicketId() {
        return ticketId;
    }

    public void setTicketId(Long ticketId) {
        this.ticketId = ticketId;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getOldValue() {
        return oldValue;
    }

    public void setOldValue(String oldValue) {
        this.oldValue = oldValue;
    }

    public String getNewValue() {
        return newValue;
    }

    public void setNewValue(String newValue) {
        this.newValue = newValue;
    }

    public LocalDateTime getChangeDate() {
        return changeDate;
    }

    public void setChangeDate(LocalDateTime changeDate) {
        this.changeDate = changeDate;
    }

    public String getChangedBy() {
        return changedBy;
    }

    public void setChangedBy(String changedBy) {
        this.changedBy = changedBy;
    }

    public String getComment() {
        return commentText;
    }

    public void setComment(String comment) {
        this.commentText = comment;
    }

    @Override
    public String toString() {
        return "AuditLog{" + "auditId=" + auditId + ", ticketId=" + ticketId + ", action=" + action + ", oldValue=" + oldValue + ", newValue=" + newValue + ", changeDate=" + changeDate + ", changedBy=" + changedBy + '}';
    }
    
}