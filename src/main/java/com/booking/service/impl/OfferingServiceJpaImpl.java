package com.booking.service.impl;

import com.booking.dto.request.AddSessionsRequest;
import com.booking.dto.request.CreateOfferingRequest;
import com.booking.dto.request.SessionRequest;
import com.booking.entity.*;
import com.booking.repository.*;
import com.booking.service.OfferingService;
import com.booking.util.TimezoneUtil;
import com.booking.validation.TimezoneValidator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Service
public class OfferingServiceJpaImpl implements OfferingService {
    private final OfferingRepository offeringRepository;
    private final TeacherRepository teacherRepository;
    private final CourseRepository courseRepository;
    private final SessionRepository sessionRepository;

    public OfferingServiceJpaImpl(OfferingRepository offeringRepository, TeacherRepository teacherRepository, CourseRepository courseRepository, SessionRepository sessionRepository) {
        this.offeringRepository = offeringRepository;
        this.teacherRepository = teacherRepository;
        this.courseRepository = courseRepository;
        this.sessionRepository = sessionRepository;
    }

    @Override
    public Offering createOffering(Long teacherId, CreateOfferingRequest req) {
        if (req.getTimezone() == null || !TimezoneValidator.isValid(req.getTimezone())) {
            throw new IllegalArgumentException("Invalid timezone");
        }
        Teacher t = teacherRepository.findById(teacherId).orElseThrow(() -> new IllegalArgumentException("Teacher not found"));
        Offering o = new Offering();
        o.setTitle(req.getTitle());
        o.setTimezone(req.getTimezone());
        o.setTeacher(t);
        if (req.getCourseId() != null) {
            Course c = courseRepository.findById(req.getCourseId()).orElse(null);
            o.setCourse(c);
        }
        else{
            Course c = new Course();
            c.setTitle(req.getTitle());
            c.setTeacherId(t.getId());
            Course saved = courseRepository.save(c);
            o.setCourse(saved);
        }
        return offeringRepository.save(o);
    }

    @Override
    public List<Offering> listAll() {
        return offeringRepository.findAll();
    }

    @Override
    public List<Offering> listOfferingsByTeacher(Long teacherId) {
        return offeringRepository.findByTeacherId(teacherId);
    }

    @Override
    public List<Session> getSessionsForOffering(Long offeringId) {
        return sessionRepository.findByOfferingId(offeringId);
    }

    @Transactional
    @Override
    public List<Session> addSessions(Long teacherId, Long offeringId, AddSessionsRequest req) {
        Offering o = offeringRepository.findById(offeringId).orElseThrow(() -> new IllegalArgumentException("Offering not found"));
        Teacher t = teacherRepository.findById(teacherId).orElseThrow(() -> new IllegalArgumentException("Teacher not found"));

        if (o.getTeacher() == null || !o.getTeacher().getId().equals(teacherId)) {
            throw new IllegalArgumentException("Teacher does not own offering");
        }
        List<Session> saved = new ArrayList<>();
        if (req.getSessions() == null) return saved;
        for (SessionRequest sr : req.getSessions()) {
            Instant start = TimezoneUtil.toUtc(sr.getStartLocal(), o.getTimezone());
            Instant end = TimezoneUtil.toUtc(sr.getEndLocal(), o.getTimezone());
            if (!start.isBefore(end)) throw new IllegalArgumentException("start must be before end");
            Session s = new Session();
            s.setOffering(o);
            s.setStartAt(start);
            s.setEndAt(end);
            saved.add(sessionRepository.save(s));
        }
        return saved;
    }
}
