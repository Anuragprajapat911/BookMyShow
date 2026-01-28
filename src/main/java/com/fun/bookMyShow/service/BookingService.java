package com.fun.bookMyShow.service;

import com.fun.bookMyShow.DTO.*;
import com.fun.bookMyShow.Model.*;
import com.fun.bookMyShow.exceptionHandling.ResourceNotFoundException;
import com.fun.bookMyShow.exceptionHandling.SeatUnavilableExcepation;
import com.fun.bookMyShow.repository.BookingRepository;
import com.fun.bookMyShow.repository.ShowRepository;
import com.fun.bookMyShow.repository.ShowSeatRepository;
import com.fun.bookMyShow.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class BookingService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ShowRepository showRepository;

    @Autowired
    private ShowSeatRepository seatRepository;

    @Autowired
    private BookingRepository bookingRepository;

    @Transactional
    public BookingDto createBooking(BookingRequestDto bookingRequest)
    {
        User user=userRepository.findById(bookingRequest.getUserId())
                .orElseThrow(()->new ResourceNotFoundException("User not found"));

        Show show=showRepository.findById(bookingRequest.getUserId())
                .orElseThrow(()->new ResourceNotFoundException("Show not found"));


        List<ShowSeat> SelectedSeat=seatRepository.findByShowId(bookingRequest.getShowId());

        for(ShowSeat showSeat:SelectedSeat)
        {
            if(!"AVAILABLE".equals(showSeat.getStatus()))
            {
             throw new SeatUnavilableExcepation("seat"+showSeat.getSeat().getSeatNumber()+"is not available");
            }
            showSeat.setStatus("LOCKED");
        }
        seatRepository.saveAll(SelectedSeat);

        Double totalAmount=SelectedSeat.stream().mapToDouble(ShowSeat::getPrice).sum();

        Payment payment=new Payment();
        payment.setAmount(totalAmount);
        payment.setPaymentTime(LocalDateTime.now());
        payment.setPaymentMethod(bookingRequest.getPaymentMethod());
        payment.setStatus("Success");
        payment.setTransectionId(UUID.randomUUID().toString());


        Booking booking=new Booking();
        booking.setUser(user);
        booking.setShow(show);
        booking.setBookingTime(LocalDateTime.now());
        booking.setStatus("Conformed");
        booking.setTotalAmount(totalAmount);
        booking.setBookingNumber(UUID.randomUUID().toString());
        booking.setPayment(payment);

        Booking saveBooking=bookingRepository.save(booking);

        SelectedSeat.forEach(seat -> {
            seat.setStatus("Booked");
            seat.setBooking(saveBooking);
        });

        seatRepository.saveAll(SelectedSeat);
        return mapToBookingDto(saveBooking,SelectedSeat);
    }

    public BookingDto getBookingById(Integer id)
    {
        Booking booking=bookingRepository.findById(id)
        .orElseThrow(()->new ResourceNotFoundException("Booking not found"));
       List<ShowSeat> seats= seatRepository.findAll()
                .stream().filter(Seat->Seat.getBooking()!=null && Seat.getBooking().getId().equals(booking.getId()))
                .collect(Collectors.toList());
                return mapToBookingDto(booking,seats);
    }

    private BookingDto getBookingDto(String bookingNumber)
    {
        Booking booking=bookingRepository.findByBookingNumber(bookingNumber)
                .orElseThrow(()->new ResourceNotFoundException("Booking not found"));
        List<ShowSeat> seats= seatRepository.findAll()
                .stream().filter(Seat->Seat.getBooking()!=null && Seat.getBooking().getId().equals(booking.getId()))
                .collect(Collectors.toList());
        return mapToBookingDto(booking,seats);
    }


    private List<BookingDto> getBookingByUserId(Long Id)
    {

        List<Booking> bookings=bookingRepository.findByUserId(Id);
        return bookings.stream()
                .map(booking -> {
                    List<ShowSeat> seats= seatRepository.findAll()
                            .stream().
                         filter(Seat->Seat.getBooking()!=null && Seat.getBooking().getId().equals(booking.getId())).collect(Collectors.toList());
                    return mapToBookingDto(booking,seats);
                })
                .collect(Collectors.toList());
    }
    @Transactional
    public BookingDto cancelBooking(Integer id)
    {
        Booking booking=bookingRepository.findById(id)
                .orElseThrow(()->new ResourceNotFoundException("Booking not found"));
        booking.setStatus("CANCELLED");

          List<ShowSeat> seats =seatRepository.findAll()
                .stream()
                .filter(Seat->Seat.getBooking()!=null && Seat.getBooking().getId().equals(booking.getId()))
            .collect(Collectors.toList());

          seats.forEach(seat->{
              seat.setStatus("AVAILABLE");
              seat.setBooking(null);
          });
          if(booking.getPayment()!=null)
          {
              booking.getPayment().setStatus("REFUNDED");
          }
          Booking updateBooking=bookingRepository.save(booking);
          seatRepository.saveAll(seats);
          return mapToBookingDto(booking,seats);
    }

    private  BookingDto mapToBookingDto(Booking  booking,List<ShowSeat> seats)
    {
     BookingDto bookingDto=new BookingDto();
     bookingDto.setId(booking.getId());
     bookingDto.setBookingNumber(booking.getBookingNumber());
     bookingDto.setBookingDate(booking.getBookingTime());
     bookingDto.setStatus(booking.getStatus());
     bookingDto.setTotalAmount(booking.getTotalAmount());

        UserDto userDto=new UserDto();
        userDto.setId(booking.getUser().getId());
        userDto.setEmail(booking.getUser().getEmail());
        userDto.setName(booking.getUser().getName());
        userDto.setPhoneNumber(booking.getUser().getPhoneNumber());
        userDto.setPassword(booking.getUser().getPassword());
        bookingDto.setUser(userDto);


        ShowDto showDto=new ShowDto();
        showDto.setId(booking.getShow().getId());
        showDto.setStartTime(booking.getShow().getStartTime());
        showDto.setEndTime(booking.getShow().getEndTime());


        MovieDto movieDto=new MovieDto();
        movieDto.setId(booking.getShow().getMovie().getId());

        movieDto.setTitle(booking.getShow().getMovie().getTitle());
        movieDto.setGenre(booking.getShow().getMovie().getGenre());
        movieDto.setLanguage(booking.getShow().getMovie().getLanguage());
        movieDto.setDescription(booking.getShow().getMovie().getDescription());
        movieDto.setPosterUrl(booking.getShow().getMovie().getPosterUrl());
        movieDto.setDuration(booking.getShow().getMovie().getDurectionMins());

        movieDto.setReleaseDate(booking.getShow().getMovie().getReleaseDate());
         showDto.setMovie(movieDto);


        ScreenDto screenDto=new ScreenDto();
        screenDto.setId(booking.getShow().getScreen().getId());
        screenDto.setName(booking.getShow().getScreen().getName());
        screenDto.setTotalSeats(booking.getShow().getScreen().getTotalSeats());



        TheaterDto theaterDto=new TheaterDto();
        theaterDto.setName(booking.getShow().getScreen().getTheater().getName());
        theaterDto.setId(booking.getShow().getScreen().getTheater().getId());
        theaterDto.setCity(booking.getShow().getScreen().getTheater().getCity());
        theaterDto.setAddress(booking.getShow().getScreen().getTheater().getAddress());
        theaterDto.setTotalScreen(booking.getShow().getScreen().getTheater().getTotalScreen());
        screenDto.setTheater(theaterDto);
        showDto.setScreen(screenDto);
        bookingDto.setShow(showDto);

         List<ShowSeatDto> seatDtos= seats.stream().map(showSeat -> {
          ShowSeatDto seatDto=new ShowSeatDto();
          seatDto.setId(showSeat.getId());
          seatDto.setStatus(showSeat.getStatus());
          seatDto.setPrice(showSeat.getPrice());


          SeatDto baseSeatDto=new SeatDto();
          baseSeatDto.setId(showSeat.getSeat().getId());
          baseSeatDto.setSeatNumber(showSeat.getSeat().getSeatNumber());
          baseSeatDto.setSeatType(showSeat.getSeat().getSeatType());
          baseSeatDto.setBasePrice(showSeat.getSeat().getBasePrice());
          seatDto.setSeat(baseSeatDto);
          return seatDto;
      })
              .collect(Collectors.toList()) ;
         bookingDto.setSeats(seatDtos);

         if(booking.getPayment()!=null)
        {
          PaymentDto paymentDto=new PaymentDto();
          paymentDto.setId(booking.getPayment().getId());
          paymentDto.setAmount(booking.getPayment().getAmount());
          paymentDto.setPaymentMethod(booking.getPayment().getPaymentMethod());
          paymentDto.setPaymentTime(booking.getPayment().getPaymentTime());
          paymentDto.setTransactionId(booking.getPayment().getTransectionId());
          paymentDto.setStatus(booking.getPayment().getStatus());
          bookingDto.setPayment(paymentDto);
        }
        return bookingDto;
    }
}
