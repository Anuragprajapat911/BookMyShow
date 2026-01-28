package com.fun.bookMyShow.repository;

import com.fun.bookMyShow.Model.Show;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
@Repository
public interface ShowRepository extends JpaRepository<Show,Long> {

    List<Show> findByMovieId(Long movieId);

    List<Show> findByScreenId(Long ScreenId);

    List<Show> findByStartTimeBetween(LocalDateTime start, LocalDateTime end);

    List<Show> findByMovie_IdAndScreen_Theater_City(Long movieId, String city);
}
