package com.booking.service;

import com.booking.dto.response.CourseWithTeacherResponse;
import com.booking.dto.response.OfferingSessionsLocalResponse;
import com.booking.entity.Parent;
import com.booking.entity.Booking;

import java.util.List;

public interface ParentService {
    Parent createParent(String name, String timezone);
    List<Booking> getBookings(Long parentId);
    void deleteBooking(Long parentId, Long bookingId);
    List<OfferingSessionsLocalResponse> getOfferingSessionsLocalResponses(Long courseId, Long parentId);
    List<CourseWithTeacherResponse> getCourseWithTeacherResponses();
}
