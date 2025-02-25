/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.ticketManager.controllers;

import com.example.ticketManager.entities.Ticket;
import com.example.ticketManager.entities.User;
import com.example.ticketManager.services.TicketService;
import com.example.ticketManager.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author macbookmimid
 */
@RestController
@RequestMapping("/tickets")
@Tag(name = "Ticket API", description = "APIs for managing tickets")
public class TicketController {

    private final TicketService ticketService;
    private final UserService userService;

    public TicketController(TicketService ticketService, UserService userService) {
        this.ticketService = ticketService;
        this.userService = userService;
    }

    @PostMapping("/create")
    public ResponseEntity<Ticket> createTicket(@RequestBody Ticket ticket) {
        Ticket createdTicket = ticketService.createTicket(ticket);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdTicket);
    }

    @GetMapping("/all")
    public List<Ticket> getAllTickets() {
        List<Ticket> tickets = ticketService.getAllTickets();
         return tickets;
    }

    @GetMapping("/user/{username}")
    @Operation(
        summary = "Get tickets by user",
        description = "Retrieve all tickets created by a specific user",
        responses = {
            @ApiResponse(responseCode = "200", description = "Tickets found"),
            @ApiResponse(responseCode = "404", description = "User not found"),
            @ApiResponse(responseCode = "204", description = "No tickets found for user")
        }
    )
    public ResponseEntity<List<Ticket>> getTicketsByUser(@PathVariable String username) {
        Optional<User> userOptional = userService.getUserByUsername(username);

        if (userOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        List<Ticket> tickets = ticketService.getTicketsByUser(userOptional.get());
        if (tickets.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(tickets);
    }

    @PutMapping("/{ticketId}/status")
    @Operation(
        summary = "Update ticket status",
        description = "Update the status of a ticket and track the change in the audit log.",
        responses = {
            @ApiResponse(responseCode = "200", description = "Ticket status updated successfully"),
            @ApiResponse(responseCode = "404", description = "Ticket not found"),
            @ApiResponse(responseCode = "400", description = "Invalid status or changedBy value")
        }
    )
    public ResponseEntity<?> updateTicketStatus(
        @Parameter(description = "ID of the ticket to update", required = true, example = "1")
        @PathVariable Long ticketId,

        @Parameter(description = "New status for the ticket", required = true, example = "In Progress")
        @RequestParam String newStatus,

        @Parameter(description = "Username of the user making the change", required = true, example = "admin")
        @RequestParam String changedBy) {

        try {
            Ticket updatedTicket = ticketService.updateTicketStatus(ticketId, newStatus, changedBy);
            return ResponseEntity.ok(updatedTicket);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Ticket not found.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid status or changedBy.");
        }
    }
     @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteTicket(@PathVariable Long id) {
        try {
            ticketService.deleteTicket(id);
            return ResponseEntity.ok("Ticket with ID " + id + " deleted successfully.");
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
}
