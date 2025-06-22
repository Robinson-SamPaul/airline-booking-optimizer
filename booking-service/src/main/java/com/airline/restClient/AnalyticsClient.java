package com.airline.restClient;

import com.airline.entity.BookingHistory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.Map;

@FeignClient(name = "analytics-service", url = "http://localhost:8000")
public interface AnalyticsClient {

    @PostMapping("/process")
    Map<String, Object> predictSeats(@RequestBody List<BookingHistory> bookingHistories);
}