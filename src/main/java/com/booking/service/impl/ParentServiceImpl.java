package com.booking.service.impl;

import com.booking.dto.response.CourseWithTeacherResponse;
import com.booking.dto.response.OfferingSessionsLocalResponse;
import com.booking.dto.response.SessionLocalTimeResponse;
import com.booking.entity.*;
import com.booking.exception.ResourceNotFoundException;
import com.booking.repository.*;
import com.booking.service.OfferingService;
import com.booking.service.ParentService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ParentServiceImpl implements ParentService {
    private final ParentRepository parentRepository;
    private final BookingRepository bookingRepository;
    private final OfferingService offeringService;
    private final SessionRepository sessionRepository;
    private final CourseRepository courseReposiory;
    private final TeacherRepository teacherRepository;
    public ParentServiceImpl(ParentRepository parentRepository, BookingRepository bookingRepository, OfferingService offeringService, SessionRepository sessionRepository, CourseRepository courseReposiory, TeacherRepository teacherRepository) {
        this.parentRepository = parentRepository;
        this.bookingRepository = bookingRepository;
        this.offeringService = offeringService;
        this.sessionRepository = sessionRepository;
        this.courseReposiory = courseReposiory;
        this.teacherRepository = teacherRepository;
    }

    @Override
    public Parent createParent(String name, String timezone) {
        Parent p = new Parent();
        p.setName(name);
        p.setTimezone(timezone);
        return parentRepository.save(p);
    }

    @Override
    public List<Booking> getBookings(Long parentId) {
        return bookingRepository.findByParentId(parentId);
    }

    @Override
    @Transactional
    public void deleteBooking(Long parentId, Long bookingId) {
        Booking booking = bookingRepository.findByIdAndParentId(bookingId, parentId)
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found for parent"));
        bookingRepository.delete(booking);
    }


    public List<OfferingSessionsLocalResponse> getOfferingSessionsLocalResponses(Long courseId, Long parentId) {
        Parent parent = parentRepository.findById(parentId).orElse(null);
        if (parent == null || parent.getTimezone() == null) {
            throw new IllegalArgumentException("Parent or timezone not found");
        }
        ZoneId zoneId = ZoneId.of(parent.getTimezone());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
        List<Offering> offerings = offeringService.listOfferingsByCourse(courseId);
        Map<Long, List<Session>> sessionsByOffering = sessionRepository.findByOfferingIdIn(
                        offerings.stream().map(Offering::getId).toList()).stream()
                .collect(Collectors.groupingBy(session -> session.getOffering().getId()));
        var result = offerings.stream().map(offering -> {
            List<Session> sessions = sessionsByOffering.getOrDefault(offering.getId(), List.of());
            List<SessionLocalTimeResponse> sessionDtos = sessions.stream().map(session -> new SessionLocalTimeResponse(
                    session.getId(),
                    session.getStartAt().atZone(zoneId).format(formatter),
                    session.getEndAt().atZone(zoneId).format(formatter)
            )).collect(Collectors.toList());
            return new OfferingSessionsLocalResponse(offering.getId(), offering.getTitle(), sessionDtos);
        }).collect(Collectors.toList());
        return result;
    }

    public List<CourseWithTeacherResponse> getCourseWithTeacherResponses() {
        List<Course> courses = courseReposiory.findAll();
        List<CourseWithTeacherResponse> result = courses.stream().map(course -> {
            Optional<Teacher> teacherOpt = teacherRepository.findById(course.getTeacherId());
            String teacherName = teacherOpt.map(Teacher::getName).orElse("Unknown");
            Long teacherId = teacherOpt.map(Teacher::getId).orElse(null);
            return new CourseWithTeacherResponse(course.getId(), course.getTitle(), teacherId, teacherName);
        }).collect(Collectors.toList());
        return result;
    }
}
