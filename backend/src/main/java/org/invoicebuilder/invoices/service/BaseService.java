package org.invoicebuilder.invoices.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BaseService<T, P> {
    T create(P p);
    Page<T> list(Pageable pageable);
}
