package org.invoicebuilder.invoices.dto.request.customer;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.invoicebuilder.invoices.domain.Customer;

public record CreateCustomerRequest(
        @NotBlank String name,
        @Email String email,
        String phone,
        String address,
        String country
) {
    public static Customer fromRequest(CreateCustomerRequest customerRequest){
        return Customer.builder()
                .name(customerRequest.name)
                .email(customerRequest.email)
                .phoneNumber(customerRequest.phone)
                .address(customerRequest.address)
                .country(customerRequest.country)
                .build();
    }

}
