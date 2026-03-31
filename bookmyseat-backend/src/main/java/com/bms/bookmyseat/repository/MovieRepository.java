package com.bms.bookmyseat.repository;

import com.bms.bookmyseat.dto.MovieRequest;
import com.bms.bookmyseat.dto.MovieResponse;
import com.bms.bookmyseat.entity.Movie;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MovieRepository extends JpaRepository<Movie, Long> {
    boolean existsByTitle(String title);
    Page<Movie> findByGenreContainingIgnoreCaseAndLanguageContainingIgnoreCaseAndTitleContainingIgnoreCase(
            String genre,
            String language,
            String title,
            Pageable pageable
    );

}