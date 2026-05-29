package com.booking.dto.request;

import lombok.Data;

@Data
public class BookingRequest {
    private Long parentId;
    private Long offeringId;
}
