package com.airline.service;

import com.airline.restClient.AnalyticsClient;
import com.airline.entity.*;
import com.airline.repository.BookingHistoryRepository;
import com.airline.repository.BookingRepository;
import com.airline.repository.FlightRepository;
import com.airline.repository.PlaneRepository;
import com.airline.restClient.NotificationClient;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Map;

@Service
public class FlightService {

    private static final Logger log = LogManager.getLogger(FlightService.class);

    @Autowired
    TaskScheduler taskScheduler;

    @Autowired
    FlightRepository flightRepository;

    @Autowired
    BookingHistoryRepository bookingHistoryRepository;

    @Autowired
    BookingRepository bookingRepository;

    @Autowired
    AnalyticsClient analyticsClient;

    @Autowired
    PlaneRepository planeRepository;

    @Autowired
    NotificationClient notificationClient;

    public Flight createFlight(Flight flight) {
        Plane plane = findByPlaneId(flight.getFlightId());
        flight.setFlightName(plane.getPlaneName());
        flight.setActualSeats(plane.getTotalSeats());

        List<BookingHistory> bookingHistories = bookingHistoryRepository.findByFlightId(flight.getFlightId());
        Map<String, Object> response = analyticsClient.predictSeats(bookingHistories);
        if (response.containsKey("error")) {
            log.info("No previous data available to calculate the overbooking limits");
            flight.setPredictedSeats(flight.getActualSeats());
        } else {
            Integer predictedSeats = (Integer) response.get("predictedSeats");
            log.info("Predicted total seat counts {}", predictedSeats);
            flight.setPredictedSeats(predictedSeats);
        }

        flight.setBookedSeats(0);
        flight.setTakeOffTime(LocalDateTime.now().plusHours(23));
        Flight savedFlight = flightRepository.save(flight);
        scheduleFlightLifecycle(savedFlight);
        return flightRepository.save(flight);
    }

    private void scheduleFlightLifecycle(Flight flight) {
        Instant runAt = flight.getTakeOffTime().atZone(ZoneId.systemDefault()).toInstant();
        taskScheduler.schedule(() ->
                handleFlightTakeoff(flight.getFlightId()), triggerContext -> runAt);
    }

    public void handleFlightTakeoff(Integer flightId) {
        Flight flight = findByFlightId(flightId);

        BookingHistory history = new BookingHistory();
        history.setFlightId(flightId);
        history.setActualSeats(flight.getActualSeats());
        history.setBookedSeats(flight.getBookedSeats());
        history.setFilledSeats(bookingRepository.countByFlightIdAndCheckInStatus(flightId, CheckInStatus.SHOW));
        history.setPredictedSeats(flight.getBookedSeats());
        history.setTakeOffTime(LocalDateTime.now());
        bookingHistoryRepository.save(history);

        // delete all bookings for this flight as it's ready to take off
        bookingRepository.deleteByFlightId(flightId);
        // delete the scheduled flight as it's ready to take off
        flightRepository.deleteById(flightId);

        if (flight.getActualSeats() < history.getFilledSeats()) {
            List<Booking> waitListPassengers = bookingRepository.findByBookingStatus(BookingStatus.WAITLIST);
            waitListPassengers.forEach(booking -> {
                booking.setBookingStatus(BookingStatus.CANCELLED);
                bookingRepository.save(booking);
            });
            log.info("Overbooked limit reached");
            notificationClient.sendEmail(waitListPassengers);
        }
    }

    public List<Flight> getAllFlights() {
        return flightRepository.findAll();
    }

    private Flight findByFlightId(Integer id) {
        return flightRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Flight not found"));
    }

    private Plane findByPlaneId(Integer id) {
        return planeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Plane not found"));
    }

    public Plane createPlane(Plane plane) {
        return planeRepository.save(plane);
    }
}
