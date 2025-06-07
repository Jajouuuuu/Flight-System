package com.flight_system.flight_service.repository;

import com.flight_system.flight_service.model.Aircraft;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AircraftRepository extends JpaRepository<Aircraft, Long> {
    Optional<Aircraft> findByRegistrationNumber(String registrationNumber);
    List<Aircraft> findByStatus(String status);
    boolean existsByRegistrationNumber(String registrationNumber);
    List<Aircraft> findByManufacturer(String manufacturer);
    List<Aircraft> findByModel(String model);
} 