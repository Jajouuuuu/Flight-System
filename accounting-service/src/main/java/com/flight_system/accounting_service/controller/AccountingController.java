package com.flight_system.accounting_service.controller;

import com.flight_system.accounting_service.model.AccountTransaction;
import com.flight_system.accounting_service.repository.AccountTransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/accounting")
@RequiredArgsConstructor
public class AccountingController {

    private final AccountTransactionRepository transactionRepository;

    @GetMapping("/summary")
    public Map<String, Object> getRevenueSummary(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate,
            @RequestParam(required = false) Integer year,
            @RequestParam(required = false) Integer month
    ) {
        List<AccountTransaction> transactions;
        String period;

        if (startDate != null && endDate != null) {
            transactions = transactionRepository.findByTransactionDateBetween(startDate, endDate);
            period = String.format("From %s to %s", startDate, endDate);
        } else if (year != null && month != null) {
            transactions = transactionRepository.findByYearAndMonth(year, month);
            period = String.format("Year %d, Month %d", year, month);
        } else if (year != null) {
            transactions = transactionRepository.findByYear(year);
            period = String.format("Year %d", year);
        } else {
            transactions = transactionRepository.findAll();
            period = "All time";
        }

        double totalRevenue = transactions.stream().mapToDouble(AccountTransaction::getAmount).sum();

        return Map.of(
                "period", period,
                "totalRevenue", totalRevenue,
                "transactionCount", transactions.size(),
                "transactions", transactions
        );
    }

    @GetMapping("/transactions")
    public ResponseEntity<List<AccountTransaction>> getAllTransactions() {
        return ResponseEntity.ok(transactionRepository.findAll());
    }

    @GetMapping("/transactions/{id}")
    public ResponseEntity<AccountTransaction> getTransaction(@PathVariable Long id) {
        return transactionRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/transactions")
    public ResponseEntity<AccountTransaction> createTransaction(@RequestBody AccountTransaction transaction) {
        transaction.setTransactionDate(LocalDateTime.now());
        AccountTransaction saved = transactionRepository.save(transaction);
        return new ResponseEntity<>(saved, HttpStatus.CREATED);
    }

    @GetMapping("/transactions/booking/{bookingNumber}")
    public ResponseEntity<List<AccountTransaction>> getTransactionsByBooking(@PathVariable String bookingNumber) {
        List<AccountTransaction> transactions = transactionRepository.findByBookingNumber(bookingNumber);
        return ResponseEntity.ok(transactions);
    }

    @GetMapping("/transactions/date-range")
    public ResponseEntity<List<AccountTransaction>> getTransactionsByDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        List<AccountTransaction> transactions = transactionRepository.findByTransactionDateBetween(startDate, endDate);
        return ResponseEntity.ok(transactions);
    }

    @GetMapping("/transactions/type/{type}")
    public ResponseEntity<List<AccountTransaction>> getTransactionsByType(@PathVariable String type) {
        List<AccountTransaction> transactions = transactionRepository.findByTransactionType(type);
        return ResponseEntity.ok(transactions);
    }

    @GetMapping("/revenue/total")
    public ResponseEntity<Double> getTotalRevenue() {
        List<AccountTransaction> transactions = transactionRepository.findAll();
        double total = transactions.stream().mapToDouble(AccountTransaction::getAmount).sum();
        return ResponseEntity.ok(total);
    }

    @GetMapping("/revenue/monthly/{year}/{month}")
    public ResponseEntity<Double> getMonthlyRevenue(@PathVariable Integer year, @PathVariable Integer month) {
        List<AccountTransaction> transactions = transactionRepository.findByYearAndMonth(year, month);
        double total = transactions.stream().mapToDouble(AccountTransaction::getAmount).sum();
        return ResponseEntity.ok(total);
    }

    @GetMapping("/revenue/yearly/{year}")
    public ResponseEntity<Double> getYearlyRevenue(@PathVariable Integer year) {
        List<AccountTransaction> transactions = transactionRepository.findByYear(year);
        double total = transactions.stream().mapToDouble(AccountTransaction::getAmount).sum();
        return ResponseEntity.ok(total);
    }
}
