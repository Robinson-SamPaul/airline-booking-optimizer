package com.airline;

import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Response;

import java.util.List;

@SuppressWarnings("unused")
@Path("/email")
public class EmailResource {

    @Inject
    MailService mailService;

    @POST
    @Path("/send")
    public Response sendTestEmail(List<Booking> bookings) {
        mailService.sendMail(bookings);
        return Response.ok("Sent!").build();
    }
}
