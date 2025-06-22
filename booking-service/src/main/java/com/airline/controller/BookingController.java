package com.airline.controller;

import com.airline.entity.Booking;
import com.airline.service.BookingService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@SuppressWarnings("unused")
@RestController
@RequestMapping("/booking")
public class BookingController {

    @Autowired
    private BookingService bookingService;

    @PostMapping
    public Booking createBooking(@RequestBody Booking booking) {
        return bookingService.createBooking(booking);
    }

    @GetMapping
    public List<Booking> getAllBookings() {
        return bookingService.getAllBookings();
    }

    @GetMapping("/flight/{id}")
    public List<Booking> getAllBookingsByFlightId(@PathVariable Integer id) {
        return bookingService.getBookingByFlightId(id);
    }

    @GetMapping("/{id}")
    public Booking getBookingById(@PathVariable Integer id) {
        return bookingService.getBookingById(id);
    }

    @PatchMapping("/check-in/{id}")
    public void updateCheckInStatus(@PathVariable Integer id) {
        bookingService.updateCheckInStatus(id);
    }

    @DeleteMapping("/{id}")
    public void deleteBooking(@PathVariable Integer id) {
        bookingService.deleteBooking(id);
    }

    @DeleteMapping("/")
    public void deleteAllBooking() {
        bookingService.deleteAllBookings();
    }
}
