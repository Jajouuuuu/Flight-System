package com.flight_system.check_in.controller;

import com.flight_system.check_in.dto.CheckInDTO;
import com.flight_system.check_in.model.CheckIn;
import com.flight_system.check_in.service.CheckInService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
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
} 