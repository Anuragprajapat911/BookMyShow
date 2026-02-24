package com.fun.bookMyShow.controller;

import com.fun.bookMyShow.DTO.BookingDto;
import com.fun.bookMyShow.DTO.BookingRequestDto;
import com.fun.bookMyShow.service.BookingService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
@CrossOrigin(origins = "http://localhost:5173")

@RestController
@RequestMapping("/api/booking")
public class BookingController
{
    @Autowired
    private BookingService bookingService;

    @PostMapping
    public ResponseEntity<BookingDto> createBooking(@Valid @RequestBody BookingRequestDto  bookingRequestDto)
    {
            return new ResponseEntity<>(bookingService.createBooking(bookingRequestDto), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BookingDto> getBookingById(@PathVariable Long id)
    {
        return ResponseEntity.ok(bookingService.getBookingById(id));
    }
    @PutMapping("/cancel/{id}")
    public ResponseEntity<BookingDto> cancelBooking(@PathVariable Long id) {

        BookingDto bookingDto = bookingService.cancelBooking(id);
        return ResponseEntity.ok(bookingDto);
    }


}
