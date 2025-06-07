package com.flight_system.customer_profile.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CustomerProfileDTO {
    private CustomerDTO customer;
    private List<ReservationDTO> reservations;
    private List<CheckInDTO> checkIns;
} 