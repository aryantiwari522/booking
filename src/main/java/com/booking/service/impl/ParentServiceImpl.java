package com.booking.service.impl;

import com.booking.entity.Parent;
import com.booking.entity.Booking;
import com.booking.repository.ParentRepository;
import com.booking.repository.BookingRepository;
import com.booking.service.ParentService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ParentServiceImpl implements ParentService {
    private final ParentRepository parentRepository;
    private final BookingRepository bookingRepository;

    public ParentServiceImpl(ParentRepository parentRepository, BookingRepository bookingRepository) {
        this.parentRepository = parentRepository;
        this.bookingRepository = bookingRepository;
    }

    @Override
    public Parent createParent(String name) {
        Parent p = new Parent();
        p.setName(name);
        return parentRepository.save(p);
    }

    @Override
    public List<Booking> getBookings(Long parentId) {
        return bookingRepository.findByParentId(parentId);
    }
}
