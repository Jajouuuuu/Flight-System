package com.flight_system.flight_service.service;

import com.flight_system.flight_service.model.Aircraft;
import com.flight_system.flight_service.repository.AircraftRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AircraftServiceImpl implements AircraftService {
    private final AircraftRepository repository;

    @Override
    public List<Aircraft> getAllAircraft() {
        return repository.findAll();
    }

    @Override
    public Optional<Aircraft> getAircraftById(Long id) {
        return repository.findById(id);
    }

    @Override
    public Optional<Aircraft> getAircraftByRegistration(String registrationNumber) {
        return repository.findByRegistrationNumber(registrationNumber);
    }

    @Override
    @Transactional
    public Aircraft createAircraft(Aircraft aircraft) {
        if (repository.existsByRegistrationNumber(aircraft.getRegistrationNumber())) {
            throw new IllegalArgumentException("Aircraft with registration number " + aircraft.getRegistrationNumber() + " already exists");
        }
        return repository.save(aircraft);
    }

    @Override
    @Transactional
    public Aircraft updateAircraft(Long id, Aircraft updatedAircraft) {
        return repository.findById(id)
                .map(aircraft -> {
                    // Don't update registration number if it would conflict with another aircraft
                    if (!aircraft.getRegistrationNumber().equals(updatedAircraft.getRegistrationNumber()) &&
                            repository.existsByRegistrationNumber(updatedAircraft.getRegistrationNumber())) {
                        throw new IllegalArgumentException("Aircraft with registration number " + updatedAircraft.getRegistrationNumber() + " already exists");
                    }
                    
                    aircraft.setRegistrationNumber(updatedAircraft.getRegistrationNumber());
                    aircraft.setModel(updatedAircraft.getModel());
                    aircraft.setManufacturer(updatedAircraft.getManufacturer());
                    aircraft.setTotalSeats(updatedAircraft.getTotalSeats());
                    aircraft.setManufacturingYear(updatedAircraft.getManufacturingYear());
                    aircraft.setStatus(updatedAircraft.getStatus());
                    aircraft.setLastMaintenanceDate(updatedAircraft.getLastMaintenanceDate());
                    aircraft.setNextMaintenanceDate(updatedAircraft.getNextMaintenanceDate());
                    
                    return repository.save(aircraft);
                })
                .orElseThrow(() -> new EntityNotFoundException("Aircraft not found with id: " + id));
    }

    @Override
    @Transactional
    public void deleteAircraft(Long id) {
        if (!repository.existsById(id)) {
            throw new EntityNotFoundException("Aircraft not found with id: " + id);
        }
        repository.deleteById(id);
    }

    @Override
    public List<Aircraft> getAircraftByStatus(String status) {
        return repository.findByStatus(status);
    }

    @Override
    public List<Aircraft> getAircraftByManufacturer(String manufacturer) {
        return repository.findByManufacturer(manufacturer);
    }

    @Override
    public List<Aircraft> getAircraftByModel(String model) {
        return repository.findByModel(model);
    }
} 