package org.invoicebuilder.invoices.dto.request.customer;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.invoicebuilder.invoices.domain.Customer;

@Schema(description = "Request object for creating a new customer")
public record CreateCustomerRequest(
        @Schema(description = "Customer's full name", example = "John Doe", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotBlank(message = "Customer name is required")
        String name,
        
        @Schema(description = "Customer's email address", example = "john.doe@example.com", requiredMode = Schema.RequiredMode.REQUIRED)
        @Email(message = "Email should be valid")
        @NotBlank(message = "Customer email is required")
        String email,
        
        @Schema(description = "Customer's phone number", example = "+1234567890", name = "phone_number")
        @JsonProperty("phone_number")
        String phone,
        
        @Schema(description = "Customer's residential address", example = "123 Main St, City, State")
        String address,
        
        @Schema(description = "Customer's country of residence", example = "United States")
        String country
) {
    public static Customer fromRequest(CreateCustomerRequest customerRequest){
        return Customer.builder()
                .name(customerRequest.name)
                .email(customerRequest.email)
                .phoneNumber(customerRequest.phone)
                .address(customerRequest.address)
                .country(customerRequest.country)
                .createdAt(java.time.Instant.now())
                .build();
    }

}
