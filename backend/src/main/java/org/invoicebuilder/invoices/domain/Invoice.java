package org.invoicebuilder.invoices.domain;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "invoices")
@Data
public class Invoice {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    @Column(name = "invoice_number", unique = true, nullable = false)
    private String invoiceNumber;

    @Column(name = "invoice_currency", nullable = false, length = 3)
    private String currency;

    @Enumerated(EnumType.STRING)
    @Column(name = "invoice_status")
    private InvoiceStatus status;

    @OneToMany(mappedBy = "invoice", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<InvoiceLineItem> lineItems;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "invoice_customer_id", nullable = false)
    private Customer customer;

    @ManyToOne( fetch = FetchType.LAZY)
    @JoinColumn(name = "invoice_sender_id")
    private Sender sender;

    @Column(name = "invoice_issue_date")
    private LocalDate issueDate;

    @Column(name = "invoice_due_date")
    private LocalDate dueDate;

    @Column(name = "invoice_tax_rate", nullable = false)
    private BigDecimal taxRate;

    @Column(name = "invoice_discount", nullable = false)
    private BigDecimal discount;

    @Column(name = "invoice_notes", length = 1000)
    private String notes;

    @Column(name = "invoice_subtotal")
    private BigDecimal subtotal;

    @Column(name = "invoice_tax_amount")
    private BigDecimal taxAmount;

    @Column(name = "invoice_total_amount")
    private BigDecimal totalAmount;

}
