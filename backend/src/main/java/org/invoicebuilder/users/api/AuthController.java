package org.invoicebuilder.users.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.invoicebuilder.users.dto.auth.*;
import org.invoicebuilder.users.service.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
@Slf4j
@Tag(name = "Authentication", description = "Authentication endpoints")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    @Operation(
        summary = "Register a new user",
        requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "User registration information",
            required = true,
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = RegisterRequest.class)
            )
        ),
        responses = {
            @ApiResponse(
                responseCode = "201",
                description = "User registered successfully",
                content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = RegistrationResponse.class)
                )
            ),
            @ApiResponse(
                responseCode = "400",
                description = "Invalid registration data or strategy"
            ),
            @ApiResponse(
                responseCode = "500",
                description = "Internal server error during registration"
            )
        }
    )
    public ResponseEntity<RegistrationResponse> registerUser(
            @Valid @RequestBody RegisterRequest registerRequest) {
        log.info("Registration request for email: {} and account: {}",
                registerRequest.email(), registerRequest.accountName());

        RegistrationResponse response = authService.registerEmailUser(registerRequest);
        log.info("User registered successfully: {}", response.email());
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/login")
    @Operation(
        summary = "Authenticate user and return JWT token",
        description = "Authenticates a user with email and password credentials and returns a JWT access token for API access",
        requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "User login credentials",
            required = true,
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = LoginRequest.class)
            )
        ),
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "Authentication successful",
                content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = AuthResponse.class)
                )
            ),
            @ApiResponse(
                responseCode = "401",
                description = "Invalid credentials - email or password is incorrect"
            ),
            @ApiResponse(
                responseCode = "400",
                description = "Invalid request format or validation errors"
            ),
            @ApiResponse(
                responseCode = "500",
                description = "Internal server error during authentication"
            )
        }
    )
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest loginRequest){
        AuthResponse authResponse = authService.login(loginRequest);
        return ResponseEntity.ok(authResponse);
    }

    @PostMapping("/change-password")
    public ResponseEntity<PasswordChangeResponse> changePassword(@Valid @RequestBody PasswordChangeRequest passwordChangeRequest){
        return null;
    }

}
