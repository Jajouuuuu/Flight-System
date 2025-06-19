package com.flight_system.customer_profile.controller;

import com.flight_system.customer_profile.dto.CustomerProfileDTO;
import com.flight_system.customer_profile.service.CustomerProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/profile")
@RequiredArgsConstructor
public class CustomerProfileController {

    private final CustomerProfileService customerProfileService;

    @GetMapping("/{customerId}")
    public CustomerProfileDTO getCustomerProfile(@PathVariable Long customerId) {
        return customerProfileService.getCustomerProfile(customerId);
    }
} 