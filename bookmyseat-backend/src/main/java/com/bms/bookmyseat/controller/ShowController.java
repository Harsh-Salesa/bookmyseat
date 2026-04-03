package com.bms.bookmyseat.controller;


import com.bms.bookmyseat.dto.ShowRequest;
import com.bms.bookmyseat.dto.ShowResponse;
import com.bms.bookmyseat.service.ShowService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/v1/shows")
@RequiredArgsConstructor
@Slf4j
public class ShowController {

    private final ShowService showService;

    // 🔐 ADMIN only
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ShowResponse createShow(@Valid @RequestBody ShowRequest request) {
        log.info("API hit: CREATE SHOW");
        return showService.createShow(request);
    }

    // 👥 USER + ADMIN
    @GetMapping
    public Page<ShowResponse> getShows(
            @RequestParam Long movieId,
            @RequestParam String date,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size
    ) {
        log.info("API hit: GET SHOWS");

        return showService.getShows(
                movieId,
                LocalDate.parse(date),
                page,
                size
        );
    }


    @GetMapping("/search")
    public Page<ShowResponse> searchShows(
            @RequestParam Long movieId,
            @RequestParam Long theaterId,
            @RequestParam String date,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size
    ) {
        return showService.searchShows(
                movieId,
                theaterId,
                LocalDate.parse(date),
                page,
                size
        );
    }
    // 🔥 UPDATE SHOW (ADMIN)
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ShowResponse updateShow(
            @PathVariable Long id,
            @Valid @RequestBody ShowRequest request
    ) {
        log.info("API hit: UPDATE SHOW id={}", id);
        return showService.updateShow(id, request);
    }


    // 🔥 DELETE SHOW (ADMIN)
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public String deleteShow(@PathVariable Long id) {

        log.info("API hit: DELETE SHOW id={}", id);

        showService.deleteShow(id);
        return "Show deleted successfully";
    }
}