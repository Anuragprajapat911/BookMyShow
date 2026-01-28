package com.fun.bookMyShow.service;


import com.fun.bookMyShow.DTO.*;

import com.fun.bookMyShow.Model.Movie;
import com.fun.bookMyShow.Model.Screen;
import com.fun.bookMyShow.Model.Show;
import com.fun.bookMyShow.Model.ShowSeat;
import com.fun.bookMyShow.exceptionHandling.ResourceNotFoundException;
import com.fun.bookMyShow.repository.MovieRepository;
import com.fun.bookMyShow.repository.ScreenRepository;
import com.fun.bookMyShow.repository.ShowRepository;
import com.fun.bookMyShow.repository.ShowSeatRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ShowService {
    @Autowired
    private MovieRepository movieRepository;

    @Autowired
    private ScreenRepository screenRepository;

    @Autowired
    private ShowRepository showRepository;

    @Autowired
    private ShowSeatRepository showSeatRepository;

    public ShowDto createShow(ShowDto showDto){

      Show show=new Show();
        Movie movie=movieRepository.findById(showDto.getId())
                .orElseThrow(()->new ResourceNotFoundException("Movie Nhi meli he Bhai"));

        Screen screen=screenRepository.findById(showDto.getId())
                .orElseThrow(()->new ResourceNotFoundException("Screen Nhi meli he Bhai"));

        show.setMovie(movie);
        show.setScreen(screen);
        show.setStartTime(showDto.getStartTime());
        show.setEndTime(showDto.getEndTime());

        Show savedShow=showRepository.save(show);

        List<ShowSeat> availableSeats=showSeatRepository.findByShowIdAndStatus(savedShow.getId(),"Available");

        return mapToDto(savedShow,availableSeats);
    }

    public ShowDto getShowById(Long id)
    {
        Show show=showRepository.findById(id)
                .orElseThrow(()->new ResourceNotFoundException("Show nhi mila he id se"));
        List<ShowSeat> availavleSeats=showSeatRepository.findByShowIdAndStatus(show.getId(),"Available");
        return mapToDto(show,availavleSeats);
    }
    public List<ShowDto> getAllShows()
    {
        List<Show> shows=showRepository.findAll();
        return shows.stream()
                .map(show -> {
                   List<ShowSeat> availableSeats  =showSeatRepository.findByShowIdAndStatus(show.getId(),"Available");
                    return mapToDto(show,availableSeats);
                })
                .collect(Collectors.toList());

    }

    public List<ShowDto> getShowByMovie(Long movieId)
    {
        List<Show> shows=showRepository.findByMovieId(movieId);
        return shows.stream()
                .map(show -> {
                    List<ShowSeat> availableSeats  =showSeatRepository.findByShowIdAndStatus(show.getId(),"Available");
                    return mapToDto(show,availableSeats);
                })
                .collect(Collectors.toList());

    }

    public List<ShowDto> getShowByMovieAndCity(Long movieId, String city)
    {
        List<Show> shows=showRepository.findByMovie_IdAndScreen_Theater_City(movieId,city);
        return shows.stream()
                .map(show -> {
                    List<ShowSeat> availableSeats  =showSeatRepository.findByShowIdAndStatus(show.getId(),"Available");
                    return mapToDto(show,availableSeats);
                })
                .collect(Collectors.toList());

    }

    public List<ShowDto> getShowByDateRange(LocalDateTime start, LocalDateTime end)
    {
        List<Show> shows=showRepository.findByStartTimeBetween(start, end);
        return shows.stream()
                .map(show -> {
                    List<ShowSeat> availableSeats  =showSeatRepository.findByShowIdAndStatus(show.getId(),"Available");
                    return mapToDto(show,availableSeats);
                })
                .collect(Collectors.toList());

    }

       private ShowDto mapToDto(Show show,List<ShowSeat> availableSeats){

        ShowDto showDto=new ShowDto();
        showDto.setId(show.getId());
        showDto.setStartTime(show.getStartTime());
        showDto.setEndTime(show.getEndTime());


        showDto.setMovie(new MovieDto(
                show.getMovie().getId(),
                show.getMovie().getTitle(),
                show.getMovie().getDescription(),
                show.getMovie().getLanguage(),
                show.getMovie().getGenre(),
                show.getMovie().getDurectionMins(),
                show.getMovie().getPosterUrl(),
                show.getMovie().getReleaseDate()
        ));


        TheaterDto  theaterDto=new TheaterDto(
            show.getScreen().getTheater().getId(),
             show.getScreen().getTheater().getName(),
             show.getScreen().getTheater().getAddress(),
             show.getScreen().getTheater().getCity(),
                show.getScreen().getTheater().getTotalScreen()

        );



           showDto.setScreen(new ScreenDto(
                show.getScreen().getId(),
                show.getScreen().getName(),
                show.getScreen().getTotalSeats(),
                   theaterDto



        ));

         List<ShowSeatDto> seatDtoS= availableSeats.stream()
                .map(seat -> {
                    ShowSeatDto showSeatDto=new ShowSeatDto();
                    showSeatDto.setId(seat.getId());
                    showSeatDto.setPrice(seat.getPrice());
                    showSeatDto.setStatus(seat.getStatus());

                    SeatDto seatDto=new SeatDto();
                    seatDto.setId(seat.getSeat().getId());
                    seatDto.setSeatType(seat.getSeat().getSeatType());
                    seatDto.setSeatNumber(seat.getSeat().getSeatNumber());
                    seatDto.setBasePrice(seat.getSeat().getBasePrice());
                    showSeatDto.setSeat(seatDto);
                    return showSeatDto;


                })
                 .collect(Collectors.toList());
            showDto.setAvailableSeats( seatDtoS);
            return showDto;
    }
}
