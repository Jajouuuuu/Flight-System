package com.flight_system.inventory_service.service;

import com.flight_system.inventory_service.model.Inventory;
import com.flight_system.inventory_service.repository.InventoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class InventoryServiceImpl implements InventoryService {

    private final InventoryRepository repository;

    @Override
    public Inventory createInventory(Inventory inventory) {
        return repository.save(inventory);
    }

    @Override
    public List<Inventory> getAll() {
        return repository.findAll();
    }

    @Override
    public Optional<Inventory> getByFlightNumber(String flightNumber) {
        return repository.findByFlightNumber(flightNumber);
    }

    @Override
    public Inventory updateInventory(String flightNumber, int reservedSeats) {
        Inventory inv = repository.findByFlightNumber(flightNumber)
                .orElseThrow(() -> new RuntimeException("Le vol n'a pas été trouvé dans l'inventaire"));

        if (inv.getAvailableSeats() < reservedSeats) {
            throw new RuntimeException("Il n'y a plus de siège disponible");
        }

        inv.setAvailableSeats(inv.getAvailableSeats() - reservedSeats);
        return repository.save(inv);
    }
}

