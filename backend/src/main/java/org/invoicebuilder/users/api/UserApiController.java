package org.invoicebuilder.users.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.invoicebuilder.users.dto.UserRegisterRequest;
import org.invoicebuilder.users.dto.UserRegistrationResponse;
import org.invoicebuilder.users.service.registration.RegistrationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "User Management", description = "Api User Management endpoints")
public class UserApiController {

    private final RegistrationService registrationService;

    @GetMapping("/hello")
    public String hello() {
        return "Hello there!!";
    }

    @PostMapping("/register")
    @Operation(
        summary = "Register a new user",
        description = "Creates a new user account with the specified registration strategy. " +
                     "Default strategy is STANDARD for email/password registration. " +
                     "Additional strategies like OAUTH can be specified for different registration flows.",
        parameters = {
            @Parameter(
                name = "strategy",
                description = "Registration strategy to use (STANDARD, OAUTH, etc.)",
                in = ParameterIn.QUERY,
                required = false,
                example = "STANDARD",
                schema = @io.swagger.v3.oas.annotations.media.Schema(
                    allowableValues = {"STANDARD", "OAUTH"},
                        defaultValue = "STANDARD"
                )
            )
        },
        requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "User registration information",
            required = true,
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = UserRegisterRequest.class)
            )
        ),
        responses = {
            @ApiResponse(
                responseCode = "201",
                description = "User registered successfully",
                content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = UserRegistrationResponse.class)
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
    public ResponseEntity<UserRegistrationResponse> registerUser(
            @Valid @RequestBody UserRegisterRequest registerRequest,
            @RequestParam(defaultValue = "STANDARD") String strategy) {
        log.info("Registration request for email: {} and account: {} with strategy: {}", 
                registerRequest.email(), registerRequest.accountName(), strategy);
        
        try {
            UserRegistrationResponse response = registrationService.registerWithStrategy(registerRequest, strategy);
            log.info("User registered successfully: {}", response.email());
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (IllegalArgumentException e) {
            log.error("Registration failed: {}", e.getMessage());
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            log.error("Unexpected error during registration", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
