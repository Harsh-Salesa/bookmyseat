package com.bms.bookmyseat.entity;

import jakarta.persistence.*;
import lombok.*;


@Entity
@Table(name = "seats")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Seat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 🔥 Many seats → one show
    @ManyToOne
    @JoinColumn(name = "show_id", nullable = false)
    private Show show;

    private String seatNumber;

    @Enumerated(EnumType.STRING)
    private SeatType type;

    @Enumerated(EnumType.STRING)
    private SeatStatus status;

    @Version  // 💣 concurrency control
    private Long version;

    private double price;

    public enum SeatType {
        REGULAR,
        PREMIUM,
        VIP
    }
    public enum SeatStatus {
        AVAILABLE,
        LOCKED,
        BOOKED
    }
}