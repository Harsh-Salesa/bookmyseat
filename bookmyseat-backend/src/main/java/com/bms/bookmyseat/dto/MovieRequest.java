package com.bms.bookmyseat.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;

@Data
public class MovieRequest {
        @NotBlank(message = "Title is required")
        private String title;

        @NotBlank(message = "Genre is required")
        private String genre;

        @NotBlank(message = "Language is required")
        private String language;

        @NotNull(message = "Release date is required")
        private LocalDate releaseDate;

        @Min(value = 1, message = "Duration must be positive")
        private Integer duration;
    }

