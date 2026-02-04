package org.invoicebuilder.invoices.dto.request.invoice;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import org.invoicebuilder.invoices.domain.Invoice;


public record CreateInvoiceRequest(
        @NotNull
        UUID customerId,

        @NotNull
        UUID senderId,

        @NotNull
        @PastOrPresent
        LocalDate issueDate,

        @NotNull
        @FutureOrPresent
        LocalDate dueDate,

        @NotBlank
        @Pattern(regexp = "^[A-Z]{3}$")
        String currency, //ISO-4217 CODES from a dropdown

        @Positive
        @DecimalMin("0.0")
        BigDecimal taxRate,

        @Positive
        @DecimalMin("0.0")
        BigDecimal discount,

        @Size(max = 1000)
        String notes,

        @NotEmpty
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
