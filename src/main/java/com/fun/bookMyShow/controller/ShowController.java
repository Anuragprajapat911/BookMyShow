package com.fun.bookMyShow.controller;

import com.fun.bookMyShow.DTO.ShowDto;
import com.fun.bookMyShow.service.ShowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
@CrossOrigin(origins = "http://localhost:5173")

@RestController
@RequestMapping("/api/shows")
public class ShowController {


    @Autowired
    private ShowService showService;

    @PostMapping
    public ResponseEntity<ShowDto> createShow(@RequestBody ShowDto showDto) {

        ShowDto createShow= showService.createShow(showDto);
        return new ResponseEntity<>(createShow, HttpStatus.CREATED);
    }
    @GetMapping("/{id}")
    public ResponseEntity<ShowDto> getShowById(@PathVariable Long id) {

      return ResponseEntity.ok( showService.getShowById(id));

    }
    @GetMapping
    public ResponseEntity<List<ShowDto>> getAllShows() {
        return ResponseEntity.ok(showService.getAllShows());
    }
    @GetMapping("/movie/{id}")
    public ResponseEntity<List<ShowDto>> searchShowByMovieId(@PathVariable Long id){

        return ResponseEntity.ok(showService.getShowByMovie(id));
    }
    @GetMapping("/movie/{id}/city/{city}")
    public ResponseEntity<List<ShowDto>> getShowByMovieAndCity(@PathVariable Long id,@PathVariable String city){

        return ResponseEntity.ok(showService.getShowByMovieAndCity(id,city));
    }
    @GetMapping("/date-range")
    public ResponseEntity<List<ShowDto>> getShowByStartAndEnd(@RequestParam LocalDateTime start, @RequestParam LocalDateTime end){

        return ResponseEntity.ok(showService.getShowByDateRange(start,end));
    }
}
