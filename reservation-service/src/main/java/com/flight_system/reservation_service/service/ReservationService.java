package com.flight_system.reservation_service.service;

import com.flight_system.reservation_service.model.Reservation;
import java.util.List;
import java.util.Map;

public interface ReservationService {
    Reservation createReservation(Reservation reservation);
    Reservation confirmReservation(String reservationNumber);
    Reservation cancelReservation(String reservationNumber);
    Reservation findByReservationNumber(String reservationNumber);
    List<Reservation> getAllReservations();
    List<Reservation> findByCustomerId(Long customerId);
    List<Reservation> findByFlightNumber(String flightNumber);
    List<Reservation> findByStatus(String status);
    Reservation updateReservation(String reservationNumber, Reservation updatedReservation);
    void deleteReservation(String reservationNumber);
    Reservation updateSeatCount(String reservationNumber, int newSeatCount);
    List<Reservation> searchReservations(String customerEmail, String flightNumber, String status);
} 
