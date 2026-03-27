package com.example.ticketrisingsystem.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@Schema(description = "Request payload to assign a ticket")
public class AssignTicketRequest {

    @NotBlank
    @Size(max = 100)
    @Schema(example = "admin")
    private String assignedTo;
}
