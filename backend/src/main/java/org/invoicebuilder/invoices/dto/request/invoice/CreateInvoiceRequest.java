package org.invoicebuilder.invoices.dto.request.invoice;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import org.invoicebuilder.common.validation.DateRange;
import org.invoicebuilder.common.validation.ISOCurrency;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import org.invoicebuilder.invoices.domain.Invoice;
import org.invoicebuilder.invoices.domain.InvoiceStatus;


@Schema(description = "Request object for creating a new invoice with line items")
@DateRange(issueDateField = "issueDate", dueDateField = "dueDate", message = "Issue date must be before or equal to due date")
public record CreateInvoiceRequest(
        @Schema(description = "Unique identifier of the customer", example = "550e8400-e29b-41d4-a716-446655440000", requiredMode = Schema.RequiredMode.REQUIRED, name = "customer_id")
        @NotNull(message = "Customer ID is required")
        @JsonProperty("customer_id")
        UUID customerId,

        @Schema(description = "Unique identifier of the sender (optional)", example = "550e8400-e29b-41d4-a716-446655440001", name = "sender_id")
        @NotNull(message = "Sender ID is required")
        @JsonProperty("sender_id")
        UUID senderId,

        @Schema(description = "Date when the invoice was issued", example = "2024-01-15", requiredMode = Schema.RequiredMode.REQUIRED, name = "issue_date")
        @NotNull(message = "Issue date is required")
        @PastOrPresent(message = "Issue date cannot be in the future")
        @JsonProperty("issue_date")
        LocalDate issueDate,

        @Schema(description = "Date when the invoice payment is due", example = "2024-02-15", requiredMode = Schema.RequiredMode.REQUIRED, name = "due_date")
        @NotNull(message = "Due date is required")
        @FutureOrPresent(message = "Due date cannot be in the past")
        @JsonProperty("due_date")
        LocalDate dueDate,

        @Schema(description = "Currency code in ISO-4217 format (3 uppercase letters)", example = "USD", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotBlank(message = "Currency is required")
        @ISOCurrency(message = "Invalid currency code. Valid examples: USD, EUR, GBP, KES, JPY")
        String currency,

        @Schema(description = "Tax rate as a percentage", example = "10.00", requiredMode = Schema.RequiredMode.REQUIRED, name = "tax_rate")
        @Positive(message = "Tax rate must be positive")
        @DecimalMin(value = "0.0", message = "Tax rate cannot be negative")
        @JsonProperty("tax_rate")
        BigDecimal taxRate,

        @Schema(description = "Discount amount", example = "50.00", requiredMode = Schema.RequiredMode.REQUIRED)
        @DecimalMin(value = "0.0", message = "Discount cannot be negative")
        BigDecimal discount,

        @Schema(description = "Additional notes or comments for the invoice", example = "Payment due within 30 days")
        @Size(max = 1000, message = "Notes cannot exceed 1000 characters")
        String notes,

        @Schema(description = "List of line items included in the invoice", requiredMode = Schema.RequiredMode.REQUIRED, name = "line_items")
        @NotEmpty(message = "At least one line item is required")
        @JsonProperty("line_items")
        List<@Valid InvoiceLineItemRequest> lineItems
) {
    public static Invoice fromRequest(CreateInvoiceRequest request, String invoiceNumber) {
        Invoice invoice = new Invoice();
        invoice.setInvoiceNumber(invoiceNumber);
        invoice.setCurrency(request.currency);
        invoice.setStatus(InvoiceStatus.DRAFT);
        invoice.setIssueDate(request.issueDate);
        invoice.setDueDate(request.dueDate);
        invoice.setTaxRate(request.taxRate);
        invoice.setDiscount(request.discount);
        invoice.setNotes(request.notes);
        return invoice;
    }
}
