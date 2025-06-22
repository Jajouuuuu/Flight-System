package com.flight_system.check_in.controller;

import com.flight_system.check_in.dto.CheckInDTO;
import com.flight_system.check_in.model.BoardingPass;
import com.flight_system.check_in.model.CheckIn;
import com.flight_system.check_in.service.CheckInService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/check-ins")
@RequiredArgsConstructor
public class CheckInController {

    private final CheckInService checkInService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CheckIn createCheckIn(@RequestBody CheckInDTO checkInDTO) {
        return checkInService.createCheckIn(checkInDTO);
    }

    @GetMapping
    public List<CheckIn> getCheckInsByCustomerId(@RequestParam(required = false) Long customerId) {
        if (customerId != null) {
            return checkInService.getCheckInsByCustomerId(customerId);
        }
        // If no customerId is provided, we could return all check-ins,
        // but for now, we'll return an empty list to match the client's expectation.
        return List.of();
    }

    // Boarding Pass endpoints
    @PostMapping("/boarding-pass")
    public ResponseEntity<BoardingPass> createBoardingPass(@RequestBody BoardingPass boardingPassRequest) {
        BoardingPass boardingPass = checkInService.createBoardingPass(boardingPassRequest);
        return new ResponseEntity<>(boardingPass, HttpStatus.CREATED);
    }

    @GetMapping("/boarding-pass/{boardingPassNumber}")
    public ResponseEntity<BoardingPass> getBoardingPass(@PathVariable String boardingPassNumber) {
        BoardingPass boardingPass = checkInService.getBoardingPassByNumber(boardingPassNumber);
        return ResponseEntity.ok(boardingPass);
    }

    @GetMapping("/customer/{customerId}/boarding-passes")
    public ResponseEntity<List<BoardingPass>> getBoardingPassesByCustomer(@PathVariable Long customerId) {
        List<BoardingPass> boardingPasses = checkInService.getBoardingPassesByCustomerId(customerId);
        return ResponseEntity.ok(boardingPasses);
    }

    @GetMapping("/booking/{bookingNumber}/boarding-passes")
    public ResponseEntity<List<BoardingPass>> getBoardingPassesByBooking(@PathVariable String bookingNumber) {
        List<BoardingPass> boardingPasses = checkInService.getBoardingPassesByBookingNumber(bookingNumber);
        return ResponseEntity.ok(boardingPasses);
    }

    @GetMapping("/flight/{flightNumber}/boarding-passes")
    public ResponseEntity<List<BoardingPass>> getBoardingPassesByFlight(@PathVariable String flightNumber) {
        List<BoardingPass> boardingPasses = checkInService.getBoardingPassesByFlightNumber(flightNumber);
        return ResponseEntity.ok(boardingPasses);
    }

    @PutMapping("/boarding-pass/{boardingPassNumber}/status")
    public ResponseEntity<BoardingPass> updateBoardingPassStatus(
            @PathVariable String boardingPassNumber, 
            @RequestParam String status) {
        BoardingPass updatedBoardingPass = checkInService.updateBoardingPassStatus(boardingPassNumber, status);
        return ResponseEntity.ok(updatedBoardingPass);
    }

    @GetMapping("/boarding-passes")
    public ResponseEntity<List<BoardingPass>> getAllBoardingPasses() {
        List<BoardingPass> boardingPasses = checkInService.getAllBoardingPasses();
        return ResponseEntity.ok(boardingPasses);
    }

    @DeleteMapping("/boarding-pass/{boardingPassNumber}")
    public ResponseEntity<Void> deleteBoardingPass(@PathVariable String boardingPassNumber) {
        checkInService.deleteBoardingPass(boardingPassNumber);
        return ResponseEntity.noContent().build();
    }

    // Check if customer can check in (payment completed)
    @GetMapping("/booking/{bookingNumber}/can-checkin")
    public ResponseEntity<Boolean> canCheckIn(@PathVariable String bookingNumber) {
        boolean canCheckIn = checkInService.canCheckIn(bookingNumber);
        return ResponseEntity.ok(canCheckIn);
    }
} 