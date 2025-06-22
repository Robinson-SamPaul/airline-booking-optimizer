package com.airline.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Entity
public class Flight {

    @Id
    private Integer flightId;
    private String flightName;
    private int actualSeats;
    private int predictedSeats;
    private int bookedSeats;
    private LocalDateTime takeOffTime;
}

