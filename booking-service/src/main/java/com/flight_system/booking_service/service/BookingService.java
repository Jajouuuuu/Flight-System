package com.flight_system.booking_service.service;

import com.flight_system.booking_service.model.Booking;
import com.flight_system.booking_service.repository.BookingRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.client.circuitbreaker.CircuitBreaker;
import org.springframework.cloud.client.circuitbreaker.CircuitBreakerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class BookingService {
    private final BookingRepository bookingRepository;
    private final RestTemplate restTemplate;
    private final CircuitBreakerFactory circuitBreakerFactory;
    private final KafkaTemplate<String, Object> kafkaTemplate;

    @Transactional
    public Booking createBooking(Booking booking) {
        log.info("Creating booking: {}", booking);
        
        // Generate unique booking number
        booking.setBookingNumber("BK" + UUID.randomUUID().toString().substring(0, 8).toUpperCase());
        
        // Verify flight availability using circuit breaker
        CircuitBreaker flightCircuitBreaker = circuitBreakerFactory.create("flight-service");
        Map<String, Object> flightData = flightCircuitBreaker.run(
            () -> {
                try {
                    return restTemplate.getForObject(
                        "http://flight-service/api/flights/{id}",
                        Map.class,
                        booking.getFlightId()
                    );
                } catch (Exception e) {
                    log.error("Error verifying flight: {}", e.getMessage());
                    throw e;
                }
            },
            throwable -> {
                log.error("Circuit breaker fallback for flight service: {}", throwable.getMessage());
                throw new RuntimeException("Flight service is not available", throwable);
            }
        );

        if (flightData == null) {
            throw new EntityNotFoundException("Flight not found with id: " + booking.getFlightId());
        }

        // Verify customer using circuit breaker
        CircuitBreaker customerCircuitBreaker = circuitBreakerFactory.create("customer-service");
        Map<String, Object> customerData = customerCircuitBreaker.run(
            () -> {
                try {
                    return restTemplate.getForObject(
                        "http://customer-service/api/customers/{id}",
                        Map.class,
                        booking.getCustomerId()
                    );
                } catch (Exception e) {
                    log.error("Error verifying customer: {}", e.getMessage());
                    throw e;
                }
            },
            throwable -> {
                log.error("Circuit breaker fallback for customer service: {}", throwable.getMessage());
                throw new RuntimeException("Customer service is not available", throwable);
            }
        );

        if (customerData == null) {
            throw new EntityNotFoundException("Customer not found with id: " + booking.getCustomerId());
        }

        // Set initial status
        booking.setBookingStatus("PENDING");
        booking.setPaymentStatus("PENDING");

        // Save booking
        Booking savedBooking = bookingRepository.save(booking);
        log.info("Booking created successfully: {}", savedBooking);

        // Publish booking created event
        try {
            kafkaTemplate.send("booking-created", Map.of(
                "bookingId", savedBooking.getId(),
                "bookingNumber", savedBooking.getBookingNumber(),
                "customerId", savedBooking.getCustomerId(),
                "flightNumber", savedBooking.getFlightNumber(),
                "status", savedBooking.getBookingStatus()
            ));
        } catch (Exception e) {
            log.error("Error publishing booking created event: {}", e.getMessage());
            // Don't fail the booking creation if event publishing fails
        }

        return savedBooking;
    }

    @Transactional
    public Booking updateBooking(String bookingNumber, Booking booking) {
        Booking existingBooking = getBookingByNumber(bookingNumber);
        
        existingBooking.setPassengerName(booking.getPassengerName());
        existingBooking.setPassengerEmail(booking.getPassengerEmail());
        existingBooking.setSeatNumber(booking.getSeatNumber());
        existingBooking.setSpecialRequests(booking.getSpecialRequests());
        existingBooking.setBaggageAllowance(booking.getBaggageAllowance());
        existingBooking.setMealPreference(booking.getMealPreference());
        
        Booking updatedBooking = bookingRepository.save(existingBooking);

        // Publish booking updated event
        kafkaTemplate.send("booking-updated", Map.of(
            "bookingId", updatedBooking.getId(),
            "bookingNumber", updatedBooking.getBookingNumber(),
            "status", updatedBooking.getBookingStatus()
        ));

        return updatedBooking;
    }

    @Transactional
    public void confirmBooking(String bookingNumber) {
        Booking booking = getBookingByNumber(bookingNumber);
        booking.setBookingStatus("CONFIRMED");
        bookingRepository.save(booking);

        // Publish booking confirmed event
        kafkaTemplate.send("booking-confirmed", Map.of(
                "bookingId", booking.getId(),
                "bookingNumber", booking.getBookingNumber(),
                "customerId", booking.getCustomerId(),
                "flightNumber", booking.getFlightNumber(),
                "totalPrice", booking.getTotalPrice()
        ));
    }

    @Transactional
    public void cancelBooking(String bookingNumber) {
        Booking booking = getBookingByNumber(bookingNumber);
        booking.setBookingStatus("CANCELLED");
        bookingRepository.save(booking);

        // Publish booking cancelled event
        kafkaTemplate.send("booking-cancelled", Map.of(
            "bookingId", booking.getId(),
            "bookingNumber", booking.getBookingNumber(),
            "customerId", booking.getCustomerId(),
            "flightNumber", booking.getFlightNumber()
        ));
    }

    @Transactional(readOnly = true)
    public Booking getBookingByNumber(String bookingNumber) {
        return bookingRepository.findByBookingNumber(bookingNumber)
                .orElseThrow(() -> new EntityNotFoundException("Booking not found: " + bookingNumber));
    }

    @Transactional(readOnly = true)
    public List<Booking> getBookingsByCustomerId(Long customerId) {
        return bookingRepository.findByCustomerId(customerId);
    }

    @Transactional(readOnly = true)
    public List<Booking> getUpcomingBookings(Long customerId) {
        return bookingRepository.findUpcomingBookingsByCustomerId(customerId, LocalDateTime.now());
    }

    @Transactional(readOnly = true)
    public List<Booking> getBookingsByFlightNumber(String flightNumber) {
        return bookingRepository.findByFlightNumber(flightNumber);
    }

    @Transactional(readOnly = true)
    public Integer getConfirmedBookingsCount(Long flightId) {
        return bookingRepository.countConfirmedBookingsByFlightId(flightId);
    }

    @Transactional
    public void updatePaymentStatus(String bookingNumber, String paymentId, String status) {
        Booking booking = getBookingByNumber(bookingNumber);
        booking.setPaymentId(paymentId);
        booking.setPaymentStatus(status);
        
        if ("COMPLETED".equals(status)) {
            booking.setBookingStatus("CONFIRMED");
        } else if ("FAILED".equals(status)) {
            booking.setBookingStatus("CANCELLED");
        }
        
        bookingRepository.save(booking);

        // Publish payment status update event
        kafkaTemplate.send("booking-payment-updated", Map.of(
            "bookingNumber", bookingNumber,
            "paymentId", paymentId,
            "status", status
        ));
    }
} 