package com.flight_system.flight_service.controller;

import com.flight_system.flight_service.exceptions.RouteNotFoundException;
import com.flight_system.flight_service.model.Route;
import com.flight_system.flight_service.service.RouteService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/routes")
@RequiredArgsConstructor
public class RouteController {
    private final RouteService routeService;

    @GetMapping
    public ResponseEntity<List<Route>> getAllRoutes() {
        return ResponseEntity.ok(routeService.getAllRoutes());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Route> getRouteById(@PathVariable Long id) {
        Route route = routeService.getRouteById(id)
                .orElseThrow(() -> new RouteNotFoundException("Route not found with id: " + id));
        return ResponseEntity.ok(route);
    }

    @GetMapping("/code/{routeCode}")
    public ResponseEntity<Route> getRouteByCode(@PathVariable String routeCode) {
        Route route = routeService.getRouteByCode(routeCode)
                .orElseThrow(() -> new RouteNotFoundException("Route not found with code: " + routeCode));
        return ResponseEntity.ok(route);
    }

    @PostMapping
    public ResponseEntity<Route> createRoute(@Valid @RequestBody Route route) {
        return new ResponseEntity<>(routeService.createRoute(route), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Route> updateRoute(@PathVariable Long id, @Valid @RequestBody Route route) {
        return ResponseEntity.ok(routeService.updateRoute(id, route));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRoute(@PathVariable Long id) {
        routeService.deleteRoute(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/origin/{originAirport}")
    public ResponseEntity<List<Route>> getRoutesByOrigin(@PathVariable String originAirport) {
        List<Route> routes = routeService.getRoutesByOrigin(originAirport);
        if (routes.isEmpty()) {
            throw new RouteNotFoundException("No routes found from origin: " + originAirport);
        }
        return ResponseEntity.ok(routes);
    }

    @GetMapping("/destination/{destinationAirport}")
    public ResponseEntity<List<Route>> getRoutesByDestination(@PathVariable String destinationAirport) {
        List<Route> routes = routeService.getRoutesByDestination(destinationAirport);
        if (routes.isEmpty()) {
            throw new RouteNotFoundException("No routes found to destination: " + destinationAirport);
        }
        return ResponseEntity.ok(routes);
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<Route>> getRoutesByStatus(@PathVariable String status) {
        List<Route> routes = routeService.getRoutesByStatus(status);
        if (routes.isEmpty()) {
            throw new RouteNotFoundException("No routes found with status: " + status);
        }
        return ResponseEntity.ok(routes);
    }

    @GetMapping("/search")
    public ResponseEntity<Route> findByOriginAndDestination(
            @RequestParam String origin,
            @RequestParam String destination) {
        Route route = routeService.findByOriginAndDestination(origin, destination)
                .orElseThrow(() -> new RouteNotFoundException("No route found from " + origin + " to " + destination));
        return ResponseEntity.ok(route);
    }

    @GetMapping("/distance")
    public ResponseEntity<List<Route>> getRoutesWithinDistance(@RequestParam Double maxDistance) {
        List<Route> routes = routeService.getRoutesWithinDistance(maxDistance);
        if (routes.isEmpty()) {
            throw new RouteNotFoundException("No routes found within distance: " + maxDistance + " miles");
        }
        return ResponseEntity.ok(routes);
    }
}
