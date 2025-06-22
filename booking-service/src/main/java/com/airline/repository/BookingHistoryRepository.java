package com.airline.repository;

import com.airline.entity.BookingHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookingHistoryRepository extends JpaRepository<BookingHistory, Integer> {
    List<BookingHistory> findByFlightId(Integer flightId);
}
