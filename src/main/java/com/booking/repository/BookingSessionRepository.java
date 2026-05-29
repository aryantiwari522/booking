package com.booking.repository;

import com.booking.entity.BookingSession;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BookingSessionRepository extends JpaRepository<BookingSession, Long> {
    List<BookingSession> findByBookingParentId(Long parentId);
}
