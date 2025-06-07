package com.flight_system.reservation_service.service;

import com.flight_system.reservation_service.feign.InventoryClient;
import com.flight_system.reservation_service.model.Reservation;
import com.flight_system.reservation_service.repository.ReservationRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class ReservationService {
    private final ReservationRepository reservationRepository;
    private final InventoryClient inventoryClient;
    private final KafkaTemplate<String, Object> kafkaTemplate;

    @Transactional
    public Reservation createReservation(Reservation reservation) {
        log.info("Creating reservation for flight {}", reservation.getFlightNumber());
        // 1. Reserve seats in inventory
        inventoryClient.reserveSeats(reservation.getFlightNumber(), reservation.getNumberOfSeats());

        // 2. Create and save the reservation
        reservation.setReservationNumber("RES" + UUID.randomUUID().toString().substring(0, 10).toUpperCase());
        reservation.setStatus("PENDING");
        Reservation savedReservation = reservationRepository.save(reservation);

        // 3. Publish reservation created event
        kafkaTemplate.send("reservation-created", Map.of(
                "reservationId", savedReservation.getId(),
                "reservationNumber", savedReservation.getReservationNumber(),
                "flightNumber", savedReservation.getFlightNumber(),
                "customerId", savedReservation.getCustomerId(),
                "status", savedReservation.getStatus()
        ));
        
        log.info("Reservation {} created successfully", savedReservation.getReservationNumber());
        return savedReservation;
    }

    @Transactional
    public Reservation confirmReservation(String reservationNumber) {
        Reservation reservation = findByReservationNumber(reservationNumber);
        reservation.setStatus("CONFIRMED");
        
        kafkaTemplate.send("reservation-confirmed", Map.of(
                "reservationNumber", reservation.getReservationNumber()
        ));
        
        return reservationRepository.save(reservation);
    }

    @Transactional
    public Reservation cancelReservation(String reservationNumber) {
        Reservation reservation = findByReservationNumber(reservationNumber);
        
        // Release seats in inventory
        inventoryClient.releaseSeats(reservation.getFlightNumber(), reservation.getNumberOfSeats());
        
        reservation.setStatus("CANCELLED");

        kafkaTemplate.send("reservation-cancelled", Map.of(
                "reservationNumber", reservation.getReservationNumber()
        ));
        
        return reservationRepository.save(reservation);
    }

    @Transactional(readOnly = true)
    public Reservation findByReservationNumber(String reservationNumber) {
        return reservationRepository.findByReservationNumber(reservationNumber)
                .orElseThrow(() -> new EntityNotFoundException("Reservation not found: " + reservationNumber));
    }
} 