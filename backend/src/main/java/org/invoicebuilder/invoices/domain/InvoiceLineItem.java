package org.invoicebuilder.invoices.domain;

import jakarta.persistence.*;
import lombok.Data;

import javax.naming.Name;
import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "invoice_line_items")
@Data
public class InvoiceLineItem {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "line_item_invoice_id")
    private Invoice invoice;

    @Column(name = "line_item_description")
    private String description;

    @Column(name = "line_item_quantity")
    private int quantity;

    @Column(name = "line_item_unit_price")
    private BigDecimal unitPrice;

    @Column(name = "line_item_total")
    private BigDecimal lineTotal;
}
