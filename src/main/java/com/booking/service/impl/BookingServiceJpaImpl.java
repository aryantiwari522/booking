package com.booking.service.impl;

import com.booking.entity.*;
import com.booking.exception.BookingConflictException;
import com.booking.exception.DuplicateBookingException;
import com.booking.exception.ResourceNotFoundException;
import com.booking.repository.*;
import com.booking.service.BookingService;
import com.booking.util.SessionOverlapUtil;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;

@Service
public class BookingServiceJpaImpl implements BookingService {
    private final BookingRepository bookingRepository;
    private final ParentRepository parentRepository;
    private final OfferingRepository offeringRepository;
    private final SessionRepository sessionRepository;

    public BookingServiceJpaImpl(BookingRepository bookingRepository, ParentRepository parentRepository, OfferingRepository offeringRepository, SessionRepository sessionRepository) {
        this.bookingRepository = bookingRepository;
        this.parentRepository = parentRepository;
        this.offeringRepository = offeringRepository;
        this.sessionRepository = sessionRepository;
    }

    @Override
    @Transactional(isolation = Isolation.SERIALIZABLE)
    public Booking bookOffering(Long parentId, Long offeringId) {
        Parent parent = parentRepository.findById(parentId)
                .orElseThrow(() -> new ResourceNotFoundException("Parent not found"));
        Offering offering = offeringRepository.findById(offeringId)
                .orElseThrow(() -> new ResourceNotFoundException("Offering not found"));

        if (bookingRepository.existsByParentIdAndOfferingId(parentId, offeringId)) {
            throw new DuplicateBookingException("Parent already booked this offering");
        }

        List<Booking> existingForParent = bookingRepository.findByParentId(parentId);
        List<Session> offeringSessions = sessionRepository.findByOfferingId(offeringId);
        for (Booking b : existingForParent) {
            for (BookingSession bs : b.getSessions()) {
                for (Session s : offeringSessions) {
                    if (SessionOverlapUtil.overlaps(bs.getStartAt(), bs.getEndAt(), s.getStartAt(), s.getEndAt())) {
                        throw new BookingConflictException("Time conflict with existing booking");
                    }
                }
            }
        }

        Booking booking = new Booking();
        booking.setParent(parent);
        booking.setOffering(offering);
        booking.setCreatedAt(Instant.now());

        for (Session s : offeringSessions) {
            BookingSession bs = new BookingSession();
            bs.setBooking(booking);
            bs.setStartAt(s.getStartAt());
            bs.setEndAt(s.getEndAt());
            bs.setSessionRefId(s.getId());
            booking.getSessions().add(bs);
        }

        try {
            return bookingRepository.saveAndFlush(booking);
        } catch (DataIntegrityViolationException ex) {
            throw new DuplicateBookingException("Parent already booked this offering");
        }
    }
}
