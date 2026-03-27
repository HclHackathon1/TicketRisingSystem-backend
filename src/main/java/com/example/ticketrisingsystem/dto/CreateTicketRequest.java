package com.example.ticketrisingsystem.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@Schema(description = "Request payload to raise a ticket")
public class CreateTicketRequest {

    @NotBlank
    @Size(max = 150)
    @Schema(example = "Unable to login")
    private String title;

    @NotBlank
    @Size(max = 2000)
    @Schema(example = "I am getting invalid credentials even with correct password")
    private String description;
}
