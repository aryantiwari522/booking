package com.booking.service;

import com.booking.dto.request.SessionRequest;
import com.booking.entity.Session;

public interface SessionService {
    Session addSession(Long offeringId, SessionRequest req);
}
