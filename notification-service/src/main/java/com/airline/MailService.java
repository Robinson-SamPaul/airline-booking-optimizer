package com.airline;

import io.netty.util.internal.StringUtil;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Objects;
import java.util.Properties;

import static com.airline.Utils.*;

@Slf4j
@ApplicationScoped
public class MailService {

    private Session session;

    @PostConstruct
    public void init() {
        Properties PROPERTIES = new Properties();
        PROPERTIES.put(MAIL_SMTP_AUTH, SENDER_AUTH);
        PROPERTIES.put(MAIL_SMTP_START_TLS, SENDER_START_TLS);
        PROPERTIES.put(MAIL_SMTP_HOST, SENDER_HOST);
        PROPERTIES.put(MAIL_SMTP_PORT, SENDER_PORT);

        session = Session.getInstance(PROPERTIES,
                new Authenticator() {
                    @Override
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(SENDER_USER_NAME, SENDER_PASSWORD);
                    }
                });
    }

    public void sendMail(List<Booking> bookingList) {
        bookingList.forEach(booking -> {
            String receiverMailId = booking.getMail();
            sendMessage(booking, receiverMailId);
        });
    }

    private void sendMessage(Booking booking, String receiverMailId) {
        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(SENDER_MAIL_ID));
            message.setRecipient(Message.RecipientType.TO, new InternetAddress(receiverMailId));
            message.setSubject("Your Booking Has Been Cancelled");
            String mailBody = getMailBody(booking);
            message.setText(mailBody);
            Transport.send(message);
        } catch (MessagingException e) {
            throw new RuntimeException("Email not sent " + e.getMessage());
        }
    }

    private static String getMailBody(Booking booking) {
        String passengerName = StringUtil.isNullOrEmpty(booking.getName()) ? "Passenger" : booking.getName();
        Integer flightId = Objects.isNull(booking.getFlightId()) ? 1000 : booking.getFlightId();
        return String.format("""
            Dear %s,
            
            We regret to inform you that your booking for flight %d has been cancelled.
            Unfortunately, all seats for this flight have been filled, and we were unable to confirm your seat from the waitlist.
            We sincerely apologize for the inconvenience. Please consider rebooking on an alternate flight or contact our support team for assistance.
            Thank you for your understanding.
            
            Best regards,
            Airline Customer Service
            """, passengerName, flightId);
    }
}
