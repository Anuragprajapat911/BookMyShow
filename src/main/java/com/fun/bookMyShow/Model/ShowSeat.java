package com.fun.bookMyShow.Model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(
        name="show_Seats",
        indexes = {
                @Index(name = "idx_show_seat_show_id", columnList = "show_id"),
                @Index(name = "idx_show_seat_booking_id", columnList = "booking_id"),
                @Index(name = "idx_show_seat_show_status", columnList = "show_id,status"),
                @Index(name = "idx_show_seat_show_seat", columnList = "show_id,seat_id")
        }
)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ShowSeat {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name="show_id", nullable=false)
    private Show show;

    @ManyToOne
    @JoinColumn(name="seat_id", nullable=false)
    private Seat seat;

    @Column(nullable = false)
    private String status;

    @Column(nullable = false)
    private Double price;


    @ManyToOne
    @JoinColumn(name = "booking_id")
    private Booking booking;

}
