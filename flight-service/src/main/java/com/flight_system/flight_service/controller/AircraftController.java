package com.flight_system.flight_service.controller;

import com.flight_system.flight_service.exceptions.AircraftNotFoundException;
import com.flight_system.flight_service.model.Aircraft;
import com.flight_system.flight_service.service.AircraftService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/aircraft")
@RequiredArgsConstructor
public class AircraftController {
    private final AircraftService aircraftService;

    @GetMapping
    public ResponseEntity<List<Aircraft>> getAllAircraft() {
        return ResponseEntity.ok(aircraftService.getAllAircraft());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Aircraft> getAircraftById(@PathVariable Long id) {
        Aircraft aircraft = aircraftService.getAircraftById(id)
                .orElseThrow(() -> new AircraftNotFoundException("Aircraft not found with id: " + id));
        return ResponseEntity.ok(aircraft);
    }

    @GetMapping("/registration/{registrationNumber}")
    public ResponseEntity<Aircraft> getAircraftByRegistration(@PathVariable String registrationNumber) {
        Aircraft aircraft = aircraftService.getAircraftByRegistration(registrationNumber)
                .orElseThrow(() -> new AircraftNotFoundException("Aircraft not found with registration number: " + registrationNumber));
        return ResponseEntity.ok(aircraft);
    }

    @PostMapping
    public ResponseEntity<Aircraft> createAircraft(@Valid @RequestBody Aircraft aircraft) {
        return new ResponseEntity<>(aircraftService.createAircraft(aircraft), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Aircraft> updateAircraft(@PathVariable Long id, @Valid @RequestBody Aircraft aircraft) {
        return ResponseEntity.ok(aircraftService.updateAircraft(id, aircraft));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAircraft(@PathVariable Long id) {
        aircraftService.deleteAircraft(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<Aircraft>> getAircraftByStatus(@PathVariable String status) {
        List<Aircraft> aircrafts = aircraftService.getAircraftByStatus(status);
        if (aircrafts.isEmpty()) {
            throw new AircraftNotFoundException("No aircrafts found with status: " + status);
        }
        return ResponseEntity.ok(aircrafts);
    }

    @GetMapping("/manufacturer/{manufacturer}")
    public ResponseEntity<List<Aircraft>> getAircraftByManufacturer(@PathVariable String manufacturer) {
        List<Aircraft> aircrafts = aircraftService.getAircraftByManufacturer(manufacturer);
        if (aircrafts.isEmpty()) {
            throw new AircraftNotFoundException("No aircrafts found for manufacturer: " + manufacturer);
        }
        return ResponseEntity.ok(aircrafts);
    }

    @GetMapping("/model/{model}")
    public ResponseEntity<List<Aircraft>> getAircraftByModel(@PathVariable String model) {
        List<Aircraft> aircrafts = aircraftService.getAircraftByModel(model);
        if (aircrafts.isEmpty()) {
            throw new AircraftNotFoundException("No aircrafts found with model: " + model);
        }
        return ResponseEntity.ok(aircrafts);
    }
}
