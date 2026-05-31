package com.booking.controller;

import com.booking.dto.request.BookingRequest;
import com.booking.dto.request.CreateUserRequest;
import com.booking.dto.response.BookingResponse;
import com.booking.dto.response.CourseWithTeacherResponse;
import com.booking.dto.response.SessionLocalTimeResponse;
import com.booking.entity.*;
import com.booking.mapper.BookingMapper;
import com.booking.repository.CourseRepository;
import com.booking.repository.ParentRepository;
import com.booking.repository.SessionRepository;
import com.booking.repository.TeacherRepository;
import com.booking.service.BookingService;
import com.booking.service.OfferingService;
import com.booking.service.ParentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/parents")
public class ParentController {
    private final BookingService bookingService;
    private final OfferingService offeringService;
    private final ParentService parentService;
    private final CourseRepository courseReposiory;
    private final TeacherRepository teacherRepository;
    private final ParentRepository parentRepository;
    private final SessionRepository sessionRepository;

    public ParentController(BookingService bookingService, OfferingService offeringService, ParentService parentService, CourseRepository courseReposiory, TeacherRepository teacherRepository, ParentRepository parentRepository, SessionRepository sessionRepository) {
        this.bookingService = bookingService;
        this.offeringService = offeringService;
        this.parentService = parentService;
        this.courseReposiory = courseReposiory;
        this.teacherRepository = teacherRepository;
        this.parentRepository = parentRepository;
        this.sessionRepository = sessionRepository;
    }

    @PostMapping
    public ResponseEntity<Parent> createParent(@RequestBody CreateUserRequest req) {
        Parent p = parentService.createParent(req.getName(), req.getTimezone());
        return ResponseEntity.ok(p);
    }

    @GetMapping("/courses")
    public ResponseEntity<List<CourseWithTeacherResponse>> listCoursesWithTeachers() {

        List<Course> courses = courseReposiory.findAll();
        List<CourseWithTeacherResponse> result = courses.stream().map(course -> {
            Optional<Teacher> teacherOpt = teacherRepository.findById(course.getTeacherId());
            String teacherName = teacherOpt.map(Teacher::getName).orElse("Unknown");
            Long teacherId = teacherOpt.map(Teacher::getId).orElse(null);
            return new CourseWithTeacherResponse(course.getId(), course.getTitle(), teacherId, teacherName);
        }).collect(Collectors.toList());
        return ResponseEntity.ok(result);
    }

    @GetMapping("/offerings/{courseId}/parent/{parentId}")
    public ResponseEntity<?> listOfferingsByCourseWithSessionsInParentTimezone(@PathVariable Long courseId, @PathVariable Long parentId) {
        Parent parent = parentRepository.findById(parentId).orElse(null);
        if (parent == null || parent.getTimezone() == null) {
            return ResponseEntity.badRequest().body("Parent or timezone not found");
        }
        ZoneId zoneId = ZoneId.of(parent.getTimezone());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
        List<Offering> offerings = offeringService.listAll().stream()
                .filter(offering -> offering.getCourse() != null && offering.getCourse().getId().equals(courseId))
                .toList();
        var result = offerings.stream().map(offering -> {
            List<Session> sessions = sessionRepository.findByOfferingId(offering.getId());
            List<SessionLocalTimeResponse> sessionDtos = sessions.stream().map(session -> new SessionLocalTimeResponse(
                    session.getId(),
                    session.getStartAt().atZone(zoneId).format(formatter),
                    session.getEndAt().atZone(zoneId).format(formatter)
            )).collect(Collectors.toList());
            return new Object() {
                public final Long offeringId = offering.getId();
                public final String offeringTitle = offering.getTitle();
                public final List<SessionLocalTimeResponse> sessions = sessionDtos;
            };
        }).collect(Collectors.toList());
        return ResponseEntity.ok(result);
    }

    @GetMapping("/{courseId}/offerings")
    public ResponseEntity<List<Offering>> listOfferings(@PathVariable Long courseId) {
        List<Offering> offerings = offeringService.listAll().stream()
                .filter(offering -> offering.getCourse() != null && offering.getCourse().getId().equals(courseId))
                .collect(Collectors.toList());
        return ResponseEntity.ok(offerings);
    }

    @PostMapping("/bookings")
    public ResponseEntity<BookingResponse> book(@RequestBody BookingRequest req) {
        Booking b = bookingService.bookOffering(req.getParentId(), req.getOfferingId());
        return ResponseEntity.ok(BookingMapper.toResponse(b));
    }

    @GetMapping("/{parentId}/bookings")
    public ResponseEntity<List<BookingResponse>> getBookings(@PathVariable Long parentId) {
        List<Booking> bookings = parentService.getBookings(parentId);
        List<BookingResponse> resp = bookings.stream().map(BookingMapper::toResponse).collect(Collectors.toList());
        return ResponseEntity.ok(resp);
    }
}
