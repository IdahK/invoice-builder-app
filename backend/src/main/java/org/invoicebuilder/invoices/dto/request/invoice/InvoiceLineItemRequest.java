package org.invoicebuilder.invoices.dto.request.invoice;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import org.invoicebuilder.invoices.domain.InvoiceLineItem;

@Schema(description = "Request object for creating an invoice line item")
public record InvoiceLineItemRequest(
        @Schema(description = "Description of the line item or service", example = "Web Development Services", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotNull(message = "Line item description is required")
        String description,

        @Schema(description = "Quantity of the line item", example = "10", requiredMode = Schema.RequiredMode.REQUIRED)
        @Positive(message = "Quantity must be positive")
        @Min(value = 1, message = "Quantity must be at least 1")
        int quantity,

        @Schema(description = "Unit price per item", example = "100.50", requiredMode = Schema.RequiredMode.REQUIRED, name = "unit_price")
        @DecimalMin(value = "0.0", message = "Unit price cannot be negative")
        @JsonProperty("unit_price")
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
