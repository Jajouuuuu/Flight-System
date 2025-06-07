package com.flight_system.booking_service.controller;

import com.flight_system.booking_service.model.Booking;
import com.flight_system.booking_service.service.BookingService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/bookings")
@RequiredArgsConstructor
public class BookingController {
    private final BookingService bookingService;

    @PostMapping
    public ResponseEntity<Booking> createBooking(@Valid @RequestBody Booking booking) {
        return new ResponseEntity<>(bookingService.createBooking(booking), HttpStatus.CREATED);
    }

    @GetMapping("/{bookingNumber}")
    public ResponseEntity<Booking> getBooking(@PathVariable String bookingNumber) {
        return ResponseEntity.ok(bookingService.getBookingByNumber(bookingNumber));
    }

    @PutMapping("/{bookingNumber}")
    public ResponseEntity<Booking> updateBooking(
            @PathVariable String bookingNumber,
            @Valid @RequestBody Booking booking) {
        return ResponseEntity.ok(bookingService.updateBooking(bookingNumber, booking));
    }

    @PostMapping("/{bookingNumber}/confirm")
    public ResponseEntity<Void> confirmBooking(@PathVariable String bookingNumber) {
        bookingService.confirmBooking(bookingNumber);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{bookingNumber}/cancel")
    public ResponseEntity<Void> cancelBooking(@PathVariable String bookingNumber) {
        bookingService.cancelBooking(bookingNumber);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/customer/{customerId}")
    public ResponseEntity<List<Booking>> getCustomerBookings(@PathVariable Long customerId) {
        return ResponseEntity.ok(bookingService.getBookingsByCustomerId(customerId));
    }

    @GetMapping("/customer/{customerId}/upcoming")
    public ResponseEntity<List<Booking>> getUpcomingBookings(@PathVariable Long customerId) {
        return ResponseEntity.ok(bookingService.getUpcomingBookings(customerId));
    }

    @GetMapping("/flight/{flightNumber}")
    public ResponseEntity<List<Booking>> getFlightBookings(@PathVariable String flightNumber) {
        return ResponseEntity.ok(bookingService.getBookingsByFlightNumber(flightNumber));
    }

    @GetMapping("/flight/{flightId}/count")
    public ResponseEntity<Integer> getConfirmedBookingsCount(@PathVariable Long flightId) {
        return ResponseEntity.ok(bookingService.getConfirmedBookingsCount(flightId));
    }

    @PostMapping("/{bookingNumber}/payment")
    public ResponseEntity<Void> updatePaymentStatus(
            @PathVariable String bookingNumber,
            @RequestParam String paymentId,
            @RequestParam String status) {
        bookingService.updatePaymentStatus(bookingNumber, paymentId, status);
        return ResponseEntity.ok().build();
    }
} 