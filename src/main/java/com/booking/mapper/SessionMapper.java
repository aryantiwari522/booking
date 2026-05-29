package com.booking.mapper;

import com.booking.dto.response.SessionResponse;
import com.booking.entity.Session;

public class SessionMapper {
    public static SessionResponse toResponse(Session s) {
        SessionResponse r = new SessionResponse();
        r.setId(s.getId());
        r.setStartUtc(s.getStartAt().toString());
        r.setEndUtc(s.getEndAt().toString());
        return r;
    }
}
