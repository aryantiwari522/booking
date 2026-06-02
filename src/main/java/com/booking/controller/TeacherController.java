package com.booking.controller;

import com.booking.dto.request.AddSessionsRequest;
import com.booking.dto.request.CreateCourseRequest;
import com.booking.dto.request.CreateOfferingRequest;
import com.booking.dto.request.CreateUserRequest;
import com.booking.dto.response.CourseResponse;
import com.booking.dto.response.OfferingResponse;
import com.booking.dto.response.SessionResponse;
import com.booking.dto.response.UserResponse;
import com.booking.entity.Offering;
import com.booking.entity.Session;
import com.booking.entity.Teacher;
import com.booking.entity.Course;
import com.booking.mapper.OfferingMapper;
import com.booking.mapper.SessionMapper;
import com.booking.repository.TeacherRepository;
import com.booking.repository.CourseRepository;
import com.booking.service.OfferingService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/teachers")
public class TeacherController {
    private final OfferingService offeringService;
    private final TeacherRepository teacherRepository;
    private final CourseRepository courseRepository;

    public TeacherController(OfferingService offeringService, TeacherRepository teacherRepository, CourseRepository courseRepository) {
        this.offeringService = offeringService;
        this.teacherRepository = teacherRepository;
        this.courseRepository = courseRepository;
    }

    @PostMapping
    public ResponseEntity<UserResponse> createTeacher(@Valid @RequestBody CreateUserRequest req) {
        Teacher t = new Teacher();
        t.setName(req.getName());
        Teacher saved = teacherRepository.save(t);
        return ResponseEntity.ok(new UserResponse(saved.getId(), saved.getName(), null));
    }

    @PostMapping("/{teacherId}/offerings")
    public ResponseEntity<OfferingResponse> createOffering(@PathVariable Long teacherId, @Valid @RequestBody CreateOfferingRequest req) {
        Offering o = offeringService.createOffering(teacherId, req);
        return ResponseEntity.ok(OfferingMapper.toResponse(o, List.of()));
    }

    @PostMapping("/{teacherId}/offerings/{offeringId}/sessions")
    public ResponseEntity<List<SessionResponse>> addSessions(@PathVariable Long teacherId, @PathVariable Long offeringId, @Valid @RequestBody AddSessionsRequest req) {
        List<Session> sessions = offeringService.addSessions(teacherId, offeringId, req);
        return ResponseEntity.ok(sessions.stream().map(SessionMapper::toResponse).toList());
    }

    @PostMapping("/{teacherId}/courses")
    public ResponseEntity<CourseResponse> createCourse(@PathVariable Long teacherId, @Valid @RequestBody CreateCourseRequest req) {
        // validate teacher exists
        Teacher t = teacherRepository.findById(teacherId).orElseThrow(() -> new IllegalArgumentException("Teacher not found"));
        Course c = new Course();
        c.setTitle(req.getTitle());
        c.setTeacherId(t.getId());
        Course saved = courseRepository.save(c);
        return ResponseEntity.ok(new CourseResponse(saved.getId(), saved.getTitle(), saved.getTeacherId()));
    }

    @GetMapping("/{teacherId}/offerings")
    public ResponseEntity<List<OfferingResponse>> listOfferings(@PathVariable Long teacherId) {
        List<OfferingResponse> responses = offeringService.listOfferingsByTeacher(teacherId).stream()
                .map(offering -> OfferingMapper.toResponse(offering, offeringService.getSessionsForOffering(offering.getId())))
                .toList();
        return ResponseEntity.ok(responses);
    }
}
