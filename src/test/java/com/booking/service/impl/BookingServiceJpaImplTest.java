package com.booking.service.impl;

import com.booking.entity.Booking;
import com.booking.entity.BookingSession;
import com.booking.entity.Offering;
import com.booking.entity.Parent;
import com.booking.entity.Session;
import com.booking.exception.BookingConflictException;
import com.booking.exception.DuplicateBookingException;
import com.booking.exception.ResourceNotFoundException;
import com.booking.repository.BookingRepository;
import com.booking.repository.OfferingRepository;
import com.booking.repository.ParentRepository;
import com.booking.repository.SessionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BookingServiceJpaImplTest {
    @Mock
    private BookingRepository bookingRepository;

    @Mock
    private ParentRepository parentRepository;

    @Mock
    private OfferingRepository offeringRepository;

    @Mock
    private SessionRepository sessionRepository;

    private BookingServiceJpaImpl service;

    @BeforeEach
    void setUp() {
        service = new BookingServiceJpaImpl(bookingRepository, parentRepository, offeringRepository, sessionRepository);
    }

    @Test
    void throwsNotFoundWhenParentDoesNotExist() {
        when(parentRepository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.bookOffering(1L, 10L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Parent not found");
    }

    @Test
    void rejectsDuplicateBookingBeforeSaving() {
        Parent parent = parent(1L);
        Offering offering = offering(10L);
        when(parentRepository.findById(1L)).thenReturn(Optional.of(parent));
        when(offeringRepository.findById(10L)).thenReturn(Optional.of(offering));
        when(bookingRepository.existsByParentIdAndOfferingId(1L, 10L)).thenReturn(true);

        assertThatThrownBy(() -> service.bookOffering(1L, 10L))
                .isInstanceOf(DuplicateBookingException.class);
        verify(bookingRepository, never()).saveAndFlush(any());
    }

    @Test
    void rejectsTimeConflictWithExistingBooking() {
        Parent parent = parent(1L);
        Offering offering = offering(10L);
        when(parentRepository.findById(1L)).thenReturn(Optional.of(parent));
        when(offeringRepository.findById(10L)).thenReturn(Optional.of(offering));
        when(bookingRepository.existsByParentIdAndOfferingId(1L, 10L)).thenReturn(false);
        when(bookingRepository.findByParentId(1L)).thenReturn(List.of(existingBooking()));
        when(sessionRepository.findByOfferingId(10L)).thenReturn(List.of(session(99L,
                "2026-06-02T10:30:00Z",
                "2026-06-02T11:30:00Z")));

        assertThatThrownBy(() -> service.bookOffering(1L, 10L))
                .isInstanceOf(BookingConflictException.class);
        verify(bookingRepository, never()).saveAndFlush(any());
    }

    @Test
    void createsBookingWithCopiedSessions() {
        Parent parent = parent(1L);
        Offering offering = offering(10L);
        Session session = session(99L, "2026-06-02T10:00:00Z", "2026-06-02T11:00:00Z");
        when(parentRepository.findById(1L)).thenReturn(Optional.of(parent));
        when(offeringRepository.findById(10L)).thenReturn(Optional.of(offering));
        when(bookingRepository.existsByParentIdAndOfferingId(1L, 10L)).thenReturn(false);
        when(bookingRepository.findByParentId(1L)).thenReturn(List.of());
        when(sessionRepository.findByOfferingId(10L)).thenReturn(List.of(session));
        when(bookingRepository.saveAndFlush(any(Booking.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Booking booking = service.bookOffering(1L, 10L);

        assertThat(booking.getParent()).isEqualTo(parent);
        assertThat(booking.getOffering()).isEqualTo(offering);
        assertThat(booking.getSessions()).hasSize(1);
        assertThat(booking.getSessions().getFirst().getSessionRefId()).isEqualTo(99L);
    }

    private static Parent parent(Long id) {
        Parent parent = new Parent();
        parent.setId(id);
        return parent;
    }

    private static Offering offering(Long id) {
        Offering offering = new Offering();
        offering.setId(id);
        return offering;
    }

    private static Session session(Long id, String start, String end) {
        Session session = new Session();
        session.setId(id);
        session.setStartAt(Instant.parse(start));
        session.setEndAt(Instant.parse(end));
        return session;
    }

    private static Booking existingBooking() {
        Booking booking = new Booking();
        BookingSession session = new BookingSession();
        session.setStartAt(Instant.parse("2026-06-02T10:00:00Z"));
        session.setEndAt(Instant.parse("2026-06-02T11:00:00Z"));
        booking.getSessions().add(session);
        return booking;
    }
}
