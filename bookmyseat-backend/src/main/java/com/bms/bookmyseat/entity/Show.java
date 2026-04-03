package com.bms.bookmyseat.entity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "shows")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Show {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 🔥 Many shows → one movie
    @ManyToOne
    @JoinColumn(name = "movie_id", nullable = false)
    private Movie movie;

    // 🔥 Many shows → one theater
    @ManyToOne
    @JoinColumn(name = "theater_id", nullable = false)
    private Theater theater;

    private LocalDate showDate;

    private LocalTime showTime;

    @Column(nullable = false)
    private Double regularPrice;

    @Column(nullable = false)
    private Double premiumPrice;

    @Column(nullable = false)
    private Double vipPrice;
}

