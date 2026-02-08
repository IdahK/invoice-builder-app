package org.invoicebuilder.invoices.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface BaseService<T, P> {
    T create(P p);
    Page<T> list(Pageable pageable);
    T getById(UUID id);
    T update(UUID id, P p);
    void delete(UUID id);
}
