package com.flight_system.customer_profile.client;

import com.flight_system.customer_profile.dto.CheckInDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(name = "check-in-service", path = "/api/check-ins")
public interface CheckInClient {

    @GetMapping
    List<CheckInDTO> getCheckInsByCustomerId(@RequestParam("customerId") Long customerId);
} 