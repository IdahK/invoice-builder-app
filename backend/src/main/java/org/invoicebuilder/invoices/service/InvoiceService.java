package org.invoicebuilder.invoices.service;

import lombok.RequiredArgsConstructor;
import org.invoicebuilder.invoices.domain.Invoice;
import org.invoicebuilder.invoices.domain.InvoiceLineItem;
import org.invoicebuilder.invoices.domain.InvoiceStatus;
import org.invoicebuilder.invoices.dto.request.invoice.CreateInvoiceRequest;
import org.invoicebuilder.invoices.dto.request.invoice.InvoiceLineItemRequest;
import org.invoicebuilder.invoices.dto.response.invoice.InvoiceListResponse;
import org.invoicebuilder.invoices.repository.InvoiceRepository;
import org.invoicebuilder.invoices.repository.InvoiceLineItemRepository;
import org.invoicebuilder.invoices.repository.CustomerRepository;
import org.invoicebuilder.invoices.repository.SenderRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.atomic.AtomicLong;

@Service
@RequiredArgsConstructor
public class InvoiceService implements BaseService<InvoiceListResponse, CreateInvoiceRequest> {
    private final InvoiceRepository invoiceRepository;
    private final InvoiceLineItemRepository invoiceLineItemRepository;
    private final CustomerRepository customerRepository;
    private final SenderRepository senderRepository;
    
    private final AtomicLong invoiceNumberCounter = new AtomicLong(1);
    
    private String generateUniqueInvoiceNumber() {
        String datePrefix = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        long sequence = invoiceNumberCounter.getAndIncrement();
        return String.format("INV-%s-%04d", datePrefix, sequence);
    }
    
    @Override
    @Transactional
    public InvoiceListResponse create(CreateInvoiceRequest createInvoiceRequest) {
        String invoiceNumber = generateUniqueInvoiceNumber();
        Invoice invoice = CreateInvoiceRequest.fromRequest(createInvoiceRequest, invoiceNumber);
        
        invoice.setCustomer(customerRepository.findById(createInvoiceRequest.customerId())
                .orElseThrow(() -> new RuntimeException("Customer not found")));
        
        invoice.setSender(senderRepository.findById(createInvoiceRequest.senderId())
                .orElse(null));
        
        Invoice savedInvoice = invoiceRepository.save(invoice);
        
        BigDecimal subtotal = BigDecimal.ZERO;
        for (InvoiceLineItemRequest lineItemRequest : createInvoiceRequest.lineItems()) {
            InvoiceLineItem lineItem = InvoiceLineItemRequest.fromLineItemRequest(lineItemRequest);
            lineItem.setInvoice(savedInvoice);
            invoiceLineItemRepository.save(lineItem);
            subtotal = subtotal.add(lineItem.getLineTotal());
        }
        
        savedInvoice.setSubtotal(subtotal);
        BigDecimal taxAmount = subtotal.multiply(createInvoiceRequest.taxRate().divide(BigDecimal.valueOf(100), RoundingMode.HALF_UP));
        savedInvoice.setTaxAmount(taxAmount);
        BigDecimal totalAmount = subtotal.add(taxAmount).subtract(createInvoiceRequest.discount());
        savedInvoice.setTotalAmount(totalAmount);
        
        savedInvoice = invoiceRepository.save(savedInvoice);
        
        return InvoiceListResponse.from(savedInvoice);
    }
    
    @Override
    public Page<InvoiceListResponse> list(Pageable pageable) {
        pageable = pageable.getPageSize() == 0 ? Pageable.ofSize(10) : pageable;
        return invoiceRepository.findAll(pageable).map(InvoiceListResponse::from);
    }
}
