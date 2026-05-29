package com.booking.service.impl;

import com.booking.dto.request.CreateOfferingRequest;
import com.booking.entity.Offering;
import com.booking.service.OfferingService;
import org.springframework.stereotype.Service;

/** Delegating wrapper to existing service implementation (keeps structure tidy). */
@Service
public class OfferingServiceImpl {
    private final OfferingService delegate;

    public OfferingServiceImpl(com.booking.service.OfferingService delegate) {
        this.delegate = delegate;
    }

    public Offering createOffering(CreateOfferingRequest req, Long teacherId) {
        CreateOfferingRequest r = new CreateOfferingRequest();
        r.setTitle(req.getTitle());
        r.setCourseId(req.getCourseId());
        r.setTimezone(req.getTimezone());
        r.setTeacherId(teacherId);
        return delegate.createOffering(teacherId,r);
    }
}
