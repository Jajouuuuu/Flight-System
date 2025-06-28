package com.flight_system.reservation_service.service;

import com.flight_system.reservation_service.model.Reservation;

public interface ReservationService {
    Reservation createReservation(Reservation reservation);
    Reservation confirmReservation(String reservationNumber);
    Reservation cancelReservation(String reservationNumber);
    Reservation findByReservationNumber(String reservationNumber);
}
