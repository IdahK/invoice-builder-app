package org.invoicebuilder.invoices.dto.response.invoice;

import org.invoicebuilder.invoices.dto.response.customer.CustomerSummaryResponse;
import org.invoicebuilder.invoices.dto.response.sender.SenderSummaryResponse;
import org.invoicebuilder.invoices.domain.InvoiceStatus;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;


public record InvoiceDetailResponse(
        UUID id,
        String invoiceNumber,
        InvoiceStatus status,

        LocalDate issueDate,
        LocalDate dueDate,

        String currency,

        BigDecimal subtotal,
        BigDecimal taxRate,
        BigDecimal taxAmount,
        BigDecimal discount,
        BigDecimal discountAmount,
        BigDecimal totalAmount,

        String notes,

        CustomerSummaryResponse customer,
        SenderSummaryResponse sender,

        List<InvoiceLineItemResponse> lineItems
) {
}
