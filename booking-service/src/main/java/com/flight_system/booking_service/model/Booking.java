package com.flight_system.booking_service.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "bookings")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "booking_number", unique = true, nullable = false)
    private String bookingNumber;

    @NotNull
    @Column(name = "customer_id", nullable = false)
    private Long customerId;

    @NotNull
    @Column(name = "flight_id", nullable = false)
    private Long flightId;

    @Column(name = "flight_number", nullable = false)
    private String flightNumber;

    @Column(name = "passenger_name", nullable = false)
    private String passengerName;

    @Column(name = "passenger_email", nullable = false)
    private String passengerEmail;

    @Column(name = "seat_number")
    private String seatNumber;

    @Positive
    @Column(name = "total_price", nullable = false)
    private Double totalPrice;

    @Column(name = "payment_id")
    private String paymentId;

    @Column(name = "payment_status")
    private String paymentStatus; // PENDING, COMPLETED, FAILED

    @Column(name = "booking_status")
    private String bookingStatus; // PENDING, CONFIRMED, CANCELLED

    @Column(name = "departure_time", nullable = false)
    private LocalDateTime departureTime;

    @Column(name = "arrival_time", nullable = false)
    private LocalDateTime arrivalTime;

    @Column(name = "origin_airport", nullable = false)
    private String originAirport;

    @Column(name = "destination_airport", nullable = false)
    private String destinationAirport;

    @Column(name = "special_requests")
    private String specialRequests;

    @Column(name = "baggage_allowance")
    private Integer baggageAllowance;

    @Column(name = "meal_preference")
    private String mealPreference;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = createdAt;
        if (bookingStatus == null) {
            bookingStatus = "PENDING";
        }
        if (paymentStatus == null) {
            paymentStatus = "PENDING";
        }
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
} 