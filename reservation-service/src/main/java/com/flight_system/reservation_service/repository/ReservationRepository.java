package com.flight_system.reservation_service.repository;

import com.flight_system.reservation_service.model.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    Optional<Reservation> findByReservationNumber(String reservationNumber);
    List<Reservation> findByCustomerId(Long customerId);
    List<Reservation> findByFlightNumber(String flightNumber);
} 