package com.bms.bookmyseat.service;

import com.bms.bookmyseat.dto.MovieRequest;
import com.bms.bookmyseat.dto.MovieResponse;
import org.springframework.data.domain.Page;

public interface MovieService {

    MovieResponse addMovie(MovieRequest request);

    Page<MovieResponse> getAllMovies(int page, int size);
}