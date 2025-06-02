package com.example.reservation.service;

import com.example.reservation.dto.ReservationRequest;
import com.example.reservation.model.Reservation;
import com.example.reservation.repository.ReservationRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReservationServiceImpl implements ReservationService {

    private final ReservationRepository repository;

    @Override
    @Transactional
    public Reservation createReservation(ReservationRequest request) {
        Reservation reservation = new Reservation();
        reservation.setFlightId(request.getFlightId());
        reservation.setCustomerId(request.getCustomerId());
        reservation.setNumberOfSeats(request.getNumberOfSeats());
        return repository.save(reservation);
    }

    @Override
    @Transactional(readOnly = true)
    public Reservation getReservation(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Reservation not found: " + id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<Reservation> getReservationsByCustomer(String customerId) {
        return repository.findByCustomerId(customerId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Reservation> getReservationsByFlight(String flightId) {
        return repository.findByFlightId(flightId);
    }

    @Override
    @Transactional
    public void cancelReservation(Long id) {
        Reservation reservation = getReservation(id);
        if ("CANCELLED".equals(reservation.getStatus())) {
            throw new IllegalStateException("Reservation is already cancelled");
        }
        reservation.setStatus("CANCELLED");
        repository.save(reservation);
    }
} 