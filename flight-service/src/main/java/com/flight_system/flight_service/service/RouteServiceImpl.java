package com.flight_system.flight_service.service;

import com.flight_system.flight_service.model.Route;
import com.flight_system.flight_service.repository.RouteRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RouteServiceImpl implements RouteService {
    private final RouteRepository repository;

    @Override
    public List<Route> getAllRoutes() {
        return repository.findAll();
    }

    @Override
    public Optional<Route> getRouteById(Long id) {
        return repository.findById(id);
    }

    @Override
    public Optional<Route> getRouteByCode(String routeCode) {
        return repository.findByRouteCode(routeCode);
    }

    @Override
    @Transactional
    public Route createRoute(Route route) {
        validateRoute(route);
        if (repository.existsByRouteCode(route.getRouteCode())) {
            throw new IllegalArgumentException("Route with code " + route.getRouteCode() + " already exists");
        }
        if (repository.existsByOriginAirportAndDestinationAirport(route.getOriginAirport(), route.getDestinationAirport())) {
            throw new IllegalArgumentException("Route from " + route.getOriginAirport() + " to " + route.getDestinationAirport() + " already exists");
        }
        return repository.save(route);
    }

    @Override
    @Transactional
    public Route updateRoute(Long id, Route updatedRoute) {
        validateRoute(updatedRoute);
        return repository.findById(id)
                .map(route -> {
                    // Check if the new route code conflicts with an existing one
                    if (!route.getRouteCode().equals(updatedRoute.getRouteCode()) &&
                            repository.existsByRouteCode(updatedRoute.getRouteCode())) {
                        throw new IllegalArgumentException("Route with code " + updatedRoute.getRouteCode() + " already exists");
                    }
                    
                    // Check if the new origin-destination pair conflicts with an existing route
                    if ((!route.getOriginAirport().equals(updatedRoute.getOriginAirport()) ||
                         !route.getDestinationAirport().equals(updatedRoute.getDestinationAirport())) &&
                            repository.existsByOriginAirportAndDestinationAirport(
                                updatedRoute.getOriginAirport(), 
                                updatedRoute.getDestinationAirport())) {
                        throw new IllegalArgumentException("Route from " + updatedRoute.getOriginAirport() + 
                                " to " + updatedRoute.getDestinationAirport() + " already exists");
                    }
                    
                    route.setOriginAirport(updatedRoute.getOriginAirport());
                    route.setDestinationAirport(updatedRoute.getDestinationAirport());
                    route.setDistanceMiles(updatedRoute.getDistanceMiles());
                    route.setEstimatedDurationMinutes(updatedRoute.getEstimatedDurationMinutes());
                    route.setBasePrice(updatedRoute.getBasePrice());
                    route.setStatus(updatedRoute.getStatus());
                    route.setRouteCode(updatedRoute.getRouteCode());
                    
                    return repository.save(route);
                })
                .orElseThrow(() -> new EntityNotFoundException("Route not found with id: " + id));
    }

    @Override
    @Transactional
    public void deleteRoute(Long id) {
        if (!repository.existsById(id)) {
            throw new EntityNotFoundException("Route not found with id: " + id);
        }
        repository.deleteById(id);
    }

    @Override
    public List<Route> getRoutesByOrigin(String originAirport) {
        return repository.findByOriginAirport(originAirport);
    }

    @Override
    public List<Route> getRoutesByDestination(String destinationAirport) {
        return repository.findByDestinationAirport(destinationAirport);
    }

    @Override
    public List<Route> getRoutesByStatus(String status) {
        return repository.findByStatus(status);
    }

    @Override
    public Optional<Route> findByOriginAndDestination(String origin, String destination) {
        return repository.findByOriginAndDestination(origin, destination);
    }

    @Override
    public List<Route> getRoutesWithinDistance(Double maxDistance) {
        return repository.findRoutesWithinDistance(maxDistance);
    }

    private void validateRoute(Route route) {
        if (route.getOriginAirport().equals(route.getDestinationAirport())) {
            throw new IllegalArgumentException("Origin and destination airports cannot be the same");
        }
        if (route.getDistanceMiles() <= 0) {
            throw new IllegalArgumentException("Distance must be greater than 0");
        }
        if (route.getEstimatedDurationMinutes() <= 0) {
            throw new IllegalArgumentException("Estimated duration must be greater than 0");
        }
        if (route.getBasePrice() <= 0) {
            throw new IllegalArgumentException("Base price must be greater than 0");
        }
    }
} 