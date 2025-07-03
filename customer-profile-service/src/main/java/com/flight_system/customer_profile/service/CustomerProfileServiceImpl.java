package com.flight_system.customer_profile.service;

import com.flight_system.customer_profile.client.CustomerClient;
import com.flight_system.customer_profile.client.CheckInClient;
import com.flight_system.customer_profile.client.ReservationClient;
import com.flight_system.customer_profile.dto.*;
import com.flight_system.customer_profile.exceptions.CustomerProfileNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CustomerProfileServiceImpl implements CustomerProfileService {

    private final CustomerClient customerClient;
    private final ReservationClient reservationClient;
    private final CheckInClient checkInClient;

    @Override
    public CustomerProfileDTO getCustomerProfile(Long customerId) {
        try {
            // 1. Get Customer Details
            CustomerDTO customer = customerClient.getCustomerById(customerId);

            // 2. Get Reservations
            List<ReservationDTO> reservations = reservationClient.getReservationsByCustomerId(customerId);

            // 3. Get Check-ins
            List<CheckInDTO> checkIns = checkInClient.getCheckInsByCustomerId(customerId);

            return new CustomerProfileDTO(customer, reservations, checkIns);

        } catch (feign.FeignException.NotFound notFound) {
            throw new CustomerProfileNotFoundException("Customer profile not found for ID: " + customerId);
        }
    }

} 