package com.flight_system.check_in.repository;

import com.flight_system.check_in.model.BoardingPass;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BoardingPassRepository extends JpaRepository<BoardingPass, Long> {
    Optional<BoardingPass> findByBoardingPassNumber(String boardingPassNumber);
    List<BoardingPass> findByBookingNumber(String bookingNumber);
    List<BoardingPass> findByFlightNumber(String flightNumber);
    List<BoardingPass> findByCustomerId(Long customerId);
    List<BoardingPass> findByPassengerName(String passengerName);
    Optional<BoardingPass> findByBookingNumberAndCustomerId(String bookingNumber, Long customerId);
    List<BoardingPass> findByStatus(String status);
} 