package com.fun.bookMyShow.repository;

import com.fun.bookMyShow.Model.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {

    List<Booking> findByUserId(Long id);

    Optional<Booking> findByBookingNumber(String bookingNumber);

    List<Booking> findByShowId(Long id);
}
