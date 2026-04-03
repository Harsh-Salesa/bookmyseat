package com.bms.bookmyseat.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

 @Data
    @Builder
    public class SeatResponse {

        private Long id;
        private String seatNumber;
        private String type;
        private String status;
        @NotNull
        private Double regularPrice;
        @NotNull
        private Double premiumPrice;
        @NotNull
        private Double vipPrice;
    }
