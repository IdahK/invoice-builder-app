package org.invoicebuilder.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.invoicebuilder.util.ApiVersionUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;

import static org.invoicebuilder.util.ApiVersionUtils.SUPPORTED_VERSIONS;
import static org.invoicebuilder.util.ApiVersionUtils.*;

/**
 * Base controller that provides common API versioning functionality
 */
public abstract class BaseApiController {

    @Operation(summary = "Get API version information", description = "Returns information about the current API version")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Version information retrieved successfully",
                    content = @Content(schema = @Schema(implementation = VersionInfo.class)))
    })
    @GetMapping("/version")
    public ResponseEntity<VersionInfo> getVersion() {
        String currentVersion = ApiVersionUtils.getCurrentVersion();
        return ResponseEntity.ok(new VersionInfo(
                currentVersion,
                SUPPORTED_VERSIONS,
                DEFAULT_VERSION.equals(currentVersion),
                "Current API version information"
        ));
    }

    /**
     * Response DTO for version information
     */
    public record VersionInfo(
            @Schema(description = "Current API version", example = "v1")
            String currentVersion,
            
            @Schema(description = "List of supported versions")
            java.util.List<String> supportedVersions,
            
            @Schema(description = "Whether this is the default version")
            boolean isDefault,
            
            @Schema(description = "Version description")
            String description
    ) {}
}
