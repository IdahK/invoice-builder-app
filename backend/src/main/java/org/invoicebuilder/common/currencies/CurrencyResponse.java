package org.invoicebuilder.common.currencies;

public record CurrencyResponse(
        String code,
        String name
) {
    public static CurrencyResponse from(Currency currency){
        return new CurrencyResponse(
                currency.getCode(),
                currency.getName()
        );
    }
}
