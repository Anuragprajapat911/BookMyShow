package com.fun.bookMyShow.service;

import com.fun.bookMyShow.DTO.TheaterDto;
import com.fun.bookMyShow.Model.Theater;
import com.fun.bookMyShow.exceptionHandling.ResourceNotFoundException;
import com.fun.bookMyShow.repository.TheaterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TheaterService {


    @Autowired
    private TheaterRepository theaterRepository;

    @CacheEvict(value = {"theaterById", "allTheaters", "theatersByCity"}, allEntries = true)
    public TheaterDto createTheater(TheaterDto theaterDto) {
        Theater theater=mapToEntity(theaterDto);
          Theater saveTheater=  theaterRepository.save(theater);
          return  mapToDto(saveTheater);

    }
    @Cacheable(value = "theaterById", key = "#id")
    public  TheaterDto getTheaterById(Long id)
    {
        Theater theater=theaterRepository.findById(id)
                .orElseThrow(()->new ResourceNotFoundException("Theater not found"));
        return mapToDto(theater);
    }
    @Cacheable(value = "allTheaters")
    public List<TheaterDto> getAllTheater()
    {
        List<Theater> theaters=theaterRepository.findAll();
        return theaters.stream().map(this::mapToDto)
                .collect(Collectors.toList());
    }


    @Cacheable(value = "theatersByCity", key = "#city")
    public List<TheaterDto> getAllTheaterCity( String city)
    {
        List<Theater> theaters=theaterRepository.findByCity(city);
        return theaters.stream().map(this::mapToDto)
                .collect(Collectors.toList());
    }

    private TheaterDto mapToDto(Theater theater){
        TheaterDto theaterDto = new TheaterDto();
        theaterDto.setId(theater.getId());
        theaterDto.setName(theater.getName());
        theaterDto.setCity(theater.getCity());
        theaterDto.setAddress(theater.getAddress());
        theaterDto.setTotalScreen(theater.getTotalScreen());
        return theaterDto;


    }
    @CachePut(value = "theaterById", key = "#id")
    @CacheEvict(value = {"allTheaters", "theatersByCity"}, allEntries = true)
    public TheaterDto updateTheater(Long id, TheaterDto theaterDto) {

        Theater existingTheater = theaterRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Theater Nhi mila he "));

        existingTheater.setName(theaterDto.getName());
        existingTheater.setCity(theaterDto.getCity());
        existingTheater.setAddress(theaterDto.getAddress());
        existingTheater.setTotalScreen(theaterDto.getTotalScreen());

        Theater updatedTheater = theaterRepository.save(existingTheater);
        return mapToDto(updatedTheater);
    }


    @CacheEvict(value = {"theaterById", "allTheaters", "theatersByCity"}, allEntries = true)
    public void deleteTheater(Long id) {

        Theater theater = theaterRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Theater Nhi mila he "));

        theaterRepository.delete(theater);
    }

    private Theater mapToEntity(TheaterDto theaterDto){

        Theater theater=new Theater();
        theater.setId(theaterDto.getId());
        theater.setName(theaterDto.getName());
        theater.setCity(theaterDto.getCity());
        theater.setTotalScreen(theaterDto.getTotalScreen());
        theater.setAddress(theaterDto.getAddress());
        return theater;
    }
}
