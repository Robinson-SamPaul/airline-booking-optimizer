package com.airline.restClient;

import com.airline.entity.Booking;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@RequestMapping("/email")
@FeignClient(name = "notification-service", url = "http://localhost:8084")
public interface NotificationClient {

    @PostMapping("/send")
    void sendEmail(List<Booking> waitListPassengers);
}