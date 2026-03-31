package com.bms.bookmyseat.controller;

import com.bms.bookmyseat.dto.MovieRequest;
import com.bms.bookmyseat.dto.MovieResponse;
import com.bms.bookmyseat.service.MovieService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/movies")
@RequiredArgsConstructor
@Slf4j
public class MovieController {

    private final MovieService movieService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public MovieResponse addMovie(@Valid @RequestBody MovieRequest request) {
        log.info("API hit: ADD MOVIE");
        return movieService.addMovie(request);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public MovieResponse updateMovie(
            @PathVariable Long id,
            @Valid @RequestBody MovieRequest request) {

        log.info("API hit: UPDATE MOVIE id={}", id);
        return movieService.updateMovie(id, request);
    }

    @GetMapping
    public Page<MovieResponse> getMovies(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size
    ) {
        log.info("API hit: GET MOVIES");
        return movieService.getAllMovies(page, size);
    }
    @GetMapping("/search")
    public Page<MovieResponse> searchMovies(
            @RequestParam(required = false) String genre,
            @RequestParam(required = false) String language,
            @RequestParam(required = false) String title,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size
    ) {
        log.info("API hit: SEARCH MOVIES");
        return movieService.searchMovies(genre, language, title, page, size);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public String deleteMovie(@PathVariable Long id) {

        log.info("API hit: DELETE MOVIE id={}", id);
        movieService.deleteMovie(id);

        return "Movie deleted successfully";
    }
}