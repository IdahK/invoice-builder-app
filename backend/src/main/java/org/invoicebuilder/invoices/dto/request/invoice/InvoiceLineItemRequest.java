package org.invoicebuilder.invoices.dto.request.invoice;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import org.invoicebuilder.invoices.domain.InvoiceLineItem;

public record InvoiceLineItemRequest(
        @NotNull
        String description,

        @Positive
        @Min(1)
        int quantity,

        @DecimalMin("0.0")
        BigDecimal unitPrice
) {
    public static InvoiceLineItem fromLineItemRequest(InvoiceLineItemRequest request) {
        InvoiceLineItem lineItem = new InvoiceLineItem();
        lineItem.setDescription(request.description);
        lineItem.setQuantity(request.quantity);
        lineItem.setUnitPrice(request.unitPrice);
        lineItem.setLineTotal(request.unitPrice.multiply(BigDecimal.valueOf(request.quantity)));
        return lineItem;
    }
}
