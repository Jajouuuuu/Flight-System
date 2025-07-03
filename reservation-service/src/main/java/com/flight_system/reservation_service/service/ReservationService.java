package com.flight_system.reservation_service.service;

import com.flight_system.reservation_service.model.Reservation;
import java.util.List;
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

    // New methods for comprehensive reservation management
    @Transactional(readOnly = true)
    public List<Reservation> getAllReservations() {
        return reservationRepository.findAll();
    }

    @Transactional(readOnly = true)
    public List<Reservation> findByCustomerId(Long customerId) {
        return reservationRepository.findByCustomerId(customerId);
    }

    @Transactional(readOnly = true)
    public List<Reservation> findByFlightNumber(String flightNumber) {
        return reservationRepository.findByFlightNumber(flightNumber);
    }

    @Transactional(readOnly = true)
    public List<Reservation> findByStatus(String status) {
        return reservationRepository.findByStatus(status);
    }

    @Transactional
    public Reservation updateReservation(String reservationNumber, Reservation updatedReservation) {
        Reservation existingReservation = findByReservationNumber(reservationNumber);
        
        // Update fields
        existingReservation.setFlightNumber(updatedReservation.getFlightNumber());
        existingReservation.setCustomerId(updatedReservation.getCustomerId());
        existingReservation.setNumberOfSeats(updatedReservation.getNumberOfSeats());
        
        return reservationRepository.save(existingReservation);
    }

    @Transactional
    public void deleteReservation(String reservationNumber) {
        Reservation reservation = findByReservationNumber(reservationNumber);
        
        // Release seats if reservation is not cancelled
        if (!"CANCELLED".equals(reservation.getStatus())) {
            inventoryClient.releaseSeats(reservation.getFlightNumber(), reservation.getNumberOfSeats());
        }
        
        reservationRepository.delete(reservation);
        
        kafkaTemplate.send("reservation-deleted", Map.of(
                "reservationNumber", reservationNumber
        ));
    }

    @Transactional
    public Reservation updateSeatCount(String reservationNumber, int newSeatCount) {
        Reservation reservation = findByReservationNumber(reservationNumber);
        int currentSeats = reservation.getNumberOfSeats();
        int seatDifference = newSeatCount - currentSeats;
        
        if (seatDifference > 0) {
            // Need more seats
            inventoryClient.reserveSeats(reservation.getFlightNumber(), seatDifference);
        } else if (seatDifference < 0) {
            // Release excess seats
            inventoryClient.releaseSeats(reservation.getFlightNumber(), Math.abs(seatDifference));
        }
        
        reservation.setNumberOfSeats(newSeatCount);
        return reservationRepository.save(reservation);
    }

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
