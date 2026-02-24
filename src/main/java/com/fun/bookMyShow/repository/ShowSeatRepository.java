package com.fun.bookMyShow.repository;

import com.fun.bookMyShow.Model.ShowSeat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface ShowSeatRepository extends JpaRepository<ShowSeat, Long> {

    List<ShowSeat> findByShowId(Long showId);

    List<ShowSeat> findByShowIdAndStatusIgnoreCase(Long showId, String status);

    List<ShowSeat> findByShowIdAndIdIn(Long showId, List<Long> ids);

    List<ShowSeat> findByShowIdAndSeatIdIn(Long showId, List<Long> seatIds);


}
