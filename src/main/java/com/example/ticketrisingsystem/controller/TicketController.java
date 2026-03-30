package com.example.ticketrisingsystem.controller;

import com.example.ticketrisingsystem.dto.AssignTicketRequest;
import com.example.ticketrisingsystem.dto.CreateTicketRequest;
import com.example.ticketrisingsystem.dto.ResolveTicketRequest;
import com.example.ticketrisingsystem.dto.TicketResponse;
import com.example.ticketrisingsystem.entity.Ticket;
import com.example.ticketrisingsystem.repository.TicketRepository;
import com.example.ticketrisingsystem.service.TicketService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tickets")
@RequiredArgsConstructor
@Tag(name = "Tickets", description = "Ticket management endpoints")
@SecurityRequirement(name = "bearer_token")
public class TicketController {

    private final TicketService ticketService;
    private final TicketRepository ticketRepository;

    @PostMapping
    @Operation(summary = "Raise a ticket")
    public ResponseEntity<TicketResponse> create(@Valid @RequestBody CreateTicketRequest request,
                                                 Authentication authentication) {
        String username = authentication.getName();
        Ticket created = ticketService.raiseTicket(request.getTitle(), request.getDescription(), username);
        return ResponseEntity.status(HttpStatus.CREATED).body(TicketResponse.fromEntity(created));
    }

    @GetMapping("/my")
    @Operation(summary = "Get my tickets (created by me)")
    public List<TicketResponse> myTickets(Authentication authentication) {
        String username = authentication.getName();
        return ticketRepository.findByCreatedByOrderByCreatedAtDesc(username)
                .stream()
                .map(TicketResponse::fromEntity)
                .toList();
    }

    @GetMapping
    @Operation(summary = "Get all tickets")
    public List<TicketResponse> allTickets() {
        return ticketService.getAllTickets()
                .stream()
                .map(TicketResponse::fromEntity)
                .toList();
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete ticket", description = "Delete an OPEN ticket owned by the logged-in user")
    public ResponseEntity<Void> deleteTicket(@PathVariable Long id, Authentication authentication) {
        String username = authentication.getName();
        ticketService.deleteUserTicket(id, username);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}/assign")
    @Operation(summary = "Assign a ticket (ADMIN only)")
    public TicketResponse assign(@PathVariable Long id,
                                 @Valid @RequestBody AssignTicketRequest request,
                                 Authentication authentication) {
        String changedBy = authentication.getName();
        boolean isAdmin = isAdmin(authentication);
        Ticket updated = ticketService.assignTicket(id, request.getAssignedTo(), changedBy, isAdmin);
        return TicketResponse.fromEntity(updated);
    }

    @PutMapping("/{id}/start")
    @Operation(summary = "Start working on a ticket (assignee)")
    public TicketResponse start(@PathVariable Long id, Authentication authentication) {
        String changedBy = authentication.getName();
        Ticket updated = ticketService.startTicket(id, changedBy);
        return TicketResponse.fromEntity(updated);
    }

    @PutMapping("/{id}/resolve")
    @Operation(summary = "Resolve a ticket")
    public TicketResponse resolve(@PathVariable Long id,
                                  @Valid @RequestBody ResolveTicketRequest request,
                                  Authentication authentication) {
        String changedBy = authentication.getName();
        Ticket updated = ticketService.resolveTicket(id, request.getResolutionNote(), changedBy);
        return TicketResponse.fromEntity(updated);
    }

    @PutMapping("/{id}/close")
    @Operation(summary = "Close a ticket")
    public TicketResponse close(@PathVariable Long id, Authentication authentication) {
        String changedBy = authentication.getName();
        Ticket updated = ticketService.closeTicket(id, changedBy);
        return TicketResponse.fromEntity(updated);
    }

    private boolean isAdmin(Authentication authentication) {
        if (authentication == null) {
            return false;
        }

        for (GrantedAuthority authority : authentication.getAuthorities()) {
            if ("ROLE_ADMIN".equals(authority.getAuthority())) {
                return true;
            }
        }
        return false;
    }
}
