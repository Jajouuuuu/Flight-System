package com.flight_system.booking_service.repository;

import com.flight_system.booking_service.model.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {
    Optional<Booking> findByBookingNumber(String bookingNumber);
    List<Booking> findByCustomerId(Long customerId);
    List<Booking> findByFlightNumber(String flightNumber);
    List<Booking> findByBookingStatus(String status);
    List<Booking> findByPaymentStatus(String status);
    
    @Query("SELECT b FROM Booking b WHERE b.customerId = :customerId AND b.departureTime >= :now ORDER BY b.departureTime")
    List<Booking> findUpcomingBookingsByCustomerId(Long customerId, LocalDateTime now);
    
    @Query("SELECT b FROM Booking b WHERE b.flightId = :flightId AND b.bookingStatus = 'CONFIRMED'")
    List<Booking> findConfirmedBookingsByFlightId(Long flightId);
    
    @Query("SELECT COUNT(b) FROM Booking b WHERE b.flightId = :flightId AND b.bookingStatus = 'CONFIRMED'")
    Integer countConfirmedBookingsByFlightId(Long flightId);
    
    @Query("SELECT b FROM Booking b WHERE b.departureTime BETWEEN :start AND :end")
    List<Booking> findBookingsInTimeRange(LocalDateTime start, LocalDateTime end);
    
    boolean existsByFlightIdAndSeatNumber(Long flightId, String seatNumber);
} 