package com.booking.dto.request;

import lombok.Data;

import java.util.List;

@Data
public class AddSessionsRequest {
    // list of session start/end pairs in local teacher timezone (iso strings)
    private List<SessionRequest> sessions;
}
