package com.flight_system.customer_profile.service;

import com.flight_system.customer_profile.dto.CustomerProfileDTO;

public interface CustomerProfileService {
    CustomerProfileDTO getCustomerProfile(Long customerId);
} 