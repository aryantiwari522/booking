package com.booking.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SessionLocalTimeResponse {
    private Long id;
    private String startAtLocalTime; // Local time in user's timezone
    private String endAtLocalTime;   // Local time in user's timezone
}
