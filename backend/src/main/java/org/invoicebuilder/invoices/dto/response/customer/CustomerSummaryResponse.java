package org.invoicebuilder.invoices.dto.response.customer;

import org.invoicebuilder.invoices.domain.Customer;

import java.util.UUID;

public record CustomerSummaryResponse(
        UUID id,
        String name,
        String email,
        String phone_number,
        String address,
        String country
) {
    public static CustomerSummaryResponse from(Customer customer){
        return new CustomerSummaryResponse(
                customer.getId(),
                customer.getName(),
                customer.getEmail(),
                customer.getPhoneNumber(),
                customer.getAddress(),
                customer.getCountry()
        );
    }
}
