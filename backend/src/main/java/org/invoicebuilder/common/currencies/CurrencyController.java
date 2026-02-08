package org.invoicebuilder.common.currencies;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/currencies")
@RequiredArgsConstructor
@Tag(name = "Currency Management", description = "APIs for managing currencies")
public class CurrencyController {
    
    private final CurrencyService currencyService;
    
    @GetMapping
    @Operation(summary = "Get all currencies", description = "Returns a list of all available currencies")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Currencies retrieved successfully",
                    content = @Content(schema = @Schema(implementation = CurrencyResponse.class)))
    })
    public ResponseEntity<List<CurrencyResponse>> getAllCurrencies() {
        return ResponseEntity.ok(currencyService.getAllCurrencies());
    }
    
    @GetMapping("/{code}")
    @Operation(summary = "Get currency by code", description = "Returns a specific currency by its code")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Currency retrieved successfully",
                    content = @Content(schema = @Schema(implementation = CurrencyResponse.class))),
            @ApiResponse(responseCode = "404", description = "Currency not found")
    })
    public ResponseEntity<CurrencyResponse> getCurrencyByCode(@PathVariable String code) {
        return ResponseEntity.ok(currencyService.getCurrencyByCode(code));
    }
}
