package org.invoicebuilder.invoices.service;

import lombok.RequiredArgsConstructor;
import org.invoicebuilder.exception.ResourceNotFoundException;
import org.invoicebuilder.invoices.domain.Customer;
import org.invoicebuilder.invoices.dto.request.customer.CreateCustomerRequest;
import org.invoicebuilder.invoices.dto.response.customer.CustomerSummaryResponse;
import org.invoicebuilder.invoices.repository.CustomerRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.UUID;

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

    @Override
    public CustomerSummaryResponse getById(UUID id){
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Customer", "id", id));
        return CustomerSummaryResponse.from(customer);
    }

    @Override
    public CustomerSummaryResponse update(UUID id, CreateCustomerRequest customerRequest){
        Customer existingCustomer = customerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Customer", "id", id));
        
        existingCustomer.setName(customerRequest.name());
        existingCustomer.setEmail(customerRequest.email());
        existingCustomer.setPhoneNumber(customerRequest.phone());
        existingCustomer.setAddress(customerRequest.address());
        existingCustomer.setCountry(customerRequest.country());
        
        Customer updatedCustomer = customerRepository.save(existingCustomer);
        return CustomerSummaryResponse.from(updatedCustomer);
    }

    @Override
    public void delete(UUID id){
        if (!customerRepository.existsById(id)) {
            throw new ResourceNotFoundException("Customer", "id", id);
        }
        customerRepository.deleteById(id);
    }

}
