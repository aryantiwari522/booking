package com.booking.dto.request;

import lombok.Data;

@Data
public class SessionRequest {
    private String startLocal; // yyyy-MM-ddTHH:mm
    private String endLocal;
}
