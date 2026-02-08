package org.invoicebuilder.invoices.repository;

import org.invoicebuilder.invoices.domain.Sender;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface SenderRepository extends JpaRepository<Sender, UUID> {
    boolean existsByEmail(String email);
}
