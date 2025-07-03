package com.flight_system.search_service.feign;

import com.flight_system.search_service.dto.InventoryDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "inventory-service", path = "/api/v1/inventory")
public interface InventoryClient {

    @GetMapping("/flight/{flightId}")
    InventoryDTO getInventoryByFlightId(@PathVariable Long flightId);
} 