package com.booking.dto.response;

import lombok.Data;

@Data
public class SessionResponse {
    private Long id;
    private String startUtc;
    private String endUtc;
}
