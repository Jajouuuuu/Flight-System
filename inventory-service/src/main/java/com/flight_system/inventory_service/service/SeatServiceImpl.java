package com.flight_system.inventory_service.service;

import com.flight_system.inventory_service.exceptions.SeatAlreadyReservedException;
import com.flight_system.inventory_service.exceptions.SeatNotFoundException;
import com.flight_system.inventory_service.model.Seat;
import com.flight_system.inventory_service.repository.SeatRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SeatServiceImpl implements SeatService {

    private final SeatRepository seatRepository;

    @Override
    public List<Seat> getAvailableSeats(String flightNumber) {
        return seatRepository.findByInventoryFlightNumberAndAvailableTrue(flightNumber);
    }

    @Override
    public Seat reserveSeat(String flightNumber, String seatNumber) {
        Seat seat = seatRepository.findBySeatNumberAndInventoryFlightNumber(seatNumber, flightNumber)
                .orElseThrow(() ->
                        new SeatNotFoundException("Seat " + seatNumber + " not found for flight " + flightNumber));

        if (!seat.isAvailable()) {
            throw new SeatAlreadyReservedException("Seat " + seatNumber + " on flight " + flightNumber + " is already reserved");
        }

        seat.setAvailable(false);
        return seatRepository.save(seat);
    }

    @Override
    public int countAvailableSeats(String flightNumber, LocalDateTime departureDateTime) {
        if (departureDateTime.isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("Departure time must be in the future");
        }
        return seatRepository.countAvailableSeats(flightNumber, departureDateTime);
    }
}
