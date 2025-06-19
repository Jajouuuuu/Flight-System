package com.flight_system.flight_service.controller;

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
        return routeService.getRouteById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/code/{routeCode}")
    public ResponseEntity<Route> getRouteByCode(@PathVariable String routeCode) {
        return routeService.getRouteByCode(routeCode)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
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
        return ResponseEntity.ok(routeService.getRoutesByOrigin(originAirport));
    }

    @GetMapping("/destination/{destinationAirport}")
    public ResponseEntity<List<Route>> getRoutesByDestination(@PathVariable String destinationAirport) {
        return ResponseEntity.ok(routeService.getRoutesByDestination(destinationAirport));
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<Route>> getRoutesByStatus(@PathVariable String status) {
        return ResponseEntity.ok(routeService.getRoutesByStatus(status));
    }

    @GetMapping("/search")
    public ResponseEntity<Route> findByOriginAndDestination(
            @RequestParam String origin,
            @RequestParam String destination) {
        return routeService.findByOriginAndDestination(origin, destination)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/within-distance")
    public ResponseEntity<List<Route>> getRoutesWithinDistance(@RequestParam Double maxDistance) {
        return ResponseEntity.ok(routeService.getRoutesWithinDistance(maxDistance));
    }
} 