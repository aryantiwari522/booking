package com.booking.dto.response;

import lombok.Data;

import java.util.List;

@Data
public class BookingResponse {
    private Long id;
    private Long parentId;
    private Long offeringId;
    private List<SessionResponse> sessions;
}
