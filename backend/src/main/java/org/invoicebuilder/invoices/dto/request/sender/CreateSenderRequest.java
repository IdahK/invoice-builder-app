package org.invoicebuilder.invoices.dto.request.sender;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.invoicebuilder.invoices.domain.Sender;

@Schema(description = "Request object for creating a new sender")
public record CreateSenderRequest(
        @Schema(description = "Sender's full name", example = "ABC Company", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotBlank(message = "Sender name is required")
        String name,
        
        @Schema(description = "Sender's email address", example = "billing@abc-company.com", requiredMode = Schema.RequiredMode.REQUIRED)
        @Email(message = "Email should be valid")
        @NotBlank(message = "Sender email is required")
        String email,
        
        @Schema(description = "Sender's phone number", example = "+1234567890", name = "phone_number")
        @JsonProperty("phone_number")
        String phoneNumber,
        
        @Schema(description = "Sender's business address", example = "456 Business Ave, Suite 100, City, State")
        String address
) {
    public static Sender fromRequest(CreateSenderRequest senderRequest) {
        Sender sender = new Sender();
        sender.setName(senderRequest.name());
        sender.setEmail(senderRequest.email());
        sender.setPhoneNumber(senderRequest.phoneNumber());
        sender.setAddress(senderRequest.address());
        return sender;
    }
}
