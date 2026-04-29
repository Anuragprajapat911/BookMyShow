package com.fun.bookMyShow.Model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(
        name="movies",
        indexes = {
                @Index(name = "idx_movie_title", columnList = "title"),
                @Index(name = "idx_movie_language", columnList = "language"),
                @Index(name = "idx_movie_genre", columnList = "genre")
        }
)
@Data
@NoArgsConstructor
@AllArgsConstructor

public class Movie {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    private String description;

    private String language;
    private String genre;
    private Integer DurectionMins;
    private String posterUrl;
    private String releaseDate;


    @OneToMany(mappedBy = "movie",cascade = CascadeType.ALL)
    private List<Show> shows;

  //  public Movie(Long id, String title, String description, String language, String genre, Integer durectionMins, String posterUrl, String durectionMins1) {
  //  }
}
