package org.invoicebuilder.performance;

import org.invoicebuilder.invoices.domain.Invoice;
import org.invoicebuilder.invoices.domain.InvoiceStatus;
import org.invoicebuilder.invoices.dto.response.invoice.InvoiceListResponse;
import org.invoicebuilder.invoices.repository.CustomerRepository;
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
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
public class InvoicePerformanceTest {

    @Autowired
    private InvoiceRepository invoiceRepository;

    @Autowired
    private InvoiceService invoiceService;

    @Autowired
    private CustomerRepository customerRepository;

    private static final int TEST_DATA_SIZE = 1000;
    private static final int WARMUP_ITERATIONS = 5;
    private static final int BENCHMARK_ITERATIONS = 20;

    @BeforeEach
    void setUp() {
        // Clean up existing data
        invoiceRepository.deleteAll();
        customerRepository.deleteAll();
        
        // Create test data
        createTestData(TEST_DATA_SIZE);
    }

    private void createTestData(int count) {
        // First create a test customer
        org.invoicebuilder.invoices.domain.Customer customer = new org.invoicebuilder.invoices.domain.Customer();
        customer.setName("Test Customer");
        customer.setEmail("test@example.com");
        customer.setPhoneNumber("123-456-7890");
        customer.setAddress("123 Test St");
        customer.setCountry("Test Country");
        customer = customerRepository.save(customer);

        List<Invoice> invoices = new ArrayList<>();
        
        for (int i = 0; i < count; i++) {
            Invoice invoice = new Invoice();
            invoice.setInvoiceNumber("INV-20240208-" + String.format("%04d", i + 1));
            invoice.setCurrency("USD");
            invoice.setTotalAmount(BigDecimal.valueOf(1000 + (i * 10)));
            invoice.setStatus(InvoiceStatus.PAID);
            invoice.setIssueDate(LocalDate.now().minusDays(i));
            invoice.setDueDate(LocalDate.now().plusDays(30 - i));
            invoice.setSubtotal(BigDecimal.valueOf(900 + (i * 10)));
            invoice.setTaxAmount(BigDecimal.valueOf(100 + (i * 1)));
            invoice.setDiscount(BigDecimal.valueOf(i % 5 == 0 ? 50 : 0));
            invoice.setCustomer(customer);
            
            invoices.add(invoice);
        }
        
        invoiceRepository.saveAll(invoices);
    }

    @Test
    void benchmarkFindAllVsProjections() {
        System.out.println("=== Invoice Performance Benchmark ===");
        System.out.println("Test Data Size: " + TEST_DATA_SIZE + " invoices");
        System.out.println("Warmup Iterations: " + WARMUP_ITERATIONS);
        System.out.println("Benchmark Iterations: " + BENCHMARK_ITERATIONS);
        System.out.println();

        Pageable pageable = PageRequest.of(0, 50);

        // Warmup
        System.out.println("Warming up...");
        for (int i = 0; i < WARMUP_ITERATIONS; i++) {
            benchmarkFindAll(pageable);
            benchmarkProjections(pageable);
            benchmarkServiceMethod(pageable);
        }

        // Benchmark findAll with EntityGraph
        System.out.println("Benchmarking findAll() with EntityGraph...");
        long[] findAllTimes = new long[BENCHMARK_ITERATIONS];
        for (int i = 0; i < BENCHMARK_ITERATIONS; i++) {
            findAllTimes[i] = benchmarkFindAll(pageable);
        }

        // Benchmark projections
        System.out.println("Benchmarking projections...");
        long[] projectionTimes = new long[BENCHMARK_ITERATIONS];
        for (int i = 0; i < BENCHMARK_ITERATIONS; i++) {
            projectionTimes[i] = benchmarkProjections(pageable);
        }

        // Benchmark service method (current implementation)
        System.out.println("Benchmarking service method (projections)...");
        long[] serviceTimes = new long[BENCHMARK_ITERATIONS];
        for (int i = 0; i < BENCHMARK_ITERATIONS; i++) {
            serviceTimes[i] = benchmarkServiceMethod(pageable);
        }

        // Calculate and display results
        displayResults("findAll() with EntityGraph", findAllTimes);
        displayResults("Projections", projectionTimes);
        displayResults("Service Method", serviceTimes);

        // Calculate performance improvement
        double avgFindAll = calculateAverage(findAllTimes);
        double avgProjections = calculateAverage(projectionTimes);
        double avgService = calculateAverage(serviceTimes);

        System.out.println("\n=== Performance Comparison ===");
        System.out.printf("Average findAll() time: %.2f ms\n", avgFindAll);
        System.out.printf("Average projections time: %.2f ms\n", avgProjections);
        System.out.printf("Average service method time: %.2f ms\n", avgService);
        
        double improvementVsFindAll = ((avgFindAll - avgProjections) / avgFindAll) * 100;
        double serviceVsProjections = ((avgService - avgProjections) / avgService) * 100;
        
        System.out.printf("Projections improvement over findAll: %.1f%%\n", improvementVsFindAll);
        System.out.printf("Service overhead over raw projections: %.1f%%\n", serviceVsProjections);

        // Verify both methods return the same number of results
        Page<Invoice> findAllResult = invoiceRepository.findAll(pageable);
        Page<InvoiceListResponse> projectionResult = invoiceService.list(pageable);
        
        assertEquals(findAllResult.getTotalElements(), projectionResult.getTotalElements(),
                "Both methods should return the same number of total elements");
        assertEquals(findAllResult.getContent().size(), projectionResult.getContent().size(),
                "Both methods should return the same number of page elements");
    }

