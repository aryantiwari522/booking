package com.booking.controller;

import com.booking.dto.request.BookingRequest;
import com.booking.dto.request.CreateUserRequest;
import com.booking.dto.response.BookingResponse;
import com.booking.dto.response.CourseWithTeacherResponse;
import com.booking.dto.response.OfferingResponse;
import com.booking.dto.response.OfferingSessionsLocalResponse;
import com.booking.dto.response.SessionLocalTimeResponse;
import com.booking.dto.response.UserResponse;
import com.booking.entity.*;
import com.booking.mapper.BookingMapper;
import com.booking.mapper.OfferingMapper;
import com.booking.repository.CourseRepository;
import com.booking.repository.ParentRepository;
import com.booking.repository.SessionRepository;
import com.booking.repository.TeacherRepository;
import com.booking.service.BookingService;
import com.booking.service.OfferingService;
import com.booking.service.ParentService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/parents")
public class ParentController {
    private final BookingService bookingService;
    private final OfferingService offeringService;
    private final ParentService parentService;

    public ParentController(BookingService bookingService, OfferingService offeringService, ParentService parentService) {
        this.bookingService = bookingService;
        this.offeringService = offeringService;
        this.parentService = parentService;
    }

    @PostMapping
    public ResponseEntity<UserResponse> createParent(@Valid @RequestBody CreateUserRequest req) {
        Parent p = parentService.createParent(req.getName(), req.getTimezone());
        return ResponseEntity.ok(new UserResponse(p.getId(), p.getName(), p.getTimezone()));
    }

    @GetMapping("/courses")
    public ResponseEntity<List<CourseWithTeacherResponse>> listCoursesWithTeachers() {

        List<CourseWithTeacherResponse> result = parentService.getCourseWithTeacherResponses();
        return ResponseEntity.ok(result);
    }

    @GetMapping("/offerings/{courseId}/parent/{parentId}")
    public ResponseEntity<List<OfferingSessionsLocalResponse>> listOfferingsByCourseWithSessionsInParentTimezone(@PathVariable Long courseId, @PathVariable Long parentId) {
        var result = parentService.getOfferingSessionsLocalResponses(courseId, parentId);
        return ResponseEntity.ok(result);
    }



    @GetMapping("/{courseId}/offerings")
    public ResponseEntity<List<OfferingResponse>> listOfferings(@PathVariable Long courseId) {
        List<OfferingResponse> offerings = offeringService.listOfferingsByCourse(courseId).stream()
                .map(offering -> OfferingMapper.toResponse(offering, offeringService.getSessionsForOffering(offering.getId())))
                .toList();
        return ResponseEntity.ok(offerings);
    }

    @PostMapping("/bookings")
    public ResponseEntity<BookingResponse> book(@Valid @RequestBody BookingRequest req) {
        Booking b = bookingService.bookOffering(req.getParentId(), req.getOfferingId());
        return ResponseEntity.ok(BookingMapper.toResponse(b));
    }

    @GetMapping("/{parentId}/bookings")
    public ResponseEntity<List<BookingResponse>> getBookings(@PathVariable Long parentId) {
        List<Booking> bookings = parentService.getBookings(parentId);
        List<BookingResponse> resp = bookings.stream().map(BookingMapper::toResponse).collect(Collectors.toList());
        return ResponseEntity.ok(resp);
    }

    @DeleteMapping("/{parentId}/bookings/{bookingId}")
    public ResponseEntity<Void> deleteBooking(@PathVariable Long parentId, @PathVariable Long bookingId) {
        parentService.deleteBooking(parentId, bookingId);
        return ResponseEntity.noContent().build();
    }
}
