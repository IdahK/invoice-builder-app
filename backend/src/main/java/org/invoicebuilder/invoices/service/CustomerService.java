package org.invoicebuilder.invoices.service;

import lombok.RequiredArgsConstructor;
import org.invoicebuilder.invoices.domain.Customer;
import org.invoicebuilder.invoices.dto.request.customer.CreateCustomerRequest;
import org.invoicebuilder.invoices.dto.response.customer.CustomerSummaryResponse;
import org.invoicebuilder.invoices.repository.CustomerRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomerService implements BaseService<CustomerSummaryResponse, CreateCustomerRequest> {
    private final CustomerRepository customerRepository;

    @Override
    public CustomerSummaryResponse create(CreateCustomerRequest customerRequest){
        Customer customer = CreateCustomerRequest.fromRequest(customerRequest);
        customerRepository.save(customer);
        return CustomerSummaryResponse.from(customer);
    }

    @Override
    public Page<CustomerSummaryResponse> list(Pageable pageable){
        pageable = pageable.getPageSize() == 0 ? Pageable.ofSize(10) : pageable;
        return customerRepository.findAll(pageable).map(CustomerSummaryResponse::from);
    }


}
