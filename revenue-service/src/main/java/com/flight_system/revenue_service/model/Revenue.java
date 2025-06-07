package com.flight_system.revenue_service.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "revenue")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Revenue {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "flight_number", nullable = false)
    private String flightNumber;

    @Column(name = "revenue_date", nullable = false)
    private LocalDate revenueDate;

    @Column(name = "amount", nullable = false)
    private Double amount;
} 