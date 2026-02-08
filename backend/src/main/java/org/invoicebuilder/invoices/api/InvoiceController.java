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
import org.invoicebuilder.invoices.dto.request.invoice.CreateInvoiceRequest;
import org.invoicebuilder.invoices.dto.response.invoice.InvoiceListResponse;
import org.invoicebuilder.invoices.dto.response.invoice.InvoiceLineItemResponse;
import org.invoicebuilder.invoices.service.InvoiceService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;
import java.util.UUID;

@Tag(name = "Invoice Management", description = "APIs for managing invoices and their line items")
@RestController
@RequestMapping("/api/v1/invoices")
@RequiredArgsConstructor
public class InvoiceController extends BaseApiController {
    private final InvoiceService invoiceService;

    @Operation(summary = "Create a new invoice", description = "Creates a new invoice with line items and calculates totals")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Invoice created successfully",
                    content = @Content(schema = @Schema(implementation = InvoiceListResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input data",
                    content = @Content(schema = @Schema(implementation = org.invoicebuilder.exception.ApiError.class))),
            @ApiResponse(responseCode = "404", description = "Customer not found",
                    content = @Content(schema = @Schema(implementation = org.invoicebuilder.exception.ApiError.class)))
    })
    @PostMapping
    public ResponseEntity<InvoiceListResponse> createInvoice(
            @Parameter(description = "Invoice details with line items", required = true)
            @Valid @RequestBody CreateInvoiceRequest request) {
        InvoiceListResponse response = invoiceService.create(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @Operation(summary = "List all invoices", description = "Retrieves a paginated list of all invoices")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Invoices retrieved successfully",
                    content = @Content(schema = @Schema(implementation = Page.class)))
    })
    @GetMapping
    public ResponseEntity<Page<InvoiceListResponse>> listInvoices(
            @Parameter(description = "Page number (0-based)", example = "0")
            @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Number of items per page", example = "10")
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<InvoiceListResponse> invoices = invoiceService.list(pageable);
        return ResponseEntity.ok(invoices);
    }

    @Operation(summary = "Get invoice by ID", description = "Retrieves a specific invoice by their unique identifier")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Invoice found",
                    content = @Content(schema = @Schema(implementation = InvoiceListResponse.class))),
            @ApiResponse(responseCode = "404", description = "Invoice not found",
                    content = @Content(schema = @Schema(implementation = org.invoicebuilder.exception.ApiError.class)))
    })
    @GetMapping("/{id}")
    public ResponseEntity<InvoiceListResponse> getInvoice(
            @Parameter(description = "Invoice unique identifier", example = "550e8400-e29b-41d4-a716-446655440000")
            @PathVariable UUID id) {
        InvoiceListResponse response = invoiceService.getById(id);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Update invoice", description = "Updates an existing invoice and its line items")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Invoice updated successfully",
                    content = @Content(schema = @Schema(implementation = InvoiceListResponse.class))),
            @ApiResponse(responseCode = "404", description = "Invoice not found",
                    content = @Content(schema = @Schema(implementation = org.invoicebuilder.exception.ApiError.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input data",
                    content = @Content(schema = @Schema(implementation = org.invoicebuilder.exception.ApiError.class)))
    })
    @PutMapping("/{id}")
    public ResponseEntity<InvoiceListResponse> updateInvoice(
            @Parameter(description = "Invoice unique identifier", example = "550e8400-e29b-41d4-a716-446655440000")
            @PathVariable UUID id,
            @Parameter(description = "Updated invoice details with line items", required = true)
            @Valid @RequestBody CreateInvoiceRequest request) {
        InvoiceListResponse response = invoiceService.update(id, request);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Delete invoice", description = "Deletes an invoice by their unique identifier")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Invoice deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Invoice not found",
                    content = @Content(schema = @Schema(implementation = org.invoicebuilder.exception.ApiError.class)))
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteInvoice(
            @Parameter(description = "Invoice unique identifier", example = "550e8400-e29b-41d4-a716-446655440000")
            @PathVariable UUID id) {
        invoiceService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Get invoice line items", description = "Retrieves all line items for a specific invoice")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Line items retrieved successfully",
                    content = @Content(schema = @Schema(implementation = List.class))),
            @ApiResponse(responseCode = "404", description = "Invoice not found",
                    content = @Content(schema = @Schema(implementation = org.invoicebuilder.exception.ApiError.class)))
    })
    @GetMapping("/{id}/line-items")
    public ResponseEntity<List<InvoiceLineItemResponse>> getInvoiceLineItems(
            @Parameter(description = "Invoice unique identifier", example = "550e8400-e29b-41d4-a716-446655440000")
            @PathVariable UUID id) {
        List<InvoiceLineItemResponse> lineItems = invoiceService.getInvoiceLineItems(id);
        return ResponseEntity.ok(lineItems);
    }
}
