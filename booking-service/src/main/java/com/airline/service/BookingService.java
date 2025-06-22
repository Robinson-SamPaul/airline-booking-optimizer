package com.airline.service;

import com.airline.entity.Booking;
import com.airline.entity.BookingStatus;
import com.airline.entity.CheckInStatus;
import com.airline.entity.Flight;
import com.airline.repository.BookingRepository;
import com.airline.repository.FlightRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Service
public class BookingService {

    @Autowired
    BookingRepository bookingRepository;

    @Autowired
    FlightRepository flightRepository;

    public Booking createBooking(Booking booking) {
        Integer flightId = booking.getFlightId();
        Flight flight = flightRepository.findById(flightId)
                .orElseThrow(() -> new RuntimeException("Flight not found with ID: " + flightId));

        int booked = flight.getBookedSeats();
        int actual = flight.getActualSeats();
        int total = flight.getPredictedSeats();

        if (flight.getTakeOffTime().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("boarding has already started. Booking is no longer allowed.");
        }

        if (booked <= actual) {
            booking.setBookingStatus(BookingStatus.CONFIRMED);
            flight.setBookedSeats(booked + 1);
        } else if (booked <= total) {
            booking.setBookingStatus(BookingStatus.WAITLIST);
            flight.setBookedSeats(booked + 1);
        } else {
            throw new RuntimeException("All seats (including waitlist) are full. Booking not allowed.");
        }

        flightRepository.save(flight);
        booking.setBookedTime(LocalTime.now());
        booking.setCheckInStatus(CheckInStatus.NO_SHOW);
        return bookingRepository.save(booking);
    }

    public Booking getBookingById(Integer id) {
        return bookingRepository.findById(id).orElseThrow(
                () -> new RuntimeException("Booking not found"));
    }

    public List<Booking> getBookingByFlightId(Integer id) {
        return bookingRepository.findByFlightId(id);
    }

    public void deleteBooking(Integer id) {
        Booking booking =  getBookingById(id);
        Flight flight = flightRepository.findById(booking.getFlightId()).orElseThrow(
                () -> new RuntimeException("Flight not found with ID: " + booking.getFlightId()));

        booking.setBookingStatus(BookingStatus.CANCELLED);
        booking.setCheckInStatus(CheckInStatus.NO_SHOW);
        booking.setBookedTime(LocalTime.now());
        bookingRepository.save(booking);

        int bookedSeats = flight.getBookedSeats();
        flight.setBookedSeats(bookedSeats - 1);
        flightRepository.save(flight);

        Optional<Booking> opsWaitListBooking = getRecentWaitList();
        if(opsWaitListBooking.isPresent()) {
            Booking waitListBooking = opsWaitListBooking.get();
            waitListBooking.setBookingStatus(BookingStatus.CONFIRMED);
            bookingRepository.save(waitListBooking);
        }
    }

    private Optional<Booking> getRecentWaitList() {
        return bookingRepository.findTopByBookingStatusOrderByBookedTimeAsc(BookingStatus.WAITLIST);
    }

    public List<Booking> getAllBookings() {
        return bookingRepository.findAll();
    }

    public void deleteAllBookings() {
        bookingRepository.deleteAll();
    }

    public void updateCheckInStatus(Integer id) {
        Booking bookingById = getBookingById(id);
        bookingById.setCheckInStatus(CheckInStatus.SHOW);
        bookingRepository.save(bookingById);
    }
}
