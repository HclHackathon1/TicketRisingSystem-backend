package com.example.ticketrisingsystem.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Login request payload")
public class LoginRequest {

    @Schema(description = "Username", example = "admin")
    private String username;

    @Schema(description = "Password", example = "admin123")
    private String password;
}
