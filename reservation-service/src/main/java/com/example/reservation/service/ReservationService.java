package com.example.reservation.service;

import com.example.reservation.dto.ReservationRequest;
import com.example.reservation.model.Reservation;
import java.util.List;

public interface ReservationService {
    Reservation createReservation(ReservationRequest request);
    Reservation getReservation(Long id);
    List<Reservation> getReservationsByCustomer(String customerId);
    List<Reservation> getReservationsByFlight(String flightId);
    void cancelReservation(Long id);
} 