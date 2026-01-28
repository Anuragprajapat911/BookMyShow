package com.fun.bookMyShow.service;

import com.fun.bookMyShow.DTO.MovieDto;
import com.fun.bookMyShow.Model.Movie;
import com.fun.bookMyShow.exceptionHandling.ResourceNotFoundException;
import com.fun.bookMyShow.repository.MovieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MovieService {


    @Autowired
    private MovieRepository movieRepository;

    public MovieDto createMovie(MovieDto movieDto) {

        Movie movie=mapToEntity(movieDto);
        Movie saveMovie=movieRepository.save(movie);
        return mapToDto(saveMovie);
    }


        public MovieDto getMovieById(Long id)
        {
            Movie movie=movieRepository.findById(id)
                    .orElseThrow(()->new ResourceNotFoundException("Movie not found by id"+id));
            return mapToDto(movie);
        }

        public List<MovieDto> getAllMovies()
        {
            List<Movie> movies=movieRepository.findAll();
            return movies.stream().map(this::mapToDto).toList();
        }

        public List<MovieDto> getMovieByLanguage(String language)
        {
            List<Movie> movies=movieRepository.findByLanguage(language);
            return movies.stream().map(this::mapToDto).toList();
        }

        public List<MovieDto> getMovieByGenre(String genre)
        {
            List<Movie> movies=movieRepository.findByGenre(genre);
            return movies.stream().map(this::mapToDto).toList();
        }

        public List<MovieDto> getMovieByTitle(String title)
        {
            List<Movie> movies=movieRepository.findByTitleContaining(title);
            return movies.stream().map(this::mapToDto).toList();
        }

        public MovieDto updateMovie(Long id, MovieDto movieDto) {

            Movie movie=movieRepository.findById(id)
                    .orElseThrow(()->new ResourceNotFoundException("Movie not found by id"+id));
            movie.setTitle(movieDto.getTitle());
            movie.setId(movieDto.getId());
            movie.setLanguage(movieDto.getLanguage());
            movie.setReleaseDate(movieDto.getReleaseDate());
            movie.setPosterUrl(movieDto.getPosterUrl());
            movie.setGenre(movieDto.getGenre());
            movie.setDurectionMins(movieDto.getDuration());
            movie.setReleaseDate(movieDto.getReleaseDate());
            Movie saveMovie=movieRepository.save(movie);
            return mapToDto(saveMovie);
        }

        public void  deleteMovie(Long id)
        {

            Movie movie=movieRepository.findById(id)
                    .orElseThrow(()->new ResourceNotFoundException("Movie not found by id"+id));
            movieRepository.delete(movie);


        }

    private MovieDto mapToDto(Movie movie) {

        MovieDto movieDto=new MovieDto();
        movieDto.setId(movie.getId());
        movieDto.setDuration(movie.getDurectionMins());
        movieDto.setPosterUrl(movie.getPosterUrl());
        movieDto.setReleaseDate(movie.getReleaseDate());
        movieDto.setGenre(movie.getGenre());
        movieDto.setTitle(movie.getTitle());
        movieDto.setLanguage(movie.getLanguage());
        movieDto.setDescription(movie.getDescription());
        return movieDto;
    }

    public Movie mapToEntity(MovieDto movieDto) {

        Movie movie=new Movie();
        movie.setTitle(movieDto.getTitle());
        movie.setId(movieDto.getId());
        movie.setLanguage(movieDto.getLanguage());
        movie.setReleaseDate(movieDto.getReleaseDate());
        movie.setPosterUrl(movieDto.getPosterUrl());
        movie.setGenre(movieDto.getGenre());
        movie.setDurectionMins(movieDto.getDuration());
        movie.setReleaseDate(movieDto.getReleaseDate());
        return movie;

    }
}
