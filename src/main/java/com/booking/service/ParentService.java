package com.booking.service;

import com.booking.entity.Parent;
import com.booking.entity.Booking;

import java.util.List;

public interface ParentService {
    Parent createParent(String name);
    List<Booking> getBookings(Long parentId);
}
