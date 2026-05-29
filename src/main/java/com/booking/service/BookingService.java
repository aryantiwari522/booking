package com.booking.service;

import com.booking.entity.Booking;

public interface BookingService {
    Booking bookOffering(Long parentId, Long offeringId);
}
