package org.invoicebuilder.invoices.dto.response.sender;

import io.swagger.v3.oas.annotations.media.Schema;
import org.invoicebuilder.invoices.domain.Sender;

import java.util.UUID;

@Schema(description = "Response object for sender information")
public record SenderSummaryResponse(
        @Schema(description = "Sender unique identifier", example = "550e8400-e29b-41d4-a716-446655440000")
        UUID id,
        
        @Schema(description = "Sender's full name", example = "ABC Company")
        String name,
        
        @Schema(description = "Sender's email address", example = "billing@abc-company.com")
        String email,
        
        @Schema(description = "Sender's phone number", example = "+1234567890")
        String phone_number,
        
        @Schema(description = "Sender's business address", example = "456 Business Ave, Suite 100, City, State")
        String address
) {
    public static SenderSummaryResponse from(Sender sender) {
        return new SenderSummaryResponse(
                sender.getId(),
                sender.getName(),
                sender.getEmail(),
                sender.getPhoneNumber(),
                sender.getAddress()
        );
    }
}
