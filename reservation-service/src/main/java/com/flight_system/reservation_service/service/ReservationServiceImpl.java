package com.flight_system.reservation_service.service;

import com.flight_system.reservation_service.exceptions.InventoryServiceException;
import com.flight_system.reservation_service.exceptions.ReservationNotFoundException;
import com.flight_system.reservation_service.feign.InventoryClient;
import com.flight_system.reservation_service.model.Reservation;
import com.flight_system.reservation_service.repository.ReservationRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class ReservationServiceImpl implements ReservationService {

    private final ReservationRepository reservationRepository;
    private final InventoryClient inventoryClient;
    private final KafkaTemplate<String, Object> kafkaTemplate;

    @Override
    @Transactional
    public Reservation createReservation(Reservation reservation) {
        log.info("Creating reservation for flight {}", reservation.getFlightNumber());

        try {
            inventoryClient.reserveSeats(reservation.getFlightNumber(), reservation.getNumberOfSeats());
        } catch (Exception e) {
            throw new InventoryServiceException("Failed to reserve seats for flight " + reservation.getFlightNumber());
        }

        reservation.setReservationNumber("RES" + UUID.randomUUID().toString().substring(0, 10).toUpperCase());
        reservation.setStatus("PENDING");
        Reservation savedReservation = reservationRepository.save(reservation);

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

    @Override
    @Transactional
    public Reservation confirmReservation(String reservationNumber) {
        Reservation reservation = findByReservationNumber(reservationNumber);
        reservation.setStatus("CONFIRMED");
        Reservation updated = reservationRepository.save(reservation);

        kafkaTemplate.send("reservation-confirmed", Map.of(
                "reservationNumber", updated.getReservationNumber()
        ));

        return updated;
    }

    @Override
    @Transactional
    public Reservation cancelReservation(String reservationNumber) {
        Reservation reservation = findByReservationNumber(reservationNumber);

        try {
            inventoryClient.releaseSeats(reservation.getFlightNumber(), reservation.getNumberOfSeats());
        } catch (Exception e) {
            throw new InventoryServiceException("Failed to release seats for flight " + reservation.getFlightNumber());
        }

        reservation.setStatus("CANCELLED");
        Reservation updated = reservationRepository.save(reservation);

        kafkaTemplate.send("reservation-cancelled", Map.of(
                "reservationNumber", updated.getReservationNumber()
        ));

        return updated;
    }

    @Override
    @Transactional
    public Reservation findByReservationNumber(String reservationNumber) {
        return reservationRepository.findByReservationNumber(reservationNumber)
                .orElseThrow(() -> new ReservationNotFoundException("Reservation not found: " + reservationNumber));
    }
}
