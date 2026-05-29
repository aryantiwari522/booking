package com.booking.service.impl;

import com.booking.dto.request.BookingRequest;
import com.booking.entity.Booking;
import org.springframework.stereotype.Service;

@Service
public class BookingServiceImpl {
    private final com.booking.service.BookingService delegate;

    public BookingServiceImpl(com.booking.service.BookingService delegate) {
        this.delegate = delegate;
    }

    public Booking book(BookingRequest req) {
        return delegate.bookOffering(req.getParentId(), req.getOfferingId());
    }
}
