package com.airline.repository;

import com.airline.entity.Booking;
import com.airline.entity.BookingStatus;
import com.airline.entity.CheckInStatus;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Integer> {

    List<Booking> findByFlightId(Integer flightId);

    int countByFlightIdAndCheckInStatus(Integer flightId, CheckInStatus checkInStatus);

    Optional<Booking> findTopByBookingStatusOrderByBookedTimeAsc(BookingStatus bookingStatus);

    void deleteByFlightId(Integer flightId);

    List<Booking> findByBookingStatus(BookingStatus bookingStatus);
}
