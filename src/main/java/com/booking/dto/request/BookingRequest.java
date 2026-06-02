package com.booking.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class BookingRequest {
    @NotNull
    private Long parentId;

    @NotNull
    private Long offeringId;
}
