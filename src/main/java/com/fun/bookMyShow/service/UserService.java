package com.fun.bookMyShow.service;

import com.fun.bookMyShow.DTO.UserDto;
import com.fun.bookMyShow.Model.User;
import com.fun.bookMyShow.exceptionHandling.ResourceNotFoundException;
import com.fun.bookMyShow.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {


    @Autowired
    private UserRepository userRepository;

    public UserDto createUsr(UserDto userDto){

        User user= mapToEntity(userDto);
        User savedUser = userRepository.save(user);
        return mapToDto(savedUser);
    }

    public UserDto getUserById(Long id){

        User user=userRepository.findById(id)
        .orElseThrow(()->new ResourceNotFoundException( "User nahi mila he Id se"));
        return mapToDto(user);
    }

    public List<UserDto> getAllUser(){

        List<User> users=userRepository.findAll();
        return users.stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

   public void deleteUser(Long id){

        User user =userRepository.findById(id)
                .orElseThrow(()->new ResourceNotFoundException("User nahi mila he Id se bhai"));
        userRepository.delete(user);
   }


    public UserDto updateUserById(Long id, UserDto userDto){

       User user= userRepository.findById(id)
               .orElseThrow(()->new ResourceNotFoundException("User nahi mila he Id se bhai"));

       user.setName(userDto.getName());
       user.setEmail(userDto.getEmail());
       user.setPhoneNumber(userDto.getPhoneNumber());
       user.setId(userDto.getId());
       user.setPassword(userDto.getPassword());
       userRepository.save(user);
       return mapToDto(user);
    }

    private User mapToEntity(UserDto userDto){

        User user = new User();
        user.setId(userDto.getId());
        user.setEmail(userDto.getEmail());
        user.setName(userDto.getName());
        user.setPhoneNumber(userDto.getPhoneNumber());
        user.setPassword(userDto.getPassword());
        return user;
    }
    private UserDto mapToDto(User user){

        UserDto userDto = new UserDto();
        userDto.setId(user.getId());
        userDto.setEmail(user.getEmail());
        userDto.setName(user.getName());
        userDto.setPhoneNumber(user.getPhoneNumber());
        userDto.setPassword(user.getPassword());
        return userDto;
    }
}
