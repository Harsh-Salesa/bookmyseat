package com.bms.bookmyseat.controller;

import com.bms.bookmyseat.dto.MovieRequest;
import com.bms.bookmyseat.dto.MovieResponse;
import com.bms.bookmyseat.service.MovieService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/movies")
@RequiredArgsConstructor
@Slf4j
public class MovieController {

    private final MovieService movieService;

    @PostMapping
    public MovieResponse addMovie(@Valid @RequestBody MovieRequest request) {
        log.info("API hit: ADD MOVIE");
        return movieService.addMovie(request);
    }

    @GetMapping
    public Page<MovieResponse> getMovies(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size
    ) {
        log.info("API hit: GET MOVIES");
        return movieService.getAllMovies(page, size);
    }
}