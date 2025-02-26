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
import com.example.ticketManager.repos.AuditLogRepository;
import com.example.ticketManager.services.AuditLogService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AuditLogServiceTest {

    @Mock
    private AuditLogRepository auditLogRepository;

    @InjectMocks
    private AuditLogService auditLogService;

    private AuditLog auditLog;

    @BeforeEach
    public void setUp() {
        auditLog = new AuditLog();
        auditLog.setTicketId(1L);
        auditLog.setAction("Status Change");
    }

    @Test
    public void testGetAuditLogsByTicketId() {
        when(auditLogRepository.findByTicketId(1L)).thenReturn(Arrays.asList(auditLog));

        List<AuditLog> logs = auditLogService.getAuditLogsByTicketId(1L);

        assertNotNull(logs);
        assertEquals(1, logs.size());
        assertEquals("Status Change", logs.get(0).getAction());
        verify(auditLogRepository, times(1)).findByTicketId(1L);
    }

    @Test
    public void testGetAllAuditLogs() {
        when(auditLogRepository.findAll()).thenReturn(Arrays.asList(auditLog));

        List<AuditLog> logs = auditLogService.getAll();

        assertNotNull(logs);
        assertEquals(1, logs.size());
        assertEquals("Status Change", logs.get(0).getAction());
        verify(auditLogRepository, times(1)).findAll();
    }
}