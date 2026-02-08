package org.invoicebuilder.invoices.service;

import lombok.RequiredArgsConstructor;
import org.invoicebuilder.exception.ResourceNotFoundException;
import org.invoicebuilder.invoices.domain.Invoice;
import org.invoicebuilder.invoices.domain.InvoiceLineItem;
import org.invoicebuilder.invoices.domain.InvoiceStatus;
import org.invoicebuilder.invoices.dto.request.invoice.CreateInvoiceRequest;
import org.invoicebuilder.invoices.dto.request.invoice.InvoiceLineItemRequest;
import org.invoicebuilder.invoices.dto.response.invoice.InvoiceListResponse;
import org.invoicebuilder.invoices.dto.response.invoice.InvoiceLineItemResponse;
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
import java.util.List;
import java.util.UUID;
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
                .orElseThrow(() -> new ResourceNotFoundException("Customer", "id", createInvoiceRequest.customerId())));
        
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
        return invoiceRepository.findInvoiceList(pageable).map(InvoiceListResponse::from);
    }

    @Override
    public InvoiceListResponse getById(UUID id) {
        Invoice invoice = invoiceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Invoice", "id", id));
        return InvoiceListResponse.from(invoice);
    }

    @Override
    @Transactional
    public InvoiceListResponse update(UUID id, CreateInvoiceRequest createInvoiceRequest) {
        Invoice existingInvoice = invoiceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Invoice", "id", id));
        
        existingInvoice.setCurrency(createInvoiceRequest.currency());
        existingInvoice.setIssueDate(createInvoiceRequest.issueDate());
        existingInvoice.setDueDate(createInvoiceRequest.dueDate());
        existingInvoice.setTaxRate(createInvoiceRequest.taxRate());
        existingInvoice.setDiscount(createInvoiceRequest.discount());
        existingInvoice.setNotes(createInvoiceRequest.notes());
        
        if (!existingInvoice.getCustomer().getId().equals(createInvoiceRequest.customerId())) {
            existingInvoice.setCustomer(customerRepository.findById(createInvoiceRequest.customerId())
                    .orElseThrow(() -> new ResourceNotFoundException("Customer", "id", createInvoiceRequest.customerId())));
        }
        
        if (createInvoiceRequest.senderId() != null) {
            existingInvoice.setSender(senderRepository.findById(createInvoiceRequest.senderId())
                    .orElse(null));
        }
        
        // Optimize line item updates - only modify what's necessary
        BigDecimal subtotal = BigDecimal.ZERO;
        
        // Clear existing line items and recreate them (simpler approach for now)
        // TODO: Implement diff-based optimization for better performance
        invoiceLineItemRepository.deleteAll(existingInvoice.getLineItems());
        existingInvoice.getLineItems().clear();
        
        for (InvoiceLineItemRequest lineItemRequest : createInvoiceRequest.lineItems()) {
            InvoiceLineItem lineItem = InvoiceLineItemRequest.fromLineItemRequest(lineItemRequest);
            lineItem.setInvoice(existingInvoice);
            invoiceLineItemRepository.save(lineItem);
            existingInvoice.getLineItems().add(lineItem);
            subtotal = subtotal.add(lineItem.getLineTotal());
        }
        
        existingInvoice.setSubtotal(subtotal);
        BigDecimal taxAmount = subtotal.multiply(createInvoiceRequest.taxRate().divide(BigDecimal.valueOf(100), RoundingMode.HALF_UP));
        existingInvoice.setTaxAmount(taxAmount);
        BigDecimal totalAmount = subtotal.add(taxAmount).subtract(createInvoiceRequest.discount());
        existingInvoice.setTotalAmount(totalAmount);
        
        Invoice updatedInvoice = invoiceRepository.save(existingInvoice);
        return InvoiceListResponse.from(updatedInvoice);
    }

    @Override
    public void delete(UUID id) {
        if (!invoiceRepository.existsById(id)) {
            throw new ResourceNotFoundException("Invoice", "id", id);
        }
        invoiceRepository.deleteById(id);
    }

    public List<InvoiceLineItemResponse> getInvoiceLineItems(UUID id) {
        Invoice invoice = invoiceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Invoice", "id", id));
        
        return invoice.getLineItems().stream()
                .map(InvoiceLineItemResponse::from)
                .toList();
    }
}
