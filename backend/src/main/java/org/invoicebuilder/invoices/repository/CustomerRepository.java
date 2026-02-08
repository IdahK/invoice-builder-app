package org.invoicebuilder.invoices.repository;

import org.invoicebuilder.invoices.domain.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface CustomerRepository extends JpaRepository<Customer, UUID> {
}
