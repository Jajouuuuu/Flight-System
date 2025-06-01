package com.flight_system.search_service.client;

import com.flight_system.search_service.dto.FlightDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.util.List;

@FeignClient(name = "flight-service")
public interface FlightClient {
    @GetMapping("/flights/search")
    List<FlightDTO> searchFlights(@RequestParam String origin, @RequestParam String destination, @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date);
}
