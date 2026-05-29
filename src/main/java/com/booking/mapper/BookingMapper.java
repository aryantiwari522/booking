package com.booking.mapper;

import com.booking.dto.response.BookingResponse;
import com.booking.dto.response.SessionResponse;
import com.booking.entity.Booking;

import java.util.stream.Collectors;

public class BookingMapper {
    public static BookingResponse toResponse(Booking b) {
        BookingResponse r = new BookingResponse();
        r.setId(b.getId());
        r.setParentId(b.getParent().getId());
        r.setOfferingId(b.getOffering().getId());
        r.setSessions(b.getSessions().stream().map(s -> {
            SessionResponse sr = new SessionResponse();
            sr.setId(s.getId());
            sr.setStartUtc(s.getStartAt().toString());
            sr.setEndUtc(s.getEndAt().toString());
            return sr;
        }).collect(Collectors.toList()));
        return r;
    }
}
