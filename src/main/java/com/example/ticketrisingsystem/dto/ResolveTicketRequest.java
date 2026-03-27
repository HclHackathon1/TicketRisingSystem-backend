package com.example.ticketrisingsystem.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@Schema(description = "Request payload to resolve/close a ticket")
public class ResolveTicketRequest {

    @NotBlank
    @Size(max = 2000)
    @Schema(example = "Issue fixed by resetting user MFA settings")
    private String resolutionNote;
}
