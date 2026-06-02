package com.booking.repository;

import com.booking.entity.Session;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SessionRepository extends JpaRepository<Session, Long> {
    List<Session> findByOfferingId(Long offeringId);

    List<Session> findByOfferingIdIn(List<Long> offeringIds);
}
