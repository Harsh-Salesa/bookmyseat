package com.bms.bookmyseat.impl;

import com.bms.bookmyseat.dto.TheaterRequest;
import com.bms.bookmyseat.dto.TheaterResponse;
import com.bms.bookmyseat.entity.Theater;
import com.bms.bookmyseat.exception.MovieAlreadyExistsException;
import com.bms.bookmyseat.exception.TheaterNotFoundException;
import com.bms.bookmyseat.repository.TheaterRepository;
import com.bms.bookmyseat.service.TheaterService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class TheaterServiceImpl implements TheaterService {

    private final TheaterRepository theaterRepository;

    @Override
    public TheaterResponse addTheater(TheaterRequest request) {

        log.info("Adding theater: {}", request.getName());
        if (theaterRepository.existsByName(request.getName())) {
            log.warn("Theater already exists: {}", request.getName());
            throw new MovieAlreadyExistsException("Theater with this title already exists");
        }
        Theater theater = Theater.builder()
                .name(request.getName())
                .location(request.getLocation())
                .build();

        theaterRepository.save(theater);

        log.info("Theater saved: {}", theater.getName());

        return map(theater);
    }

    @Override
    public List<TheaterResponse> getAllTheaters() {

        log.info("Fetching all theaters");

        return theaterRepository.findAll()
                .stream()
                .map(this::map)
                .toList();
    }

    private TheaterResponse map(Theater theater) {
        return TheaterResponse.builder()
                .id(theater.getId())
                .name(theater.getName())
                .location(theater.getLocation())
                .build();
    }
    @Override
    public TheaterResponse updateTheater(Long id, TheaterRequest request) {

        Theater theater = theaterRepository.findById(id)
                .orElseThrow(() -> new TheaterNotFoundException("Theater not found"));

        theater.setName(request.getName());
        theater.setLocation(request.getLocation());

        theaterRepository.save(theater);

        log.info("Theater updated: {}", theater.getName());

        return map(theater);
    }
    @Override
    public void deleteTheater(Long id) {

        Theater theater = theaterRepository.findById(id)
                .orElseThrow(() -> new TheaterNotFoundException("Theater not found"));

        theaterRepository.delete(theater);

        log.info("Theater deleted: {}", theater.getName());
    }
    @Override
    public Page<TheaterResponse> searchTheaters(String name, String location, int page, int size) {

        log.info("Searching theaters: name={}, location={}", name, location);

        Pageable pageable = PageRequest.of(page, size);

        Page<Theater> theaters = theaterRepository
                .findByNameContainingIgnoreCaseAndLocationContainingIgnoreCase(
                        name == null ? "" : name,
                        location == null ? "" : location,
                        pageable
                );

        return theaters.map(this::map);
    }
}
