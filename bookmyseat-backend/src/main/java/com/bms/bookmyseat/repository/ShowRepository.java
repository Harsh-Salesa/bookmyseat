package com.bms.bookmyseat.repository;

import com.bms.bookmyseat.entity.Movie;
import com.bms.bookmyseat.entity.Show;
import com.bms.bookmyseat.entity.Theater;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.time.LocalTime;

public interface ShowRepository extends JpaRepository<Show, Long> {

    Page<Show> findByMovieIdAndTheaterIdAndShowDate(
            Long movieId,
            Long theaterId,
            LocalDate showDate,
            Pageable pageable
    );
    boolean existsByMovieAndTheaterAndShowDateAndShowTime(
            Movie movie,
            Theater theater,
            LocalDate showDate,
            LocalTime showTime
    );

    Page<Show> findByMovieIdAndShowDate(
            Long movieId,
            LocalDate showDate,
            Pageable pageable
    );
}
