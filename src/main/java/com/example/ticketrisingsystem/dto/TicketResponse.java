package com.example.ticketrisingsystem.dto;

import com.example.ticketrisingsystem.entity.Ticket;
import com.example.ticketrisingsystem.entity.TicketStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Value;

import java.time.Instant;

@Value
@Builder
@Schema(description = "Ticket response")
public class TicketResponse {
    Long id;
    String title;
    String description;
    TicketStatus status;
    String createdBy;
    String assignedTo;
    String resolutionNote;
    Instant createdAt;
    Instant updatedAt;

    public static TicketResponse fromEntity(Ticket ticket) {
        return TicketResponse.builder()
                .id(ticket.getId())
                .title(ticket.getTitle())
                .description(ticket.getDescription())
                .status(ticket.getStatus())
                .createdBy(ticket.getCreatedBy())
                .assignedTo(ticket.getAssignedTo() != null ? ticket.getAssignedTo().getUsername() : null)
                .resolutionNote(ticket.getResolutionNote())
                .createdAt(ticket.getCreatedAt())
                .updatedAt(ticket.getUpdatedAt())
                .build();
    }
}
