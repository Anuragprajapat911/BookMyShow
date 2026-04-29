package com.fun.bookMyShow.controller;

import com.fun.bookMyShow.DTO.TheaterDto;

import com.fun.bookMyShow.service.TheaterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/theaters")
public class TheaterController {

    @Autowired
    private TheaterService theaterService;

    @PostMapping
    public ResponseEntity<TheaterDto> createTheater(@RequestBody TheaterDto theaterDto){

      TheaterDto createTheater= theaterService.createTheater(theaterDto);
      return new ResponseEntity<>(createTheater, HttpStatus.CREATED);
    }
    @GetMapping("/{id}")
    public ResponseEntity<TheaterDto> getTheaterById( @PathVariable Long id)
    {
      return ResponseEntity.ok( theaterService.getTheaterById(id));

    }
    @GetMapping
    public ResponseEntity<List<TheaterDto>>  getAllTheaters()
    {
        return ResponseEntity.ok(theaterService.getAllTheater());
    }
    @PutMapping("/{id}")
    public ResponseEntity<TheaterDto> updateTheater(@PathVariable Long id, @RequestBody TheaterDto theaterDto)
    {
       TheaterDto update= theaterService.updateTheater(id, theaterDto);
       return   ResponseEntity.ok(update);
    }
    @DeleteMapping("/{id}")
    public  ResponseEntity<String> deleteTheater(@PathVariable Long id)
    {
        theaterService.deleteTheater(id);
        return ResponseEntity.ok("Theater Deleted kar diya gay he");
    }

    @GetMapping("/city/{city}")
    public ResponseEntity<List<TheaterDto>> getTheatersByCity(@PathVariable String city) {
        return ResponseEntity.ok(theaterService.getAllTheaterCity(city));
    }
}
