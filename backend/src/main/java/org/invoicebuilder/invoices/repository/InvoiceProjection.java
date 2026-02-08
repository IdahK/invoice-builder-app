package org.invoicebuilder.invoices.repository;

import org.invoicebuilder.invoices.domain.InvoiceStatus;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public interface InvoiceProjection {
    UUID getId();
    String getInvoiceNumber();
    String getCustomerName();
    String getCurrency();
    BigDecimal getTotalAmount();
    InvoiceStatus getStatus();
    LocalDate getIssueDate();
}
