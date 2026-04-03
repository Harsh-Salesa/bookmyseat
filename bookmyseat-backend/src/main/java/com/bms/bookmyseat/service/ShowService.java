package com.bms.bookmyseat.service;

import com.bms.bookmyseat.dto.ShowRequest;
import com.bms.bookmyseat.dto.ShowResponse;
import org.springframework.data.domain.Page;

import java.time.LocalDate;

public interface ShowService {



    // ✅ CREATE
    ShowResponse createShow(ShowRequest request);

    // ✅ READ (basic)
    Page<ShowResponse> getShows(Long movieId, LocalDate date, int page, int size);

    // ✅ SEARCH / FILTER
    Page<ShowResponse> searchShows(
            Long movieId,
            Long theaterId,
            LocalDate date,
            int page,
            int size
    );

    // ✅ UPDATE
    ShowResponse updateShow(Long id, ShowRequest request);

    // ✅ DELETE
    void deleteShow(Long id);
}