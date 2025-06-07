package com.flight_system.inventory_service.model;

import jakarta.persistence.*;
import lombok.*;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Inventory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String flightNumber;

    private String seatClass;

    private int totalSeats;

    private int availableSeats;

    private LocalDateTime departureDateTime;

    @OneToMany(mappedBy = "inventory", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<Seat> seats;

    @PrePersist
    @PreUpdate
    private void assignSeatsToThisInventory() {
        if (seats != null) {
            for (Seat seat : seats) {
                seat.setInventory(this);
            }
        }
    }
}


