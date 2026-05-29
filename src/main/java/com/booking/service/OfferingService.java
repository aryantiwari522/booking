package com.booking.service;

import com.booking.dto.request.AddSessionsRequest;
import com.booking.dto.request.CreateOfferingRequest;
import com.booking.entity.Offering;
import com.booking.entity.Session;

import java.util.List;

public interface OfferingService {
    Offering createOffering(Long teacherId, CreateOfferingRequest req);
    List<Offering> listAll();
    List<Offering> listOfferingsByTeacher(Long teacherId);
    List<Session> getSessionsForOffering(Long offeringId);
    List<Session> addSessions(Long teacherId, Long offeringId, AddSessionsRequest req);
}
