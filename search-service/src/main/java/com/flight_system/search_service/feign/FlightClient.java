package com.flight_system.search_service.feign;

import com.flight_system.search_service.dto.FlightDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@FeignClient(name = "flight-service", path = "/api/flights")
public interface FlightClient {

    @GetMapping("/search")
    List<FlightDTO> searchFlights(
            @RequestParam String origin,
            @RequestParam String destination,
            @RequestParam("departureTime") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime departureTime);
}