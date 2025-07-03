package com.flight_system.booking_service.service;

import com.flight_system.booking_service.model.Booking;

import java.util.List;

public interface BookingService {
    Booking createBooking(Booking booking);
    Booking updateBooking(String bookingNumber, Booking booking);
    void confirmBooking(String bookingNumber);
    void cancelBooking(String bookingNumber);
    Booking getBookingByNumber(String bookingNumber);
    List<Booking> getBookingsByCustomerId(Long customerId);
    List<Booking> getUpcomingBookings(Long customerId);
    List<Booking> getBookingsByFlightNumber(String flightNumber);
    Integer getConfirmedBookingsCount(Long flightId);
    void updatePaymentStatus(String bookingNumber, String paymentId, String status);
}
