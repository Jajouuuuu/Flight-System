package com.flight_system.inventory_service.service;

import com.flight_system.inventory_service.model.FlightInventory;
import com.flight_system.inventory_service.repository.FlightInventoryRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.OptimisticLockException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class InventoryServiceImpl implements InventoryService {
    private final FlightInventoryRepository inventoryRepository;

    @Override
    @KafkaListener(topics = "flight-created", groupId = "inventory-service")
    @Transactional
    public void handleFlightCreated(Map<String, Object> flightData) {
        log.info("Received flight created event: {}", flightData);
        Long flightId = Long.valueOf(flightData.get("flightId").toString());
        String flightNumber = (String) flightData.get("flightNumber");
        Integer totalSeats = (Integer) flightData.get("totalSeats");

        FlightInventory inventory = FlightInventory.builder()
                .flightId(flightId)
                .flightNumber(flightNumber)
                .totalSeats(totalSeats)
                .availableSeats(totalSeats)
                .reservedSeats(0)
                .build();
        inventoryRepository.save(inventory);
    }

    @Override
    @KafkaListener(topics = "flight-cancelled", groupId = "inventory-service")
    @Transactional
    public void handleFlightCancelled(Map<String, Object> flightData) {
        log.info("Received flight cancelled event: {}", flightData);
        Long flightId = Long.valueOf(flightData.get("flightId").toString());
        inventoryRepository.findByFlightId(flightId).ifPresent(inventoryRepository::delete);
    }

    @Override
    @Transactional(readOnly = true)
    public FlightInventory getInventoryByFlightId(Long flightId) {
        return inventoryRepository.findByFlightId(flightId)
                .orElseThrow(() -> new EntityNotFoundException("Inventory not found for flight id: " + flightId));
    }

    @Override
    @Transactional
    public void reserveSeats(String flightNumber, int seatsToReserve) {
        FlightInventory inventory = inventoryRepository.findByFlightNumber(flightNumber)
                .orElseThrow(() -> new EntityNotFoundException("Inventory not found for flight number: " + flightNumber));

        if (inventory.getAvailableSeats() < seatsToReserve) {
            throw new RuntimeException("Not enough available seats");
        }

        inventory.setAvailableSeats(inventory.getAvailableSeats() - seatsToReserve);
        inventory.setReservedSeats(inventory.getReservedSeats() + seatsToReserve);

        try {
            inventoryRepository.save(inventory);
        } catch (OptimisticLockException ex) {
            throw new RuntimeException("Seat reservation conflict, please try again.", ex);
        }
    }

    @Override
    @Transactional
    public void releaseSeats(String flightNumber, int seatsToRelease) {
        FlightInventory inventory = inventoryRepository.findByFlightNumber(flightNumber)
                .orElseThrow(() -> new EntityNotFoundException("Inventory not found for flight number: " + flightNumber));

        inventory.setAvailableSeats(inventory.getAvailableSeats() + seatsToRelease);
        inventory.setReservedSeats(inventory.getReservedSeats() - seatsToRelease);

        inventoryRepository.save(inventory);
    }
}

