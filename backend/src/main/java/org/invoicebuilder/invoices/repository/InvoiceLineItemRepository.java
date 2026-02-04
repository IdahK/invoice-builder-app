package org.invoicebuilder.invoices.repository;

import org.invoicebuilder.invoices.domain.InvoiceLineItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface InvoiceLineItemRepository extends JpaRepository<InvoiceLineItem, UUID> {
}
