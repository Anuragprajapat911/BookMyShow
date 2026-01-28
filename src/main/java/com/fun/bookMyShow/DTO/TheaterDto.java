package com.fun.bookMyShow.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TheaterDto {

    private Long id;
    private String name;
    private String address;
    private String city;
    private Integer totalScreen;
}
