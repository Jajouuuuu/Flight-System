package com.flight_system.flight_service.service;

import com.flight_system.flight_service.model.Route;
import java.util.List;
import java.util.Optional;

public interface RouteService {
    List<Route> getAllRoutes();
    Optional<Route> getRouteById(Long id);
    Optional<Route> getRouteByCode(String routeCode);
    Route createRoute(Route route);
    Route updateRoute(Long id, Route route);
    void deleteRoute(Long id);
    List<Route> getRoutesByOrigin(String originAirport);
    List<Route> getRoutesByDestination(String destinationAirport);
    List<Route> getRoutesByStatus(String status);
    Optional<Route> findByOriginAndDestination(String origin, String destination);
    List<Route> getRoutesWithinDistance(Double maxDistance);
} 