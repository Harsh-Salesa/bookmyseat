package com.bms.bookmyseat.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;


    @Data
    @Builder
    public class MovieResponse {

        private Long id;
        private String title;
        private String genre;
        private String language;
        private LocalDate releaseDate;
        private Integer duration;
    }

