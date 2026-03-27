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
@Schema(description = "JWT Token response")
public class TokenResponse {

    @Schema(description = "JWT token", example = "eyJhbGciOiJIUzUxMiJ9...")
    private String token;

    @Schema(description = "Token type", example = "Bearer")
    private String type;

    @Schema(description = "Username", example = "admin")
    private String username;

    @Schema(description = "Token expiration time in milliseconds", example = "3600000")
    private long expiresIn;

    public String getTokenWithBearer() {
        return String.format("%s %s", type, token);
    }
}
