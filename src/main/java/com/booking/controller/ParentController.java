package com.booking.controller;

import com.booking.dto.request.BookingRequest;
import com.booking.dto.request.CreateUserRequest;
import com.booking.dto.response.BookingResponse;
import com.booking.entity.Booking;
import com.booking.entity.Offering;
import com.booking.entity.Parent;
import com.booking.mapper.BookingMapper;
import com.booking.service.BookingService;
import com.booking.service.OfferingService;
import com.booking.service.ParentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
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
    public ResponseEntity<Parent> createParent(@RequestBody CreateUserRequest req) {
        Parent p = parentService.createParent(req.getName());
        return ResponseEntity.ok(p);
    }

    @GetMapping("/offerings")
    public ResponseEntity<List<Offering>> listOfferings() {
        return ResponseEntity.ok(offeringService.listAll());
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
