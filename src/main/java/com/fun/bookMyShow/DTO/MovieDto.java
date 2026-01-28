package com.fun.bookMyShow.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MovieDto {


    private Long Id;
    private String title;
    private String description;
    private String language;
    private String genre;
    private Integer duration;
    private String releaseDate;
    private String posterUrl;
}
