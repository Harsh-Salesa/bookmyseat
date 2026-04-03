package com.bms.bookmyseat.dto;

import lombok.*;

import java.time.*;

@Data
@Builder
public class ShowResponse {

    private Long id;

    private String movieTitle;

    private String theaterName;

    private LocalDate showDate;

    private LocalTime showTime;
}
