package org.invoicebuilder.invoices.dto.response.invoice;

import org.invoicebuilder.invoices.domain.Invoice;
import org.invoicebuilder.invoices.domain.InvoiceStatus;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public record InvoiceListResponse(
        UUID id,
        String invoiceNumber,
        String customerName,
        String currency,
        BigDecimal totalAmount,
        InvoiceStatus status,
        LocalDate issueDate
) {
    public static InvoiceListResponse from(Invoice invoice){
        return new InvoiceListResponse(
                invoice.getId(),
                invoice.getInvoiceNumber(),
                invoice.getCustomer().getName(),
                invoice.getCurrency(),
                invoice.getTotalAmount(),
                invoice.getStatus(),
                invoice.getIssueDate()
        );
    }
}
