package com.flight_system.flight_service.repository;

import com.flight_system.flight_service.model.Route;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RouteRepository extends JpaRepository<Route, Long> {
    List<Route> findByOriginAirport(String originAirport);
    List<Route> findByDestinationAirport(String destinationAirport);
    List<Route> findByStatus(String status);
    Optional<Route> findByRouteCode(String routeCode);
    
    @Query("SELECT r FROM Route r WHERE r.originAirport = :origin AND r.destinationAirport = :destination")
    Optional<Route> findByOriginAndDestination(String origin, String destination);
    
    @Query("SELECT r FROM Route r WHERE r.distanceMiles <= :maxDistance")
    List<Route> findRoutesWithinDistance(Double maxDistance);
    
    boolean existsByRouteCode(String routeCode);
    boolean existsByOriginAirportAndDestinationAirport(String originAirport, String destinationAirport);
} 