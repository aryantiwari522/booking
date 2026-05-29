package com.booking.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "bookings")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private Parent parent;

    @ManyToOne(fetch = FetchType.LAZY)
    private Offering offering;

    private Instant createdAt;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "booking")
    private List<BookingSession> sessions = new ArrayList<>();
}
