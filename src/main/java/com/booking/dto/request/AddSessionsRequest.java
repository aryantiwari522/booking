package com.booking.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.util.List;

@Data
public class AddSessionsRequest {
    // list of session start/end pairs in local teacher timezone (iso strings)
    @Valid
    @NotEmpty
    private List<SessionRequest> sessions;
}
