package com.flight_system.check_in.service;

import com.flight_system.check_in.dto.CheckInDTO;
import com.flight_system.check_in.exceptions.CheckInAlreadyExistsException;
import com.flight_system.check_in.model.CheckIn;
import com.flight_system.check_in.repository.CheckInRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CheckInServiceImpl implements CheckInService {

    private final CheckInRepository checkInRepository;

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
} 