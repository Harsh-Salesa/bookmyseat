package com.bms.bookmyseat.controller;

import com.bms.bookmyseat.dto.TheaterRequest;
import com.bms.bookmyseat.dto.TheaterResponse;
import com.bms.bookmyseat.service.TheaterService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/theaters")
@RequiredArgsConstructor
@Slf4j
public class TheaterController {

    private final TheaterService theaterService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public TheaterResponse addTheater(@Valid @RequestBody TheaterRequest request) {
        log.info("API hit: ADD THEATER");
        return theaterService.addTheater(request);
    }

    @GetMapping
    public List<TheaterResponse> getAllTheaters() {
        log.info("API hit: GET THEATERS");
        return theaterService.getAllTheaters();
    }
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public TheaterResponse updateTheater(
            @PathVariable Long id,
            @Valid @RequestBody TheaterRequest request) {

        log.info("API hit: UPDATE THEATER id={}", id);
        return theaterService.updateTheater(id, request);
    }

    // Delete
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public String deleteTheater(@PathVariable Long id) {

        log.info("API hit: DELETE THEATER id={}", id);

        theaterService.deleteTheater(id);
        return "Theater deleted successfully";
    }

    @GetMapping("/search")
    public Page<TheaterResponse> searchTheaters(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String location,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size
    ) {
        log.info("API hit: SEARCH THEATERS");
        return theaterService.searchTheaters(name, location, page, size);
    }
}
