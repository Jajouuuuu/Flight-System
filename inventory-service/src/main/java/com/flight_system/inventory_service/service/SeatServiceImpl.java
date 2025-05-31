package com.flight_system.inventory_service.service;

import com.flight_system.inventory_service.model.Seat;
import com.flight_system.inventory_service.repository.SeatRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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
                .orElseThrow(() -> new RuntimeException("Seat not found"));

        if (!seat.isAvailable()) {
            throw new RuntimeException("Seat already reserved");
        }

        seat.setAvailable(false);
        return seatRepository.save(seat);
    }
}

