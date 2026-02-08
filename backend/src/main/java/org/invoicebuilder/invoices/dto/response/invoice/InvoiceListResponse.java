package org.invoicebuilder.invoices.dto.response.invoice;

import org.invoicebuilder.invoices.domain.Invoice;
import org.invoicebuilder.invoices.domain.InvoiceStatus;
import org.invoicebuilder.invoices.repository.InvoiceProjection;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public record InvoiceListResponse(
        UUID id,
        String invoice_number,
        String customer_name,
        String currency,
        BigDecimal total_amount,
        InvoiceStatus status,
        LocalDate issue_date
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
    
    public static InvoiceListResponse from(InvoiceProjection projection){
        return new InvoiceListResponse(
                projection.getId(),
                projection.getInvoiceNumber(),
                projection.getCustomerName(),
                projection.getCurrency(),
                projection.getTotalAmount(),
                projection.getStatus(),
                projection.getIssueDate()
        );
    }
}
