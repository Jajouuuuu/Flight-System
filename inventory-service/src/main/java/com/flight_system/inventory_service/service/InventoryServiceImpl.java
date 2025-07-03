package com.flight_system.inventory_service.service;

import com.flight_system.inventory_service.exceptions.InsufficientSeatsException;
import com.flight_system.inventory_service.exceptions.InventoryNotFoundException;
import com.flight_system.inventory_service.exceptions.ReservationConflictException;
import com.flight_system.inventory_service.model.FlightInventory;
import com.flight_system.inventory_service.repository.FlightInventoryRepository;
import jakarta.persistence.OptimisticLockException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
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
                .orElseThrow(() -> new InventoryNotFoundException("Inventory not found for flight id: " + flightId));
    }

    @Override
    @Transactional
    public void reserveSeats(String flightNumber, int seatsToReserve) {
        FlightInventory inventory = inventoryRepository.findByFlightNumber(flightNumber)
                .orElseThrow(() -> new InventoryNotFoundException("Inventory not found for flight number: " + flightNumber));

        if (inventory.getAvailableSeats() < seatsToReserve) {
            throw new InsufficientSeatsException("Not enough seats to reserve " + seatsToReserve + " on flight " + flightNumber);
        }

        inventory.setAvailableSeats(inventory.getAvailableSeats() - seatsToReserve);
        inventory.setReservedSeats(inventory.getReservedSeats() + seatsToReserve);

        try {
            inventoryRepository.save(inventory);
        } catch (OptimisticLockException ex) {
            throw new ReservationConflictException("Concurrent seat reservation conflict on flight " + flightNumber);
        }
    }

    @Override
    @Transactional
    public void releaseSeats(String flightNumber, int seatsToRelease) {
        FlightInventory inventory = inventoryRepository.findByFlightNumber(flightNumber)
                .orElseThrow(() -> new InventoryNotFoundException("Inventory not found for flight number: " + flightNumber));

        inventory.setAvailableSeats(inventory.getAvailableSeats() + seatsToRelease);
        inventory.setReservedSeats(inventory.getReservedSeats() - seatsToRelease);

        inventoryRepository.save(inventory);
    }

    // New CRUD methods
    @Override
    @Transactional
    public FlightInventory createInventory(FlightInventory inventory) {
        log.info("Creating new inventory for flight: {}", inventory.getFlightNumber());
        
        // Set available seats equal to total seats if not already set
        if (inventory.getAvailableSeats() == null) {
            inventory.setAvailableSeats(inventory.getTotalSeats());
        }
        
        // Set reserved seats to 0 if not already set
        if (inventory.getReservedSeats() == null) {
            inventory.setReservedSeats(0);
        }
        
        return inventoryRepository.save(inventory);
    }

    @Override
    @Transactional(readOnly = true)
    public List<FlightInventory> getAllInventories() {
        return inventoryRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public FlightInventory getInventoryByFlightNumber(String flightNumber) {
        return inventoryRepository.findByFlightNumber(flightNumber)
                .orElseThrow(() -> new EntityNotFoundException("Inventory not found for flight number: " + flightNumber));
    }

    @Override
    @Transactional
    public FlightInventory updateInventory(Long inventoryId, FlightInventory inventory) {
        FlightInventory existingInventory = inventoryRepository.findById(inventoryId)
                .orElseThrow(() -> new EntityNotFoundException("Inventory not found with id: " + inventoryId));

        // Update fields
        existingInventory.setFlightNumber(inventory.getFlightNumber());
        existingInventory.setFlightId(inventory.getFlightId());
        existingInventory.setTotalSeats(inventory.getTotalSeats());
        existingInventory.setAvailableSeats(inventory.getAvailableSeats());
        existingInventory.setReservedSeats(inventory.getReservedSeats());

        return inventoryRepository.save(existingInventory);
    }

    @Override
    @Transactional
    public void deleteInventory(Long inventoryId) {
        if (!inventoryRepository.existsById(inventoryId)) {
            throw new EntityNotFoundException("Inventory not found with id: " + inventoryId);
        }
        inventoryRepository.deleteById(inventoryId);
    }

    @Override
    @Transactional(readOnly = true)
    public Integer getAvailableSeats(String flightNumber) {
        FlightInventory inventory = inventoryRepository.findByFlightNumber(flightNumber)
                .orElseThrow(() -> new EntityNotFoundException("Inventory not found for flight number: " + flightNumber));
        return inventory.getAvailableSeats();
    }
}
