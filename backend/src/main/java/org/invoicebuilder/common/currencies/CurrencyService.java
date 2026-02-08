package org.invoicebuilder.common.currencies;

import lombok.RequiredArgsConstructor;
import org.invoicebuilder.exception.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CurrencyService {
    
    private final CurrencyRepository currencyRepository;
    
    public List<CurrencyResponse> getAllCurrencies() {
        return currencyRepository.findAll()
                .stream()
                .map(CurrencyResponse::from)
                .toList();
    }
    
    public CurrencyResponse getCurrencyByCode(String code) {
        return currencyRepository.findByCode(code)
                .map(CurrencyResponse::from)
                .orElseThrow(() -> new ResourceNotFoundException("Currency", "code", code));
    }
}
