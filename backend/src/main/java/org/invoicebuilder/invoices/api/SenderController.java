package org.invoicebuilder.invoices.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.invoicebuilder.api.BaseApiController;
import org.invoicebuilder.invoices.dto.request.sender.CreateSenderRequest;
import org.invoicebuilder.invoices.dto.response.sender.SenderSummaryResponse;
import org.invoicebuilder.invoices.service.SenderService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.UUID;

@Tag(name = "Sender Management", description = "APIs for managing senders (invoice issuers)")
@RestController
@RequestMapping("/api/v1/senders")
@RequiredArgsConstructor
public class SenderController extends BaseApiController {
    private final SenderService senderService;

    @Operation(summary = "Create a new sender", description = "Creates a new sender with the provided details")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Sender created successfully",
                    content = @Content(schema = @Schema(implementation = SenderSummaryResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input data",
                    content = @Content(schema = @Schema(implementation = org.invoicebuilder.exception.ApiError.class)))
    })
    @PostMapping
    public ResponseEntity<SenderSummaryResponse> createSender(
            @Parameter(description = "Sender details to create", required = true)
            @Valid @RequestBody CreateSenderRequest request) {
        SenderSummaryResponse response = senderService.create(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @Operation(summary = "List all senders", description = "Retrieves a paginated list of all senders")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Senders retrieved successfully",
                    content = @Content(schema = @Schema(implementation = Page.class)))
    })
    @GetMapping
    public ResponseEntity<Page<SenderSummaryResponse>> listSenders(
            @Parameter(description = "Page number (0-based)", example = "0")
            @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Number of items per page", example = "10")
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<SenderSummaryResponse> senders = senderService.list(pageable);
        return ResponseEntity.ok(senders);
    }

    @Operation(summary = "Get sender by ID", description = "Retrieves a specific sender by their unique identifier")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Sender found",
                    content = @Content(schema = @Schema(implementation = SenderSummaryResponse.class))),
            @ApiResponse(responseCode = "404", description = "Sender not found",
                    content = @Content(schema = @Schema(implementation = org.invoicebuilder.exception.ApiError.class)))
    })
    @GetMapping("/{id}")
    public ResponseEntity<SenderSummaryResponse> getSender(
            @Parameter(description = "Sender unique identifier", example = "550e8400-e29b-41d4-a716-446655440000")
            @PathVariable UUID id) {
        SenderSummaryResponse response = senderService.getById(id);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Update sender", description = "Updates an existing sender's details")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Sender updated successfully",
                    content = @Content(schema = @Schema(implementation = SenderSummaryResponse.class))),
            @ApiResponse(responseCode = "404", description = "Sender not found",
                    content = @Content(schema = @Schema(implementation = org.invoicebuilder.exception.ApiError.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input data",
                    content = @Content(schema = @Schema(implementation = org.invoicebuilder.exception.ApiError.class)))
    })
    @PutMapping("/{id}")
    public ResponseEntity<SenderSummaryResponse> updateSender(
            @Parameter(description = "Sender unique identifier", example = "550e8400-e29b-41d4-a716-446655440000")
            @PathVariable UUID id,
            @Parameter(description = "Updated sender details", required = true)
            @Valid @RequestBody CreateSenderRequest request) {
        SenderSummaryResponse response = senderService.update(id, request);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Delete sender", description = "Deletes a sender by their unique identifier")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Sender deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Sender not found",
                    content = @Content(schema = @Schema(implementation = org.invoicebuilder.exception.ApiError.class)))
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSender(
            @Parameter(description = "Sender unique identifier", example = "550e8400-e29b-41d4-a716-446655440000")
            @PathVariable UUID id) {
        senderService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
