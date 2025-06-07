package com.flight_system.flight_service.service;

import com.flight_system.flight_service.model.Aircraft;
import java.util.List;
import java.util.Optional;

public interface AircraftService {
    List<Aircraft> getAllAircraft();
    Optional<Aircraft> getAircraftById(Long id);
    Optional<Aircraft> getAircraftByRegistration(String registrationNumber);
    Aircraft createAircraft(Aircraft aircraft);
    Aircraft updateAircraft(Long id, Aircraft aircraft);
    void deleteAircraft(Long id);
    List<Aircraft> getAircraftByStatus(String status);
    List<Aircraft> getAircraftByManufacturer(String manufacturer);
    List<Aircraft> getAircraftByModel(String model);
} 