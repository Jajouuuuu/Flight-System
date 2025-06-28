package com.flight_system.accounting_service.controller;

import com.flight_system.accounting_service.model.AccountTransaction;
import com.flight_system.accounting_service.repository.AccountTransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/accounting")
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
                "totalRevenue", totalRevenue
        );
    }
}
