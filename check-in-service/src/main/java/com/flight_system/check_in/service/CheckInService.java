package com.flight_system.check_in.service;

import com.flight_system.check_in.dto.CheckInDTO;
import com.flight_system.check_in.model.CheckIn;

import java.util.List;

public interface CheckInService {
    CheckIn createCheckIn(CheckInDTO checkInDTO);
    List<CheckIn> getCheckInsByCustomerId(Long customerId);
} 