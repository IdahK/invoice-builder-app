package org.invoicebuilder.invoices.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@Tag(name = "API Information", description = "API versioning and discovery endpoints")
@RestController
@RequestMapping("/api")
public class ApiInfoController {

    @Operation(summary = "Get supported API versions", description = "Returns information about supported API versions and their documentation")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "API versions retrieved successfully",
                    content = @Content(schema = @Schema(implementation = ApiVersionsResponse.class)))
    })
    @GetMapping("/versions")
    public ResponseEntity<ApiVersionsResponse> getSupportedVersions() {
        ApiVersionsResponse response = new ApiVersionsResponse(
                List.of("v1"),
                "v1",
                Map.of(
                        "v1", "/api/v1/docs",
                        "v2", "/api/v2/docs"
                ),
                Map.of(
                        "v1", "Current stable version",
                        "v2", "Next version (in development)"
                )
        );
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Get API health status", description = "Returns the health status of the API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "API is healthy",
                    content = @Content(schema = @Schema(implementation = ApiHealthResponse.class)))
    })
    @GetMapping("/health")
    public ResponseEntity<ApiHealthResponse> getHealth() {
        ApiHealthResponse response = new ApiHealthResponse(
                "UP",
                "Invoice Builder API is running",
                "v1.0.0"
        );
        return ResponseEntity.ok(response);
    }

    // Response DTOs
    public record ApiVersionsResponse(
            @Schema(description = "List of supported API versions")
            List<String> supportedVersions,
            
            @Schema(description = "Default API version")
            String defaultVersion,
            
            @Schema(description = "Documentation URLs for each version")
            Map<String, String> documentationUrls,
            
            @Schema(description = "Version descriptions")
            Map<String, String> descriptions
    ) {}

    public record ApiHealthResponse(
            @Schema(description = "Health status", example = "UP")
            String status,
            
            @Schema(description = "Health message", example = "Invoice Builder API is running")
            String message,
            
            @Schema(description = "API version", example = "v1.0.0")
            String version
    ) {}
}
