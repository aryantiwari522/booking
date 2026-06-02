package com.booking.controller;

import com.booking.dto.response.BookingResponse;
import com.booking.mapper.BookingMapper;
import com.booking.repository.BookingRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/bookings")
public class BookingController {
    private final BookingRepository bookingRepository;

    public BookingController(BookingRepository bookingRepository) {
        this.bookingRepository = bookingRepository;
    }

    @GetMapping
    public ResponseEntity<List<BookingResponse>> listAll() {
        return ResponseEntity.ok(bookingRepository.findAll().stream().map(BookingMapper::toResponse).toList());
    }
}
