package com.fun.bookMyShow.service;

import com.fun.bookMyShow.Model.Booking;
import com.fun.bookMyShow.Model.ShowSeat;
import com.fun.bookMyShow.Model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class EmailService {

    private static final Logger log = LoggerFactory.getLogger(EmailService.class);
    private static final DateTimeFormatter SHOW_TIME_FORMATTER = DateTimeFormatter.ofPattern("dd MMM yyyy, hh:mm a");

    @Autowired(required = false)
    private JavaMailSender mailSender;

    @Value("${mailing.enabled:true}")
    private boolean mailingEnabled;

    @Value("${mailing.from:${spring.mail.username:no-reply@bookmyshow.local}}")
    private String fromAddress;

    @Async
    public void sendWelcomeMail(User user) {
        if (user == null || user.getEmail() == null || user.getEmail().isBlank()) {
            return;
        }

        String subject = "Welcome to BookMyShow";
        String body = "Hi " + safe(user.getName()) + ",\n\n"
                + "Your account is ready.\n"
                + "You can now book tickets on BookMyShow.\n\n"
                + "Regards,\nBookMyShow Team";
        sendMail(user.getEmail(), subject, body);
    }

    @Async
    public void sendPasswordResetMail(User user, String resetUrl) {
        if (user == null || user.getEmail() == null || user.getEmail().isBlank()) {
            return;
        }

        String subject = "Reset your BookMyShow password";
        String body = "Hi " + safe(user.getName()) + ",\n\n"
                + "We received a request to reset your password.\n"
                + "Use the link below to set a new password:\n"
                + safe(resetUrl) + "\n\n"
                + "If you did not request this, you can ignore this email.\n\n"
                + "Regards,\nBookMyShow Team";
        sendMail(user.getEmail(), subject, body);
    }

    @Async
    public void sendBookingConfirmation(Booking booking, List<ShowSeat> seats) {
        if (booking == null || booking.getUser() == null) {
            return;
        }
        String to = booking.getUser().getEmail();
        if (to == null || to.isBlank()) {
            return;
        }

        String subject = "Booking Confirmed - " + safe(booking.getBookingNumber());
        String body = "Hi " + safe(booking.getUser().getName()) + ",\n\n"
                + "Your booking is confirmed.\n"
                + "Booking Number: " + safe(booking.getBookingNumber()) + "\n"
                + "Movie: " + safe(booking.getShow().getMovie().getTitle()) + "\n"
                + "Theater: " + safe(booking.getShow().getScreen().getTheater().getName()) + "\n"
                + "Show Time: " + booking.getShow().getStartTime().format(SHOW_TIME_FORMATTER) + "\n"
                + "Seats: " + getSeatNumbers(seats) + "\n"
                + "Total Amount: " + booking.getTotalAmount() + "\n\n"
                + "Enjoy your show!\n\n"
                + "Regards,\nBookMyShow Team";

        sendMail(to, subject, body);
    }

    @Async
    public void sendBookingCancellation(Booking booking, List<ShowSeat> seats) {
        if (booking == null || booking.getUser() == null) {
            return;
        }
        String to = booking.getUser().getEmail();
        if (to == null || to.isBlank()) {
            return;
        }

        String subject = "Booking Cancelled - " + safe(booking.getBookingNumber());
        String body = "Hi " + safe(booking.getUser().getName()) + ",\n\n"
                + "Your booking has been cancelled.\n"
                + "Booking Number: " + safe(booking.getBookingNumber()) + "\n"
                + "Movie: " + safe(booking.getShow().getMovie().getTitle()) + "\n"
                + "Show Time: " + booking.getShow().getStartTime().format(SHOW_TIME_FORMATTER) + "\n"
                + "Seats: " + getSeatNumbers(seats) + "\n"
                + "Refund Status: " + (booking.getPayment() != null ? safe(booking.getPayment().getStatus()) : "N/A") + "\n\n"
                + "Regards,\nBookMyShow Team";

        sendMail(to, subject, body);
    }

    private void sendMail(String to, String subject, String body) {
        if (!mailingEnabled) {
            return;
        }
        if (mailSender == null) {
            log.warn("Mail sender not configured. Skipping email to {}", to);
            return;
        }

        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(fromAddress);
            message.setTo(to);
            message.setSubject(subject);
            message.setText(body);
            mailSender.send(message);
        } catch (Exception ex) {
            log.error("Failed to send email to {}. Reason: {}", to, ex.getMessage());
        }
    }

    private String getSeatNumbers(List<ShowSeat> seats) {
        if (seats == null || seats.isEmpty()) {
            return "N/A";
        }
        return seats.stream()
                .map(showSeat -> showSeat.getSeat() != null ? showSeat.getSeat().getSeatNumber() : null)
                .filter(Objects::nonNull)
                .collect(Collectors.joining(", "));
    }

    private String safe(String value) {
        return value == null ? "N/A" : value;
    }
}
