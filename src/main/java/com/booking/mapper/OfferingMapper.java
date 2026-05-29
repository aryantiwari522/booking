package com.booking.mapper;

import com.booking.dto.response.OfferingResponse;
import com.booking.dto.response.SessionResponse;
import com.booking.entity.Offering;
import com.booking.entity.Session;

import java.util.stream.Collectors;

public class OfferingMapper {
    public static OfferingResponse toResponse(Offering o, java.util.List<Session> sessions) {
        OfferingResponse r = new OfferingResponse();
        r.setId(o.getId());
        r.setTitle(o.getTitle());
        r.setTimezone(o.getTimezone());
        r.setSessions(sessions.stream().map(OfferingMapper::toSession).collect(Collectors.toList()));
        return r;
    }

    public static SessionResponse toSession(Session s) {
        SessionResponse sr = new SessionResponse();
        sr.setId(s.getId());
        sr.setStartUtc(s.getStartAt().toString());
        sr.setEndUtc(s.getEndAt().toString());
        return sr;
    }
}
