package org.invoicebuilder.invoices.service;

import lombok.RequiredArgsConstructor;
import org.invoicebuilder.exception.ResourceNotFoundException;
import org.invoicebuilder.invoices.domain.Sender;
import org.invoicebuilder.invoices.dto.request.sender.CreateSenderRequest;
import org.invoicebuilder.invoices.dto.response.sender.SenderSummaryResponse;
import org.invoicebuilder.invoices.repository.SenderRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class SenderService implements BaseService<SenderSummaryResponse, CreateSenderRequest> {
    private final SenderRepository senderRepository;

    @Override
    public SenderSummaryResponse create(CreateSenderRequest senderRequest) {
        if (senderRepository.existsByEmail(senderRequest.email())) {
            throw new DataIntegrityViolationException("Sender with email '" + senderRequest.email() + "' already exists. Only one sender record is allowed.");
        }
        Sender sender = CreateSenderRequest.fromRequest(senderRequest);
        Sender savedSender = senderRepository.save(sender);
        return SenderSummaryResponse.from(savedSender);
    }

    @Override
    public Page<SenderSummaryResponse> list(Pageable pageable) {
        pageable = pageable.getPageSize() == 0 ? Pageable.ofSize(10) : pageable;
        return senderRepository.findAll(pageable).map(SenderSummaryResponse::from);
    }

    @Override
    public SenderSummaryResponse getById(UUID id) {
        Sender sender = senderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Sender", "id", id));
        return SenderSummaryResponse.from(sender);
    }

    @Override
    public SenderSummaryResponse update(UUID id, CreateSenderRequest senderRequest) {
        Sender existingSender = senderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Sender", "id", id));
        
        // Check if email is being changed to another existing sender's email
        if (!existingSender.getEmail().equals(senderRequest.email()) && 
            senderRepository.existsByEmail(senderRequest.email())) {
            throw new DataIntegrityViolationException("Sender with email '" + senderRequest.email() + "' already exists. Only one sender record is allowed.");
        }
        
        existingSender.setName(senderRequest.name());
        existingSender.setEmail(senderRequest.email());
        existingSender.setPhoneNumber(senderRequest.phoneNumber());
        existingSender.setAddress(senderRequest.address());
        
        Sender updatedSender = senderRepository.save(existingSender);
        return SenderSummaryResponse.from(updatedSender);
    }

    @Override
    public void delete(UUID id) {
        if (!senderRepository.existsById(id)) {
            throw new ResourceNotFoundException("Sender", "id", id);
        }
        senderRepository.deleteById(id);
    }
}
