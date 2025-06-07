package com.flight_system.inventory_service.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "flight_inventory")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FlightInventory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "flight_id", unique = true, nullable = false)
    private Long flightId;

    @NotNull
    @Column(name = "flight_number", unique = true, nullable = false)
    private String flightNumber;

    @NotNull
    @PositiveOrZero
    @Column(name = "total_seats", nullable = false)
    private Integer totalSeats;

    @NotNull
    @PositiveOrZero
    @Column(name = "available_seats", nullable = false)
    private Integer availableSeats;

    @NotNull
    @PositiveOrZero
    @Column(name = "reserved_seats", nullable = false)
    private Integer reservedSeats;

    @Version
    private Long version;
} 