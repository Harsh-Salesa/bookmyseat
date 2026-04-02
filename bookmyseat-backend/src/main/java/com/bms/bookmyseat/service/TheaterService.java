package com.bms.bookmyseat.service;

import com.bms.bookmyseat.dto.TheaterRequest;
import com.bms.bookmyseat.dto.TheaterResponse;
import org.springframework.data.domain.Page;

import java.util.List;

public interface TheaterService {

    TheaterResponse addTheater(TheaterRequest request);

    List<TheaterResponse> getAllTheaters();
    TheaterResponse updateTheater(Long id, TheaterRequest request);
    void deleteTheater(Long id);
    Page<TheaterResponse> searchTheaters(String name, String location, int page, int size);
}
