package com.bms.bookmyseat.impl;

import com.bms.bookmyseat.dto.MovieRequest;
import com.bms.bookmyseat.dto.MovieResponse;
import com.bms.bookmyseat.entity.Movie;
import com.bms.bookmyseat.repository.MovieRepository;
import com.bms.bookmyseat.service.MovieService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import org.springframework.data.domain.Pageable;

@Service
@RequiredArgsConstructor
@Slf4j
public class MovieServiceImpl implements MovieService {

    private final MovieRepository movieRepository;

    @Override
    public MovieResponse addMovie(MovieRequest request) {

        log.info("Adding movie: {}", request.getTitle());

        Movie movie = Movie.builder()
                .title(request.getTitle())
                .genre(request.getGenre())
                .language(request.getLanguage())
                .releaseDate(request.getReleaseDate())
                .duration(request.getDuration())
                .build();

        movieRepository.save(movie);

        log.info("Movie saved successfully: {}", movie.getTitle());

        return map(movie);
    }

    @Override
    public Page<MovieResponse> getAllMovies(int page, int size) {

        log.info("Fetching movies: page={}, size={}", page, size);

        Pageable pageable = PageRequest.of(page, size, Sort.by("releaseDate").descending());

        return movieRepository.findAll(pageable)
                .map(this::map);
    }

    private MovieResponse map(Movie movie) {
        return MovieResponse.builder()
                .id(movie.getId())
                .title(movie.getTitle())
                .genre(movie.getGenre())
                .language(movie.getLanguage())
                .releaseDate(movie.getReleaseDate())
                .duration(movie.getDuration())
                .build();
    }
}
