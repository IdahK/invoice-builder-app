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
import org.invoicebuilder.invoices.dto.request.customer.CreateCustomerRequest;
import org.invoicebuilder.invoices.dto.response.customer.CustomerSummaryResponse;
import org.invoicebuilder.invoices.service.CustomerService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

import java.util.UUID;

@Tag(name = "Customer Management", description = "APIs for managing customers")
@RestController
@RequestMapping("/api/v1/customers")
@RequiredArgsConstructor
public class CustomerController extends BaseApiController {
    private final CustomerService customerService;

    @Operation(summary = "Create a new customer", description = "Creates a new customer with the provided details")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Customer created successfully",
                    content = @Content(schema = @Schema(implementation = CustomerSummaryResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input data",
                    content = @Content(schema = @Schema(implementation = org.invoicebuilder.exception.ApiError.class)))
    })
    @PostMapping
    public ResponseEntity<CustomerSummaryResponse> createCustomer(
            @Parameter(description = "Customer details to create", required = true)
            @Valid @RequestBody CreateCustomerRequest request) {
        CustomerSummaryResponse response = customerService.create(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @Operation(summary = "List all customers", description = "Retrieves a paginated list of all customers")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Customers retrieved successfully",
                    content = @Content(schema = @Schema(implementation = Page.class)))
    })
    @GetMapping
    public ResponseEntity<Page<CustomerSummaryResponse>> listCustomers(
            @Parameter(description = "Page number (0-based)", example = "0")
            @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Number of items per page", example = "10")
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<CustomerSummaryResponse> customers = customerService.list(pageable);
        return ResponseEntity.ok(customers);
    }

    @Operation(summary = "Get customer by ID", description = "Retrieves a specific customer by their unique identifier")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Customer found",
                    content = @Content(schema = @Schema(implementation = CustomerSummaryResponse.class))),
            @ApiResponse(responseCode = "404", description = "Customer not found",
                    content = @Content(schema = @Schema(implementation = org.invoicebuilder.exception.ApiError.class)))
    })
    @GetMapping("/{id}")
    public ResponseEntity<CustomerSummaryResponse> getCustomer(
            @Parameter(description = "Customer unique identifier", example = "550e8400-e29b-41d4-a716-446655440000")
            @PathVariable UUID id) {
        CustomerSummaryResponse response = customerService.getById(id);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Update customer", description = "Updates an existing customer's details")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Customer updated successfully",
                    content = @Content(schema = @Schema(implementation = CustomerSummaryResponse.class))),
            @ApiResponse(responseCode = "404", description = "Customer not found",
                    content = @Content(schema = @Schema(implementation = org.invoicebuilder.exception.ApiError.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input data",
                    content = @Content(schema = @Schema(implementation = org.invoicebuilder.exception.ApiError.class)))
    })
    @PutMapping("/{id}")
    public ResponseEntity<CustomerSummaryResponse> updateCustomer(
            @Parameter(description = "Customer unique identifier", example = "550e8400-e29b-41d4-a716-446655440000")
            @PathVariable UUID id,
            @Parameter(description = "Updated customer details", required = true)
            @Valid @RequestBody CreateCustomerRequest request) {
        CustomerSummaryResponse response = customerService.update(id, request);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Delete customer", description = "Deletes a customer by their unique identifier")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Customer deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Customer not found",
                    content = @Content(schema = @Schema(implementation = org.invoicebuilder.exception.ApiError.class)))
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCustomer(
            @Parameter(description = "Customer unique identifier", example = "550e8400-e29b-41d4-a716-446655440000")
            @PathVariable UUID id) {
        customerService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
