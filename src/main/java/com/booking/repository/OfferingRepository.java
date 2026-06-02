package com.booking.repository;

import com.booking.entity.Offering;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OfferingRepository extends JpaRepository<Offering, Long> {
    List<Offering> findByTeacherId(Long teacherId);

    List<Offering> findByCourseId(Long courseId);
}
