package com.flight_system.check_in.repository;

import com.flight_system.check_in.model.CheckIn;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CheckInRepository extends JpaRepository<CheckIn, Long> {
    List<CheckIn> findByCustomerId(Long customerId);
} 