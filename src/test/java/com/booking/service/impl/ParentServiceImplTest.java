package com.booking.service.impl;

import com.booking.entity.Booking;
import com.booking.exception.ResourceNotFoundException;
import com.booking.repository.BookingRepository;
import com.booking.repository.ParentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ParentServiceImplTest {
    @Mock
    private ParentRepository parentRepository;

    @Mock
    private BookingRepository bookingRepository;

    private ParentServiceImpl service;

    @BeforeEach
    void setUp() {
        service = new ParentServiceImpl(parentRepository, bookingRepository);
    }

    @Test
    void deletesBookingWhenItBelongsToParent() {
        Booking booking = new Booking();
        when(bookingRepository.findByIdAndParentId(10L, 1L)).thenReturn(Optional.of(booking));

        service.deleteBooking(1L, 10L);

        verify(bookingRepository).delete(booking);
    }

    @Test
    void throwsNotFoundWhenBookingDoesNotBelongToParent() {
        when(bookingRepository.findByIdAndParentId(10L, 1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.deleteBooking(1L, 10L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Booking not found for parent");
    }
}
