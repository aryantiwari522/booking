package com.booking.repository;

import com.booking.entity.Booking;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BookingRepository extends JpaRepository<Booking, Long> {
    @Override
    @EntityGraph(attributePaths = {"parent", "offering", "sessions"})
    List<Booking> findAll();

    @EntityGraph(attributePaths = {"offering", "sessions"})
    List<Booking> findByParentId(Long parentId);

    Optional<Booking> findByIdAndParentId(Long id, Long parentId);

    boolean existsByParentIdAndOfferingId(Long parentId, Long offeringId);
}
