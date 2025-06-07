package com.flight_system.inventory_service.model;

import jakarta.persistence.*;
import lombok.*;
import com.fasterxml.jackson.annotation.JsonBackReference;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Seat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String seatNumber;

    private boolean available = true;

    private String seatClass;

    @ManyToOne
    @JoinColumn(name = "inventory_id")
    @JsonBackReference
    private Inventory inventory;
}


