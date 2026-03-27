package com.example.ticketrisingsystem.controller;

import com.example.ticketrisingsystem.service.TicketService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping({"/api/tickets", "/tickets"})
@RequiredArgsConstructor
@Tag(name = "Tickets", description = "User ticket endpoints")
public class TicketController {

    private final TicketService ticketService;

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete ticket", description = "Delete an OPEN ticket owned by the logged-in user")
    public ResponseEntity<Void> deleteTicket(@PathVariable Long id, Authentication authentication) {
        String username = authentication.getName();
        ticketService.deleteUserTicket(id, username);
        return ResponseEntity.noContent().build();
    }
}
