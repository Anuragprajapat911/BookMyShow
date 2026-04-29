package com.fun.bookMyShow.Model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(
        name="bookings",
        indexes = {
                @Index(name = "idx_booking_user_id", columnList = "user_id"),
                @Index(name = "idx_booking_show_id", columnList = "show_id"),
                @Index(name = "idx_booking_booking_number", columnList = "booking_number")
        }
)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String bookingNumber;

    @Column(nullable = false)
    private LocalDateTime bookingTime;

    @ManyToOne
    @JoinColumn(name="user_id",nullable=false)
    private User user;

    @ManyToOne
    @JoinColumn(name="show_id",nullable=false)
    private Show show;

    @Column(nullable=false)
    private String status;

    @Column(nullable=false)
    private Double totalAmount;

    @OneToMany(mappedBy = "booking", cascade = CascadeType.ALL)
    private List<ShowSeat> showSeats;


    @OneToOne(cascade=CascadeType.ALL)
    @JoinColumn(name="payment_id")
    private Payment payment;


}
