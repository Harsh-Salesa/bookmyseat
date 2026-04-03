package com.bms.bookmyseat.impl;

import com.bms.bookmyseat.dto.ShowRequest;
import com.bms.bookmyseat.dto.ShowResponse;
import com.bms.bookmyseat.entity.Movie;
import com.bms.bookmyseat.entity.Show;
import com.bms.bookmyseat.entity.Theater;
import com.bms.bookmyseat.exception.MovieNotFoundException;
import com.bms.bookmyseat.exception.ShowAlreadyExistsException;
import com.bms.bookmyseat.exception.ShowNotFoundException;
import com.bms.bookmyseat.exception.TheaterNotFoundException;
import com.bms.bookmyseat.repository.MovieRepository;
import com.bms.bookmyseat.repository.ShowRepository;
import com.bms.bookmyseat.repository.TheaterRepository;
import com.bms.bookmyseat.service.ShowService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
@Slf4j
public class ShowServiceImpl implements ShowService {

    private final ShowRepository showRepository;
    private final MovieRepository movieRepository;
    private final TheaterRepository theaterRepository;

    @Override
    public ShowResponse createShow(ShowRequest request) {

        log.info("Creating show for movieId={} theaterId={}", request.getMovieId(), request.getTheaterId());

        Movie movie = movieRepository.findById(request.getMovieId())
                .orElseThrow(() -> new MovieNotFoundException("Movie not found"));

        Theater theater = theaterRepository.findById(request.getTheaterId())
                .orElseThrow(() -> new TheaterNotFoundException("Theater not found"));

        // 🔥 duplicate check
        boolean exists = showRepository.existsByMovieAndTheaterAndShowDateAndShowTime(
                movie,
                theater,
                request.getShowDate(),
                request.getShowTime()
        );

        if (exists) {
            throw new ShowAlreadyExistsException("Show already exists for this time");
        }

        Show show = Show.builder()
                .movie(movie)
                .theater(theater)
                .showDate(request.getShowDate())
                .showTime(request.getShowTime())
                .build();

        showRepository.save(show);

        log.info("Show created successfully");

        return map(show);
    }

    @Override
    public Page<ShowResponse> getShows(Long movieId, LocalDate date, int page, int size) {

        log.info("Fetching shows for movieId={} date={}", movieId, date);

        Pageable pageable = PageRequest.of(page, size);

        return showRepository
                .findByMovieIdAndShowDate(movieId, date, pageable)
                .map(this::map);
    }

    @Override
    public Page<ShowResponse> searchShows(
            Long movieId,
            Long theaterId,
            LocalDate date,
            int page,
            int size) {

        log.info("Searching shows movieId={} theaterId={} date={}", movieId, theaterId, date);

        Pageable pageable = PageRequest.of(page, size);

        return showRepository
                .findByMovieIdAndTheaterIdAndShowDate(
                        movieId,
                        theaterId,
                        date,
                        pageable
                )
                .map(this::map);
    }

    // 🔥 UPDATE
    @Override
    public ShowResponse updateShow(Long id, ShowRequest request) {

        log.info("Updating show id={}", id);

        Show show = showRepository.findById(id)
                .orElseThrow(() -> new ShowNotFoundException("Show not found"));

        Movie movie = movieRepository.findById(request.getMovieId())
                .orElseThrow(() -> new MovieNotFoundException("Movie not found"));

        Theater theater = theaterRepository.findById(request.getTheaterId())
                .orElseThrow(() -> new TheaterNotFoundException("Theater not found"));

        show.setMovie(movie);
        show.setTheater(theater);
        show.setShowDate(request.getShowDate());
        show.setShowTime(request.getShowTime());

        showRepository.save(show);

        log.info("Show updated successfully");

        return map(show);
    }

    // 🔥 DELETE
    @Override
    public void deleteShow(Long id) {

        log.info("Deleting show id={}", id);

        Show show = showRepository.findById(id)
                .orElseThrow(() -> new ShowNotFoundException("Show not found"));

        showRepository.delete(show);

        log.info("Show deleted successfully");
    }

    // 🔥 MAPPER
    private ShowResponse map(Show show) {
        return ShowResponse.builder()
                .id(show.getId())
                .movieTitle(show.getMovie().getTitle())
                .theaterName(show.getTheater().getName())
                .showDate(show.getShowDate())
                .showTime(show.getShowTime())
                .build();
    }
}