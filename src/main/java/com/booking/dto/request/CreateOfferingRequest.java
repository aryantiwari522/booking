package com.booking.dto.request;

import lombok.Data;

@Data
public class CreateOfferingRequest {
    private String title;
    private Long courseId;
    private Long teacherId;
    private String timezone; // IANA
}
