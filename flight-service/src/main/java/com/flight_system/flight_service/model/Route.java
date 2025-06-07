package com.flight_system.flight_service.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Entity
@Table(name = "routes")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Route {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(name = "origin_airport", nullable = false)
    private String originAirport;

    @NotBlank
    @Column(name = "destination_airport", nullable = false)
    private String destinationAirport;

    @NotNull
    @Column(name = "distance_miles")
    private Double distanceMiles;

    @Column(name = "estimated_duration_minutes")
    private Integer estimatedDurationMinutes;

    @Column(name = "base_price")
    private Double basePrice;

    private String status; // ACTIVE, SUSPENDED, SEASONAL

    @Column(name = "route_code", unique = true)
    private String routeCode;
} 