    private long benchmarkFindAll(Pageable pageable) {
        long startTime = System.nanoTime();
        Page<Invoice> result = invoiceRepository.findAll(pageable);
        // Force evaluation by accessing content
        result.getContent().size();
        long endTime = System.nanoTime();
        return (endTime - startTime) / 1_000_000; // Convert to milliseconds
    }

    private long benchmarkProjections(Pageable pageable) {
        long startTime = System.nanoTime();
        var result = invoiceRepository.findInvoiceList(pageable);
        // Force evaluation by accessing content
        result.getContent().size();
        long endTime = System.nanoTime();
        return (endTime - startTime) / 1_000_000; // Convert to milliseconds
    }

    private long benchmarkServiceMethod(Pageable pageable) {
        long startTime = System.nanoTime();
        Page<InvoiceListResponse> result = invoiceService.list(pageable);
        // Force evaluation by accessing content
        result.getContent().size();
        long endTime = System.nanoTime();
        return (endTime - startTime) / 1_000_000; // Convert to milliseconds
    }

    private void displayResults(String methodName, long[] times) {
        double avg = calculateAverage(times);
        long min = calculateMin(times);
        long max = calculateMax(times);
        
        System.out.printf("%s - Avg: %.2f ms, Min: %d ms, Max: %d ms\n", 
                methodName, avg, min, max);
    }

    private double calculateAverage(long[] times) {
        return (double) sum(times) / times.length;
    }

    private long sum(long[] times) {
        long sum = 0;
        for (long time : times) {
            sum += time;
        }
        return sum;
    }

    private long calculateMin(long[] times) {
        long min = Long.MAX_VALUE;
        for (long time : times) {
            if (time < min) {
                min = time;
            }
        }
        return min;
    }

    private long calculateMax(long[] times) {
        long max = Long.MIN_VALUE;
        for (long time : times) {
            if (time > max) {
                max = time;
            }
        }
        return max;
    }

    @Test
    void memoryUsageComparison() {
        System.out.println("\n=== Memory Usage Analysis ===");
        
        Pageable pageable = PageRequest.of(0, 100);
        
        // Test memory usage with full entities
        Runtime runtime = Runtime.getRuntime();
        runtime.gc(); // Suggest garbage collection
        
        long beforeFindAll = runtime.totalMemory() - runtime.freeMemory();
        Page<Invoice> findAllResult = invoiceRepository.findAll(pageable);
        List<Invoice> findAllEntities = findAllResult.getContent();
        long afterFindAll = runtime.totalMemory() - runtime.freeMemory();
        long findAllMemory = afterFindAll - beforeFindAll;
        
        runtime.gc();
        
        // Test memory usage with projections
        long beforeProjections = runtime.totalMemory() - runtime.freeMemory();
        var projectionResult = invoiceRepository.findInvoiceList(pageable);
        List<?> projectionEntities = projectionResult.getContent();
        long afterProjections = runtime.totalMemory() - runtime.freeMemory();
        long projectionMemory = afterProjections - beforeProjections;
        
        System.out.printf("findAll() memory usage: ~%,d bytes\n", findAllMemory);
        System.out.printf("Projections memory usage: ~%,d bytes\n", projectionMemory);
        
        if (findAllMemory > 0 && projectionMemory > 0) {
            double memoryReduction = ((double)(findAllMemory - projectionMemory) / findAllMemory) * 100;
            System.out.printf("Memory reduction with projections: %.1f%%\n", memoryReduction);
        }
        
        // Verify same number of results
        assertEquals(findAllEntities.size(), projectionEntities.size(),
                "Both methods should return the same number of results");
    }
}
