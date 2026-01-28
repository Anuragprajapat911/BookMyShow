package com.fun.bookMyShow.repository;

import com.fun.bookMyShow.Model.Screen;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface ScreenRepository extends JpaRepository<Screen,Long> {


    Optional<Screen> findByTheaterId(Long theaterId);
}
