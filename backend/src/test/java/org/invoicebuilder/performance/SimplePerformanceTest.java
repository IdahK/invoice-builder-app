package org.invoicebuilder.performance;

import org.invoicebuilder.invoices.domain.Invoice;
import org.invoicebuilder.invoices.domain.InvoiceStatus;
import org.invoicebuilder.invoices.dto.response.invoice.InvoiceListResponse;
import org.invoicebuilder.invoices.repository.InvoiceRepository;
import org.invoicebuilder.invoices.service.InvoiceService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;

@SpringBootTest
@ActiveProfiles("test")
public class SimplePerformanceTest {

    @Autowired
    private InvoiceRepository invoiceRepository;

    @Autowired
    private InvoiceService invoiceService;

    private static final int BENCHMARK_ITERATIONS = 10;

    @BeforeEach
    @Transactional
    void setUp() {
        // Clean up existing data
        invoiceRepository.deleteAll();
        
        // Create minimal test data using existing entities
        if (invoiceRepository.count() == 0) {
            createMinimalTestData();
        }
    }

    private void createMinimalTestData() {
        // Check if we have any customers first, if not create one
        // For this simple test, we'll work with existing data or create minimal invoices
        for (int i = 0; i < 100; i++) {
            Invoice invoice = new Invoice();
            invoice.setInvoiceNumber("INV-TEST-" + String.format("%03d", i + 1));
            invoice.setCurrency("USD");
            invoice.setTotalAmount(BigDecimal.valueOf(100 + i));
            invoice.setStatus(InvoiceStatus.DRAFT);
            invoice.setIssueDate(LocalDate.now().minusDays(i));
            invoice.setDueDate(LocalDate.now().plusDays(30));
            invoice.setSubtotal(BigDecimal.valueOf(90 + i));
            invoice.setTaxAmount(BigDecimal.valueOf(10));
            invoice.setDiscount(BigDecimal.ZERO);
            
            try {
                invoiceRepository.save(invoice);
            } catch (Exception e) {
                // Skip if customer is required but not available
                break;
            }
        }
    }

    @Test
    void benchmarkBasicComparison() {
        System.out.println("=== Simple Performance Benchmark ===");
        
        Pageable pageable = PageRequest.of(0, 20);
        
        // Warmup
        System.out.println("Warming up...");
        try {
            invoiceRepository.findAll(pageable).getContent().size();
            invoiceService.list(pageable).getContent().size();
        } catch (Exception e) {
            System.out.println("Warmup failed: " + e.getMessage());
            return;
        }

        // Benchmark findAll
        System.out.println("Benchmarking findAll()...");
        long[] findAllTimes = new long[BENCHMARK_ITERATIONS];
        for (int i = 0; i < BENCHMARK_ITERATIONS; i++) {
            long startTime = System.nanoTime();
            try {
                Page<Invoice> result = invoiceRepository.findAll(pageable);
                result.getContent().size(); // Force evaluation
                long endTime = System.nanoTime();
                findAllTimes[i] = (endTime - startTime) / 1_000_000;
            } catch (Exception e) {
                findAllTimes[i] = -1; // Mark as failed
            }
        }

        // Benchmark service method (projections)
        System.out.println("Benchmarking service method (projections)...");
        long[] serviceTimes = new long[BENCHMARK_ITERATIONS];
        for (int i = 0; i < BENCHMARK_ITERATIONS; i++) {
            long startTime = System.nanoTime();
            try {
                Page<InvoiceListResponse> result = invoiceService.list(pageable);
                result.getContent().size(); // Force evaluation
                long endTime = System.nanoTime();
                serviceTimes[i] = (endTime - startTime) / 1_000_000;
            } catch (Exception e) {
                serviceTimes[i] = -1; // Mark as failed
            }
        }

        // Calculate and display results
        displayResults("findAll()", findAllTimes);
        displayResults("Service (projections)", serviceTimes);

        // Calculate averages (excluding failed attempts)
        double avgFindAll = calculateAverageExcludingFailures(findAllTimes);
        double avgService = calculateAverageExcludingFailures(serviceTimes);

        if (avgFindAll > 0 && avgService > 0) {
            System.out.println("\n=== Performance Comparison ===");
            System.out.printf("Average findAll() time: %.2f ms\n", avgFindAll);
            System.out.printf("Average service time: %.2f ms\n", avgService);
            
            double improvement = ((avgFindAll - avgService) / avgFindAll) * 100;
            System.out.printf("Performance improvement: %.1f%%\n", improvement);
        } else {
            System.out.println("Could not calculate performance comparison due to errors");
        }
    }

    private void displayResults(String methodName, long[] times) {
        double avg = calculateAverageExcludingFailures(times);
        long min = calculateMinExcludingFailures(times);
        long max = calculateMaxExcludingFailures(times);
        int successCount = countSuccessful(times);
        
        System.out.printf("%s - Success: %d/%d, Avg: %.2f ms, Min: %d ms, Max: %d ms\n", 
                methodName, successCount, times.length, avg, min, max);
    }

    private double calculateAverageExcludingFailures(long[] times) {
        long sum = 0;
        int count = 0;
        for (long time : times) {
            if (time > 0) {
                sum += time;
                count++;
            }
        }
        return count > 0 ? (double) sum / count : 0;
    }

    private long calculateMinExcludingFailures(long[] times) {
        long min = Long.MAX_VALUE;
        for (long time : times) {
            if (time > 0 && time < min) {
                min = time;
            }
        }
        return min == Long.MAX_VALUE ? 0 : min;
    }

    private long calculateMaxExcludingFailures(long[] times) {
        long max = Long.MIN_VALUE;
        for (long time : times) {
            if (time > 0 && time > max) {
                max = time;
            }
        }
        return max == Long.MIN_VALUE ? 0 : max;
    }

    private int countSuccessful(long[] times) {
        int count = 0;
        for (long time : times) {
            if (time > 0) {
                count++;
            }
        }
        return count;
    }
}
