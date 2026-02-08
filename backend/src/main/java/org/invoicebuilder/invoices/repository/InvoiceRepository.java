package org.invoicebuilder.invoices.repository;

import org.invoicebuilder.invoices.domain.Invoice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface InvoiceRepository extends JpaRepository<Invoice, UUID> {
    
    @EntityGraph(attributePaths = {"customer"})
    Page<Invoice> findAll(Pageable pageable);
    
    @Query("""
        SELECT i.id as id,
               i.invoiceNumber as invoiceNumber,
               c.name as customerName,
               i.currency as currency,
               i.totalAmount as totalAmount,
               i.status as status,
               i.issueDate as issueDate
        FROM Invoice i
        JOIN i.customer c
        """)
    Page<InvoiceProjection> findInvoiceList(Pageable pageable);
    
    long count();
}
