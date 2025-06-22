package com.airline;

import lombok.Data;

import java.time.LocalTime;

@Data
public class Booking {

    private Integer id;
    private String name;
    private String mail;
    private Integer flightId;
    private BookingStatus bookingStatus;
    private CheckInStatus checkInStatus;
    private LocalTime bookedTime;
}
