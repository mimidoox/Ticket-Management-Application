/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.ticketManager.services;

import com.example.ticketManager.entities.AuditLog;
import com.example.ticketManager.repos.AuditLogRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author macbookmimid
 */
@Service
public class AuditLogService {
    @Autowired
    private AuditLogRepository auditLogRepository;

    public List<AuditLog> getAuditLogsByTicketId(Long ticketId) {
        return auditLogRepository.findByTicketId(ticketId);
    }
    
        public List<AuditLog> getAll() {
        return auditLogRepository.findAll();
    }
}