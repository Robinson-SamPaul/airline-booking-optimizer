package com.airline.controller;

import com.airline.entity.Booking;
import com.airline.entity.Flight;
import com.airline.entity.Plane;
import com.airline.service.FlightService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@SuppressWarnings("unused")
@RestController
@RequestMapping("/flight")
public class FlightController {

    @Autowired
    private FlightService flightService;

    @PostMapping("/schedule")
    public Flight createFlight(@RequestBody Flight flight) {
        return flightService.createFlight(flight);
    }

    @PostMapping("/create")
    public Plane createPlane(@RequestBody Plane plane) {
        return flightService.createPlane(plane);
    }

    @GetMapping
    public List<Flight> getAllFlights() {
        return flightService.getAllFlights();
    }
}
