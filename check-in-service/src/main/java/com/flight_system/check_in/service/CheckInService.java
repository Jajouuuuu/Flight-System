package com.flight_system.check_in.service;

import com.flight_system.check_in.dto.CheckInDTO;
import com.flight_system.check_in.model.BoardingPass;
import com.flight_system.check_in.model.CheckIn;

import java.util.List;

public interface CheckInService {
    CheckIn createCheckIn(CheckInDTO checkInDTO);
    List<CheckIn> getCheckInsByCustomerId(Long customerId);
    
    // Boarding Pass methods
    BoardingPass createBoardingPass(BoardingPass boardingPassRequest);
    BoardingPass getBoardingPassByNumber(String boardingPassNumber);
    List<BoardingPass> getBoardingPassesByCustomerId(Long customerId);
    List<BoardingPass> getBoardingPassesByBookingNumber(String bookingNumber);
    List<BoardingPass> getBoardingPassesByFlightNumber(String flightNumber);
    BoardingPass updateBoardingPassStatus(String boardingPassNumber, String status);
    List<BoardingPass> getAllBoardingPasses();
    void deleteBoardingPass(String boardingPassNumber);
    
    // Check if customer can check in (payment completed)
    boolean canCheckIn(String bookingNumber);
} 