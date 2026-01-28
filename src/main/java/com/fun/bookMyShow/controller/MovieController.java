package com.fun.bookMyShow.controller;

import com.fun.bookMyShow.DTO.BookingDto;
import com.fun.bookMyShow.DTO.MovieDto;
import com.fun.bookMyShow.service.MovieService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@CrossOrigin(origins = "http://localhost:5173")

@RestController
@RequestMapping("/api/movies")
public class MovieController {
    @Autowired
    private MovieService movieService;


    @PostMapping
    public ResponseEntity<MovieDto> createMovie(@Valid @RequestBody MovieDto movieDto){

      return new ResponseEntity<>(movieService.createMovie(movieDto), HttpStatus.CREATED) ;
    }
    @GetMapping("/{id}")
    public ResponseEntity<MovieDto> getMovieById(@PathVariable Long id){

        return ResponseEntity.ok(movieService.getMovieById(id));
    }
    @GetMapping
    public ResponseEntity<List<MovieDto>> getAllMovies(){

        return ResponseEntity.ok(movieService.getAllMovies());
    }
    @GetMapping("/language/{language}")
    public ResponseEntity<List<MovieDto>> getMovieByLanguage(@PathVariable String language){

        return ResponseEntity.ok(movieService.getMovieByLanguage(language));
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteMovie(@PathVariable Long id){
        movieService.deleteMovie(id);
        return ResponseEntity.ok().body("Movie deleted successfully");
    }
    @PutMapping("/{id}")
    public ResponseEntity<MovieDto> updateMovieById(@Valid @RequestBody MovieDto movieDto ,@PathVariable Long id){
        MovieDto updateMovie=movieService.updateMovie(id,movieDto);
        return ResponseEntity.ok(updateMovie);


    }
    @GetMapping("/title{title}")
    public ResponseEntity<List<MovieDto>> searchMovieByTitle(@PathVariable String title){


        return ResponseEntity.ok(movieService.getMovieByTitle(title));

    }
}
