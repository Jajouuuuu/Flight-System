package com.flight_system.flight_service.controller;

import com.flight_system.flight_service.model.Aircraft;
import com.flight_system.flight_service.service.AircraftService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/aircraft")
@RequiredArgsConstructor
public class AircraftController {
    private final AircraftService aircraftService;

    @GetMapping
    public ResponseEntity<List<Aircraft>> getAllAircraft() {
        return ResponseEntity.ok(aircraftService.getAllAircraft());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Aircraft> getAircraftById(@PathVariable Long id) {
        return aircraftService.getAircraftById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/registration/{registrationNumber}")
    public ResponseEntity<Aircraft> getAircraftByRegistration(@PathVariable String registrationNumber) {
        return aircraftService.getAircraftByRegistration(registrationNumber)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
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
        return ResponseEntity.ok(aircraftService.getAircraftByStatus(status));
    }

    @GetMapping("/manufacturer/{manufacturer}")
    public ResponseEntity<List<Aircraft>> getAircraftByManufacturer(@PathVariable String manufacturer) {
        return ResponseEntity.ok(aircraftService.getAircraftByManufacturer(manufacturer));
    }

    @GetMapping("/model/{model}")
    public ResponseEntity<List<Aircraft>> getAircraftByModel(@PathVariable String model) {
        return ResponseEntity.ok(aircraftService.getAircraftByModel(model));
    }
} 