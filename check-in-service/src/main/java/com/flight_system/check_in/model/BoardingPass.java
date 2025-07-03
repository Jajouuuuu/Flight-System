package com.flight_system.check_in.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "boarding_passes")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BoardingPass {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "boarding_pass_number", unique = true, nullable = false)
    private String boardingPassNumber;

    @NotNull
    @Column(name = "booking_number", nullable = false)
    private String bookingNumber;

    @NotNull
    @Column(name = "passenger_name", nullable = false)
    private String passengerName;

    @NotNull
    @Column(name = "flight_number", nullable = false)
    private String flightNumber;

    @Column(name = "seat_number")
    private String seatNumber;

    @NotNull
    @Column(name = "departure_time", nullable = false)
    private LocalDateTime departureTime;

    @Column(name = "departure_gate")
    private String departureGate;

    @Column(name = "boarding_time")
    private LocalDateTime boardingTime;

    @NotNull
    @Column(name = "departure_airport", nullable = false)
    private String departureAirport;

    @NotNull
    @Column(name = "arrival_airport", nullable = false)
    private String arrivalAirport;

    @NotNull
    @Column(name = "customer_id", nullable = false)
    private Long customerId;

    @NotNull
    @Column(name = "status", nullable = false)
    private String status; // ACTIVE, USED, CANCELLED

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = createdAt;
        status = "ACTIVE";
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
} 