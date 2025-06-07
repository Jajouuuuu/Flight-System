package com.flight_system.accounting_service.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "account_transactions")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AccountTransaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "transaction_date", nullable = false)
    private LocalDateTime transactionDate;

    @Column(name = "description", nullable = false)
    private String description;

    @Column(name = "debit", nullable = false)
    private Double debit;

    @Column(name = "credit", nullable = false)
    private Double credit;

    @Column(name = "reference_id")
    private String referenceId; // e.g., bookingNumber or transactionId

    @PrePersist
    protected void onCreate() {
        transactionDate = LocalDateTime.now();
    }
} 