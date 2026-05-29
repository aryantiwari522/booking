package com.booking.service.impl;

import com.booking.dto.request.SessionRequest;
import com.booking.entity.Session;
import com.booking.entity.Offering;
import com.booking.repository.OfferingRepository;
import com.booking.repository.SessionRepository;
import com.booking.service.SessionService;
import com.booking.util.TimezoneUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

@Service
public class SessionServiceImpl implements SessionService {
    private final OfferingRepository offeringRepository;
    private final SessionRepository sessionRepository;

    public SessionServiceImpl(OfferingRepository offeringRepository, SessionRepository sessionRepository) {
        this.offeringRepository = offeringRepository;
        this.sessionRepository = sessionRepository;
    }

    @Transactional
    @Override
    public Session addSession(Long offeringId, SessionRequest req) {
        Offering o = offeringRepository.findById(offeringId).orElseThrow(() -> new IllegalArgumentException("Offering not found"));
        Instant start = TimezoneUtil.toUtc(req.getStartLocal(), o.getTimezone());
        Instant end = TimezoneUtil.toUtc(req.getEndLocal(), o.getTimezone());
        if (!start.isBefore(end)) throw new IllegalArgumentException("start must be before end");
        Session s = new Session();
        s.setOffering(o);
        s.setStartAt(start);
        s.setEndAt(end);
        return sessionRepository.save(s);
    }
}
