package com.flight_system.flight_service.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.*;

@Entity
@Table(name = "aircraft")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Aircraft {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(unique = true, nullable = false)
    private String registrationNumber;

    @NotBlank
    private String model;

    @NotBlank
    private String manufacturer;

    @Positive
    @Column(name = "total_seats")
    private Integer totalSeats;

    @Column(name = "manufacturing_year")
    private Integer manufacturingYear;

    private String status; // ACTIVE, MAINTENANCE, RETIRED

    @Column(name = "last_maintenance_date")
    private String lastMaintenanceDate;

    @Column(name = "next_maintenance_date")
    private String nextMaintenanceDate;
} 