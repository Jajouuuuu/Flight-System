package com.flight_system.reservation_service.service;

import com.flight_system.reservation_service.exceptions.InventoryServiceException;
import com.flight_system.reservation_service.exceptions.ReservationNotFoundException;
import com.flight_system.reservation_service.feign.InventoryClient;
import com.flight_system.reservation_service.model.Reservation;
import com.flight_system.reservation_service.repository.ReservationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
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

    @Override
    @Transactional(readOnly = true)
    public List<Reservation> getAllReservations() {
        return reservationRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Reservation> findByCustomerId(Long customerId) {
        return reservationRepository.findByCustomerId(customerId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Reservation> findByFlightNumber(String flightNumber) {
        return reservationRepository.findByFlightNumber(flightNumber);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Reservation> findByStatus(String status) {
        return reservationRepository.findByStatus(status);
    }

    @Override
    @Transactional
    public Reservation updateReservation(String reservationNumber, Reservation updatedReservation) {
        Reservation existingReservation = findByReservationNumber(reservationNumber);
        
        // Update fields
        existingReservation.setFlightNumber(updatedReservation.getFlightNumber());
        existingReservation.setCustomerId(updatedReservation.getCustomerId());
        existingReservation.setNumberOfSeats(updatedReservation.getNumberOfSeats());
        
        return reservationRepository.save(existingReservation);
    }

    @Override
    @Transactional
    public void deleteReservation(String reservationNumber) {
        Reservation reservation = findByReservationNumber(reservationNumber);
        
        // Release seats if reservation is not cancelled
        if (!"CANCELLED".equals(reservation.getStatus())) {
            try {
                inventoryClient.releaseSeats(reservation.getFlightNumber(), reservation.getNumberOfSeats());
            } catch (Exception e) {
                throw new InventoryServiceException("Failed to release seats for flight " + reservation.getFlightNumber());
            }
        }
        
        reservationRepository.delete(reservation);
        
        kafkaTemplate.send("reservation-deleted", Map.of(
                "reservationNumber", reservationNumber
        ));
    }

    @Override
    @Transactional
    public Reservation updateSeatCount(String reservationNumber, int newSeatCount) {
        Reservation reservation = findByReservationNumber(reservationNumber);
        int currentSeats = reservation.getNumberOfSeats();
        int seatDifference = newSeatCount - currentSeats;
        
        try {
            if (seatDifference > 0) {
                // Need more seats
                inventoryClient.reserveSeats(reservation.getFlightNumber(), seatDifference);
            } else if (seatDifference < 0) {
                // Release excess seats
                inventoryClient.releaseSeats(reservation.getFlightNumber(), Math.abs(seatDifference));
            }
        } catch (Exception e) {
            throw new InventoryServiceException("Failed to update seat count for flight " + reservation.getFlightNumber());
        }
        
        reservation.setNumberOfSeats(newSeatCount);
        return reservationRepository.save(reservation);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Reservation> searchReservations(String customerEmail, String flightNumber, String status) {
        // This is a simplified implementation - in reality, you'd need to integrate with customer service
        // to get customer ID from email, or implement a more complex query
        
        if (flightNumber != null && status != null) {
            return reservationRepository.findByFlightNumberAndStatus(flightNumber, status);
        } else if (flightNumber != null) {
            return findByFlightNumber(flightNumber);
        } else if (status != null) {
            return findByStatus(status);
        } else {
            return getAllReservations();
        }
    }
}
