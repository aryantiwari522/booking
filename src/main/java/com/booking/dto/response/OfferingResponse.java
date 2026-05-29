package com.booking.dto.response;

import lombok.Data;

import java.util.List;

@Data
public class OfferingResponse {
    private Long id;
    private String title;
    private String timezone;
    private List<SessionResponse> sessions;
}
