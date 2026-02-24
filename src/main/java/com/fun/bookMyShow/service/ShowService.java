package com.fun.bookMyShow.service;


import com.fun.bookMyShow.DTO.*;

import com.fun.bookMyShow.Model.Movie;
import com.fun.bookMyShow.Model.Screen;
import com.fun.bookMyShow.Model.Show;
import com.fun.bookMyShow.Model.ShowSeat;
import com.fun.bookMyShow.Model.Seat;
import com.fun.bookMyShow.exceptionHandling.ResourceNotFoundException;
import com.fun.bookMyShow.repository.MovieRepository;
import com.fun.bookMyShow.repository.ScreenRepository;
import com.fun.bookMyShow.repository.ShowRepository;
import com.fun.bookMyShow.repository.ShowSeatRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
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

    @Transactional
    public ShowDto createShow(ShowDto showDto){

      Show show = new Show();
        Long movieId = showDto.getMovie() != null ? showDto.getMovie().getId() : null;
        Long screenId = showDto.getScreen() != null ? showDto.getScreen().getId() : null;

        if (movieId == null) {
            throw new ResourceNotFoundException("Movie id is required in show payload");
        }
        if (screenId == null) {
            throw new ResourceNotFoundException("Screen id is required in show payload");
        }

        Movie movie = movieRepository.findById(movieId)
                .orElseThrow(() -> new ResourceNotFoundException("Movie not found by id " + movieId));

        Screen screen = screenRepository.findById(screenId)
                .orElseThrow(() -> new ResourceNotFoundException("Screen not found by id " + screenId));

        show.setMovie(movie);
        show.setScreen(screen);
        show.setStartTime(showDto.getStartTime());
        show.setEndTime(showDto.getEndTime());

        Show savedShow = showRepository.save(show);

        List<ShowSeat> availableSeats = ensureAvailableSeats(savedShow);

        return mapToDto(savedShow,availableSeats);
    }

    @Transactional(readOnly = true)
    public ShowDto getShowById(Long id)
    {
        Show show=showRepository.findById(id)
                .orElseThrow(()->new ResourceNotFoundException("Show nhi mila he id se"));
        List<ShowSeat> availavleSeats = ensureAvailableSeats(show);
        return mapToDto(show,availavleSeats);
    }
    @Transactional(readOnly = true)
    public List<ShowDto> getAllShows()
    {
        List<Show> shows=showRepository.findAll();
        return shows.stream()
                .map(show -> {
                   List<ShowSeat> availableSeats  = ensureAvailableSeats(show);
                    return mapToDto(show,availableSeats);
                })
                .collect(Collectors.toList());

    }

    @Transactional(readOnly = true)
    public List<ShowDto> getShowByMovie(Long movieId)
    {
        List<Show> shows=showRepository.findByMovieId(movieId);
        return shows.stream()
                .map(show -> {
                    List<ShowSeat> availableSeats  = ensureAvailableSeats(show);
                    return mapToDto(show,availableSeats);
                })
                .collect(Collectors.toList());

    }

    @Transactional(readOnly = true)
    public List<ShowDto> getShowByMovieAndCity(Long movieId, String city)
    {
        List<Show> shows=showRepository.findByMovie_IdAndScreen_Theater_City(movieId,city);
        return shows.stream()
                .map(show -> {
                    List<ShowSeat> availableSeats  = ensureAvailableSeats(show);
                    return mapToDto(show,availableSeats);
                })
                .collect(Collectors.toList());

    }

    @Transactional(readOnly = true)
    public List<ShowDto> getShowByDateRange(LocalDateTime start, LocalDateTime end)
    {
        List<Show> shows=showRepository.findByStartTimeBetween(start, end);
        return shows.stream()
                .map(show -> {
                    List<ShowSeat> availableSeats  = ensureAvailableSeats(show);
                    return mapToDto(show,availableSeats);
                })
                .collect(Collectors.toList());

    }

    private List<ShowSeat> ensureAvailableSeats(Show show) {
        List<ShowSeat> showSeats = showSeatRepository.findByShowId(show.getId());
        if (showSeats.isEmpty() && show.getScreen() != null && show.getScreen().getSeats() != null) {
            List<ShowSeat> generatedSeats = new ArrayList<>();
            for (Seat seat : show.getScreen().getSeats()) {
                ShowSeat showSeat = new ShowSeat();
                showSeat.setShow(show);
                showSeat.setSeat(seat);
                showSeat.setStatus("AVAILABLE");
                showSeat.setPrice(seat.getBasePrice() != null ? seat.getBasePrice().doubleValue() : 0.0);
                generatedSeats.add(showSeat);
            }
            showSeats = showSeatRepository.saveAll(generatedSeats);
        }
        return showSeats.stream()
                .filter(seat -> "AVAILABLE".equalsIgnoreCase(seat.getStatus()))
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
