package com.example.ticketrisingsystem.controller;

import com.example.ticketrisingsystem.dto.AssignTicketRequest;
import com.example.ticketrisingsystem.dto.CreateTicketRequest;
import com.example.ticketrisingsystem.dto.ResolveTicketRequest;
import com.example.ticketrisingsystem.dto.TicketResponse;
import com.example.ticketrisingsystem.dto.TicketTransitionResponse;
import com.example.ticketrisingsystem.entity.Ticket;
import com.example.ticketrisingsystem.service.TicketService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/tickets")
@Tag(name = "Tickets", description = "Ticket management endpoints")
public class TicketController {

    private final TicketService ticketService;

    public TicketController(TicketService ticketService) {
        this.ticketService = ticketService;
    }

    @GetMapping("/health")
    @Operation(summary = "Health check", description = "Check if the API is running")
    @ApiResponse(responseCode = "200", description = "API is running")
    public ResponseEntity<String> health() {
        return ResponseEntity.ok("Ticket Rising System API is running");
    }

    @PostMapping
    @Operation(summary = "Raise ticket", description = "Raise a new complaint/ticket")
    @SecurityRequirement(name = "bearer_token")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Ticket raised successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    public ResponseEntity<TicketResponse> raiseTicket(@Valid @RequestBody CreateTicketRequest request,
                                                      Authentication authentication) {
        Ticket ticket = ticketService.raiseTicket(request.getTitle(), request.getDescription(), authentication.getName());
        return ResponseEntity.status(HttpStatus.CREATED).body(TicketResponse.fromEntity(ticket));
    }

    @GetMapping
    @Operation(summary = "Get all tickets", description = "Retrieve all tickets")
    @SecurityRequirement(name = "bearer_token")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Tickets retrieved successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    public ResponseEntity<List<TicketResponse>> getAllTickets() {
        log.info("Fetching all tickets");
        List<TicketResponse> response = ticketService.getAllTickets().stream().map(TicketResponse::fromEntity).toList();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get ticket by ID", description = "Retrieve a specific ticket by ID")
    @SecurityRequirement(name = "bearer_token")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ticket found"),
            @ApiResponse(responseCode = "404", description = "Ticket not found"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    public ResponseEntity<TicketResponse> getTicketById(@PathVariable Long id) {
        log.info("Fetching ticket with ID: {}", id);
        return ResponseEntity.ok(TicketResponse.fromEntity(ticketService.getTicket(id)));
    }

    @PutMapping("/{id}/assign")
    @Operation(summary = "Assign ticket", description = "Assign ticket to an admin/user. Only admin can assign.")
    @SecurityRequirement(name = "bearer_token")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ticket assigned successfully"),
            @ApiResponse(responseCode = "403", description = "Forbidden - only admin can assign")
    })
    public ResponseEntity<TicketResponse> assignTicket(@PathVariable Long id,
                                                       @Valid @RequestBody AssignTicketRequest request,
                                                       Authentication authentication) {
        boolean isAdmin = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .anyMatch(role -> role.equals("ROLE_ADMIN"));

        Ticket ticket = ticketService.assignTicket(id, request.getAssignedTo(), authentication.getName(), isAdmin);
        return ResponseEntity.ok(TicketResponse.fromEntity(ticket));
    }

    @PutMapping("/{id}/resolve")
    @Operation(summary = "Resolve ticket", description = "Resolve a ticket with a resolution note")
    @SecurityRequirement(name = "bearer_token")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ticket resolved successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid status transition")
    })
    public ResponseEntity<TicketResponse> resolveTicket(@PathVariable Long id,
                                                        @Valid @RequestBody ResolveTicketRequest request,
                                                        Authentication authentication) {
        Ticket ticket = ticketService.resolveTicket(id, request.getResolutionNote(), authentication.getName());
        return ResponseEntity.ok(TicketResponse.fromEntity(ticket));
    }

    @PutMapping("/{id}/close")
    @Operation(summary = "Close ticket", description = "Close a resolved ticket. Cannot close without resolution note.")
    @SecurityRequirement(name = "bearer_token")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ticket closed successfully"),
            @ApiResponse(responseCode = "400", description = "Cannot close without resolution note")
    })
    public ResponseEntity<TicketResponse> closeTicket(@PathVariable Long id, Authentication authentication) {
        Ticket ticket = ticketService.closeTicket(id, authentication.getName());
        return ResponseEntity.ok(TicketResponse.fromEntity(ticket));
    }

    @GetMapping("/{id}/transitions")
    @Operation(summary = "Get status transitions", description = "Track status transitions for a ticket")
    @SecurityRequirement(name = "bearer_token")
    @ApiResponse(responseCode = "200", description = "Transitions fetched")
    public ResponseEntity<List<TicketTransitionResponse>> getTransitions(@PathVariable Long id) {
        List<TicketTransitionResponse> transitions = ticketService.getTransitions(id).stream()
                .map(TicketTransitionResponse::fromEntity)
                .toList();
        return ResponseEntity.ok(transitions);
    }
}
