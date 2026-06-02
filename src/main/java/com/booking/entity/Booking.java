package com.booking.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(
        name = "bookings",
        uniqueConstraints = @UniqueConstraint(
                name = "uk_bookings_parent_offering",
                columnNames = {"parent_id", "offering_id"}
        )
)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id", nullable = false)
    private Parent parent;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "offering_id", nullable = false)
    private Offering offering;

    private Instant createdAt;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "booking", orphanRemoval = true)
    @JsonManagedReference
    private List<BookingSession> sessions = new ArrayList<>();
}
