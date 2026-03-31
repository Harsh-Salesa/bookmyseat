package com.bms.bookmyseat.impl;

import com.bms.bookmyseat.dto.MovieRequest;
import com.bms.bookmyseat.dto.MovieResponse;
import com.bms.bookmyseat.entity.Movie;
import com.bms.bookmyseat.exception.MovieAlreadyExistsException;
import com.bms.bookmyseat.exception.MovieNotFoundException;
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
        if (movieRepository.existsByTitle(request.getTitle())) {
            log.warn("Movie already exists: {}", request.getTitle());
            throw new MovieAlreadyExistsException("Movie with this title already exists");
        }

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

    @Override
    public Page<MovieResponse> searchMovies(String genre, String language, String title, int page, int size) {

        log.info("Searching movies: genre={}, language={}, title={}", genre, language, title);

        Pageable pageable = PageRequest.of(page, size, Sort.by("releaseDate").descending());

        Page<Movie> movies = movieRepository
                .findByGenreContainingIgnoreCaseAndLanguageContainingIgnoreCaseAndTitleContainingIgnoreCase(
                        genre == null ? "" : genre,
                        language == null ? "" : language,
                        title == null ? "" : title,
                        pageable
                );

        return movies.map(this::map);
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
    @Override
    public MovieResponse updateMovie(Long id, MovieRequest request) {

        log.info("Updating movie id={}", id);

        Movie movie = movieRepository.findById(id)
                .orElseThrow(() -> new MovieNotFoundException("Movie not found"));

        movie.setTitle(request.getTitle());
        movie.setGenre(request.getGenre());
        movie.setLanguage(request.getLanguage());
        movie.setReleaseDate(request.getReleaseDate());
        movie.setDuration(request.getDuration());

        movieRepository.save(movie);

        log.info("Movie updated successfully: {}", movie.getTitle());

        return map(movie);
    }
    @Override
    public void deleteMovie(Long id) {

        log.info("Deleting movie id={}", id);

        Movie movie = movieRepository.findById(id)
                .orElseThrow(() -> new MovieNotFoundException("Movie not found"));

        movieRepository.delete(movie);

        log.info("Movie deleted successfully: {}", movie.getTitle());
    }
}
