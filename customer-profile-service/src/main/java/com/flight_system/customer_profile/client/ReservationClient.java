package com.flight_system.customer_profile.client;

import com.flight_system.customer_profile.dto.ReservationDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(name = "reservation-service", path = "/api/reservations")
public interface ReservationClient {

    @GetMapping
    List<ReservationDTO> getReservationsByCustomerId(@RequestParam("customerId") Long customerId);
} 