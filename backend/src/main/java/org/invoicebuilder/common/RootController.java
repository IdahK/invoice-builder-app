package org.invoicebuilder.common;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@Tag(name = "Root", description = "Root endpoint")
@RestController
public class RootController {

    @GetMapping("/")
    public ResponseEntity<String> welcome(){
        return ResponseEntity.ok("Welcome");
    }

    @GetMapping("/csrf-token")
    public ResponseEntity<CsrfToken> getCsrfToken(HttpServletRequest servletRequest){
        return ResponseEntity.ok((CsrfToken) servletRequest.getAttribute("_csrf"));
    }

    @Operation(summary = "Root endpoint", description = "Returns welcome message and API information")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Welcome message retrieved successfully")
    })
    @GetMapping("/welcome-api-versions")
    public ResponseEntity<Map<String, Object>> getRoot() {
        Map<String, Object> response = Map.of(
                "message", "Welcome to Invoice Builder API",
                "status", "running",
                "version", "v1.0.0",
                "endpoints", Map.of(
                        "api_versions", "/api/versions",
                        "api_health", "/api/health",
                        "api_docs", "/api/v1/docs",
                        "actuator", "/actuator",
                        "actuator_health", "/actuator/health"
                ),
                "documentation", "http://localhost:8080/api/v1/docs"
        );
        return ResponseEntity.ok(response);
    }
}
