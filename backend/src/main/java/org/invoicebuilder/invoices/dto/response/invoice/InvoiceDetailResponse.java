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
        String invoice_number,
        InvoiceStatus status,

        LocalDate issue_date,
        LocalDate due_date,

        String currency,

        BigDecimal subtotal,
        BigDecimal tax_rate,
        BigDecimal tax_amount,
        BigDecimal discount,
        BigDecimal discount_amount,
        BigDecimal total_amount,

        String notes,

        CustomerSummaryResponse customer,
        SenderSummaryResponse sender,

        List<InvoiceLineItemResponse> line_items
) {
}
