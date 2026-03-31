package com.bms.bookmyseat.service;

import com.bms.bookmyseat.dto.MovieRequest;
import com.bms.bookmyseat.dto.MovieResponse;
import org.springframework.data.domain.Page;

public interface MovieService {

    MovieResponse addMovie(MovieRequest request);
    Page<MovieResponse> getAllMovies(int page, int size);

    Page<MovieResponse> searchMovies(String genre, String language, String title, int page, int size);

    void deleteMovie(Long id);
    MovieResponse updateMovie(Long id, MovieRequest request);
}