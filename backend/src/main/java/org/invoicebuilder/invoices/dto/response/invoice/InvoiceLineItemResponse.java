package org.invoicebuilder.invoices.dto.response.invoice;

import org.invoicebuilder.invoices.domain.InvoiceLineItem;

import java.math.BigDecimal;
import java.util.UUID;

public record InvoiceLineItemResponse(
        UUID id,
        String description,
        int quantity,
        BigDecimal unitPrice,
        BigDecimal lineTotal
) {
    public static InvoiceLineItemResponse from(InvoiceLineItem item) {
        return new InvoiceLineItemResponse(
                item.getId(),
                item.getDescription(),
                item.getQuantity(),
                item.getUnitPrice(),
                item.getLineTotal()
        );
    }

}
