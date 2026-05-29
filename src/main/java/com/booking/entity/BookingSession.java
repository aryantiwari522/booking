package com.booking.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

@Entity
@Table(name = "booking_sessions")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookingSession {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "booking_id")
    private Booking booking;

    private Instant startAt;
    private Instant endAt;

    private Long sessionRefId;
}
