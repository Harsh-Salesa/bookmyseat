package com.bms.bookmyseat.repository;

import com.bms.bookmyseat.entity.Movie;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MovieRepository extends JpaRepository<Movie, Long> {
}