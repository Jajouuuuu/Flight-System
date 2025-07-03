package com.flight_system.check_in.service;

import com.flight_system.check_in.dto.CheckInDTO;
import com.flight_system.check_in.exceptions.CheckInAlreadyExistsException;
import com.flight_system.check_in.model.BoardingPass;
import com.flight_system.check_in.model.CheckIn;
import com.flight_system.check_in.repository.BoardingPassRepository;
import com.flight_system.check_in.repository.CheckInRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class CheckInServiceImpl implements CheckInService {

    private final CheckInRepository checkInRepository;
    private final BoardingPassRepository boardingPassRepository;
    private final KafkaTemplate<String, Object> kafkaTemplate;
    private final RestTemplate restTemplate;

    @Override
    public CheckIn createCheckIn(CheckInDTO checkInDTO) {
        boolean alreadyExists = checkInRepository.existsByBookingId(checkInDTO.getBookingId());

        if (alreadyExists) {
            throw new CheckInAlreadyExistsException("A check-in already exists for booking: " + checkInDTO.getBookingId());
        }

        CheckIn checkIn = new CheckIn();
        checkIn.setBookingId(checkInDTO.getBookingId());
        checkIn.setCustomerId(checkInDTO.getCustomerId());
        checkIn.setSeatNumber(checkInDTO.getSeatNumber());
        return checkInRepository.save(checkIn);
    }


    @Override
    public List<CheckIn> getCheckInsByCustomerId(Long customerId) {
        return checkInRepository.findByCustomerId(customerId);
    }

    // Boarding Pass methods
    @Override
    @Transactional
    public BoardingPass createBoardingPass(BoardingPass boardingPassRequest) {
        // Verify payment is completed for this booking
        if (!canCheckIn(boardingPassRequest.getBookingNumber())) {
            throw new IllegalStateException("Payment must be completed before creating boarding pass");
        }

        // Generate boarding pass number
        String boardingPassNumber = "BP-" + boardingPassRequest.getBookingNumber() + "-" + 
                                  UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        
        BoardingPass boardingPass = BoardingPass.builder()
                .boardingPassNumber(boardingPassNumber)
                .bookingNumber(boardingPassRequest.getBookingNumber())
                .customerId(boardingPassRequest.getCustomerId())
                .passengerName(boardingPassRequest.getPassengerName())
                .flightNumber(boardingPassRequest.getFlightNumber())
                .seatNumber(boardingPassRequest.getSeatNumber())
                .departureTime(boardingPassRequest.getDepartureTime())
                .departureGate(boardingPassRequest.getDepartureGate())
                .boardingTime(boardingPassRequest.getBoardingTime())
                .departureAirport(boardingPassRequest.getDepartureAirport())
                .arrivalAirport(boardingPassRequest.getArrivalAirport())
                .status("ACTIVE")
                .build();

        BoardingPass savedBoardingPass = boardingPassRepository.save(boardingPass);

        // Send boarding pass created event
        kafkaTemplate.send("boarding-pass-created", Map.of(
            "boardingPassNumber", savedBoardingPass.getBoardingPassNumber(),
            "bookingNumber", savedBoardingPass.getBookingNumber(),
            "flightNumber", savedBoardingPass.getFlightNumber(),
            "customerId", savedBoardingPass.getCustomerId(),
            "passengerName", savedBoardingPass.getPassengerName()
        ));

        log.info("Boarding pass created: {}", savedBoardingPass.getBoardingPassNumber());
        return savedBoardingPass;
    }

    @Override
    @Transactional(readOnly = true)
    public BoardingPass getBoardingPassByNumber(String boardingPassNumber) {
        return boardingPassRepository.findByBoardingPassNumber(boardingPassNumber)
                .orElseThrow(() -> new EntityNotFoundException("Boarding pass not found: " + boardingPassNumber));
    }

    @Override
    @Transactional(readOnly = true)
    public List<BoardingPass> getBoardingPassesByCustomerId(Long customerId) {
        return boardingPassRepository.findByCustomerId(customerId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<BoardingPass> getBoardingPassesByBookingNumber(String bookingNumber) {
        return boardingPassRepository.findByBookingNumber(bookingNumber);
    }

    @Override
    @Transactional(readOnly = true)
    public List<BoardingPass> getBoardingPassesByFlightNumber(String flightNumber) {
        return boardingPassRepository.findByFlightNumber(flightNumber);
    }

    @Override
    @Transactional
    public BoardingPass updateBoardingPassStatus(String boardingPassNumber, String status) {
        BoardingPass boardingPass = getBoardingPassByNumber(boardingPassNumber);
        boardingPass.setStatus(status);
        return boardingPassRepository.save(boardingPass);
    }

    @Override
    @Transactional(readOnly = true)
    public List<BoardingPass> getAllBoardingPasses() {
        return boardingPassRepository.findAll();
    }

    @Override
    @Transactional
    public void deleteBoardingPass(String boardingPassNumber) {
        BoardingPass boardingPass = getBoardingPassByNumber(boardingPassNumber);
        boardingPassRepository.delete(boardingPass);
    }

    @Override
    public boolean canCheckIn(String bookingNumber) {
        try {
            // Call payment service to check if payment is completed
            String paymentServiceUrl = "http://localhost:8080/api/v1/payments/booking/" + bookingNumber;
            Map<String, Object> paymentResponse = restTemplate.getForObject(paymentServiceUrl, Map.class);
            
            if (paymentResponse != null) {
                String paymentStatus = (String) paymentResponse.get("status");
                return "COMPLETED".equals(paymentStatus);
            }
            return false;
        } catch (Exception e) {
            log.warn("Could not verify payment status for booking {}: {}", bookingNumber, e.getMessage());
            return false;
        }
    }
} 