package com.bms.bookmyseat.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

 @Entity
    @Table(name = "movies")
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public class Movie {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        private String title;

        private String genre;

        private String language;

        private LocalDate releaseDate;

        private Integer duration;
    }

