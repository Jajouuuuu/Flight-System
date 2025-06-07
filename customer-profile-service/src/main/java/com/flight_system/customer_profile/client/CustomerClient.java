package com.flight_system.customer_profile.client;

import com.flight_system.customer_profile.dto.CustomerDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "customer-service", path = "/api/customers")
public interface CustomerClient {

    @GetMapping("/{id}")
    CustomerDTO getCustomerById(@PathVariable("id") Long id);
} 