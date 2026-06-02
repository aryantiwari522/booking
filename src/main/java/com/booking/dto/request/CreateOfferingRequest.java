package com.booking.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CreateOfferingRequest {
    @NotBlank
    private String title;

    private Long courseId;

    private Long teacherId;

    @NotBlank
    private String timezone; // IANA
}
