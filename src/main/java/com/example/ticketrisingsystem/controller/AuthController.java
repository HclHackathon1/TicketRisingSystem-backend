package com.example.ticketrisingsystem.controller;

import com.example.ticketrisingsystem.dto.LoginRequest;
import com.example.ticketrisingsystem.dto.TokenResponse;
import com.example.ticketrisingsystem.model.User;
import com.example.ticketrisingsystem.repository.UserRepository;
import com.example.ticketrisingsystem.security.JwtTokenProvider;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/auth")
@Tag(name = "Authentication", description = "Authentication endpoints")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenProvider tokenProvider;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/login")
    @Operation(summary = "User login", description = "Authenticate user and get JWT token")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Login successful",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = TokenResponse.class))),
            @ApiResponse(responseCode = "401", description = "Invalid credentials"),
            @ApiResponse(responseCode = "400", description = "Bad request")
    })
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.getUsername(),
                            loginRequest.getPassword()
                    )
            );

            String token = tokenProvider.generateTokenFromAuthentication(authentication);
            log.info("User {} logged in successfully", loginRequest.getUsername());

            return ResponseEntity.ok(TokenResponse.builder()
                    .token(token)
                    .type("Bearer")
                    .username(loginRequest.getUsername())
                    .expiresIn(tokenProvider.getTokenExpirationMs())
                    .build());
        } catch (Exception e) {
            log.error("Login failed for user {}: {}", loginRequest.getUsername(), e.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("{\"error\": \"Invalid username or password\"}");
        }
    }

        @GetMapping("/login")
        @Operation(summary = "User login (GET)", description = "Authenticate user and get JWT token using query parameters. NOTE: For security, POST with JSON body is recommended.")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Login successful",
                                        content = @Content(mediaType = "application/json",
                                                        schema = @Schema(implementation = TokenResponse.class))),
                        @ApiResponse(responseCode = "401", description = "Invalid credentials"),
                        @ApiResponse(responseCode = "400", description = "Bad request")
        })
        public ResponseEntity<?> loginGet(@RequestParam String username, @RequestParam String password) {
                LoginRequest loginRequest = new LoginRequest();
                loginRequest.setUsername(username);
                loginRequest.setPassword(password);
                return login(loginRequest);
        }

    @PostMapping("/signup")
    @Operation(summary = "User signup", description = "Register a new user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "User registered successfully"),
            @ApiResponse(responseCode = "400", description = "Bad request")
    })
    public ResponseEntity<?> signup(@RequestBody LoginRequest signupRequest) {
        try {
            if (userRepository.existsByUsername(signupRequest.getUsername())) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("{\"error\": \"Username already exists\"}");
            }

            User user = User.builder()
                    .username(signupRequest.getUsername())
                    .password(passwordEncoder.encode(signupRequest.getPassword()))
                    .role(User.Role.USER)
                    .build();

            userRepository.save(user);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body("{\"message\": \"User registered successfully\"}");
        } catch (Exception e) {
            log.error("Signup failed for user {}: {}", signupRequest.getUsername(), e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("{\"error\": \"Could not register user\"}");
        }
    }

        @GetMapping("/signup")
        @Operation(summary = "User signup (GET)", description = "Register a new user using query parameters. NOTE: For security, POST with JSON body is recommended.")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "201", description = "User registered successfully"),
                        @ApiResponse(responseCode = "400", description = "Bad request")
        })
        public ResponseEntity<?> signupGet(@RequestParam String username, @RequestParam String password) {
                LoginRequest signupRequest = new LoginRequest();
                signupRequest.setUsername(username);
                signupRequest.setPassword(password);
                return signup(signupRequest);
        }
}
